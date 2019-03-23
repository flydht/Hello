package com.example.trendchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.trendchart.model.BesselPoint;
import com.example.trendchart.model.BesselPointData;
import com.example.trendchart.model.BesselViewData;
import com.example.trendchart.util.DensityUtils;

import java.util.List;

public class BesselTrendView extends View {

    private Paint besselPaint;
    private Path curvePath;
    private Paint dashLinePaint;
    private Path dashLinePath;
    private BesselViewData pointData;
    private Paint axisPaint;
    private boolean isDrawBesselLine = true;
    private Paint dashBesselPaint;
    private Paint XAxisScalePaint;
    private Paint pointValuePaint;

    private int besselPaintStrokeWidth = 3;
    private PathEffect mEffects;
    private LinearGradient linearGradient;
    private Context mContext;

    public BesselTrendView(Context context) {
        this(context, null);
    }

    public BesselTrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        besselPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dashLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        besselPaint.setColor(Color.WHITE);
        besselPaint.setAntiAlias(true);
        besselPaint.setStrokeWidth(besselPaintStrokeWidth);
        curvePath = new Path();

        dashBesselPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dashBesselPaint.setColor(0x33FFFFFF);
        dashBesselPaint.setStrokeWidth(besselPaintStrokeWidth);

        dashLinePath = new Path();
        dashLinePaint.setPathEffect(new DashPathEffect(new float[]{4, 2}, 0));

        axisPaint = new Paint();
        axisPaint.setColor(0x66ffffff);
        axisPaint.setAntiAlias(true);
        axisPaint.setTextSize(DensityUtils.sp2px(context, 10));

        XAxisScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        XAxisScalePaint.setColor(Color.WHITE);
        XAxisScalePaint.setAntiAlias(true);
        XAxisScalePaint.setStrokeWidth(0.5f);

        pointValuePaint = new Paint();
        pointValuePaint.setAntiAlias(true);
        pointValuePaint.setTextSize(DensityUtils.sp2px(context, 10));
        pointValuePaint.setColor(Color.WHITE);

        mEffects = new DashPathEffect(
                new float[]{DensityUtils.dp2px(mContext, 3.33f), DensityUtils.dp2px(mContext, 3f)},
                0);
    }

    public void setData(BesselViewData pointData) {
        this.pointData = pointData;
    }

    public void setDrawBesselLine(boolean isFlag) {
        isDrawBesselLine = isFlag;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDashLine(canvas);
        drawBesselLine(canvas);
        drawY_axis(canvas);
        drawX_axisScale(canvas);
        drawX_axisText(canvas);
    }

    private void drawDashLine(Canvas canvas) {

        if (pointData != null) {
            int level = pointData.getLevelValue();
            int levelNum = 0, levelHeight = 0;
            if (level != 0) {
                levelNum = (pointData.getMaxValue() - pointData.getStartValue()) / level;
                levelHeight = pointData.getLineHeight() / levelNum;
            }

            for (int i = 0; i <= levelNum; i++) {
                dashLinePath.reset();
                dashLinePaint.setColor(0x4DFFFFFF);
                dashLinePaint.setStyle(Paint.Style.STROKE);
                dashLinePaint.setStrokeWidth(0.5F);
                dashLinePath.moveTo(pointData.getMarginLeft(),
                        i * levelHeight + pointData.getDashLineMarginTop());
                dashLinePath.lineTo(pointData.getMarginLeft() + pointData.getLineWidth(),
                        i * levelHeight + pointData.getDashLineMarginTop());
                canvas.drawPath(dashLinePath, dashLinePaint);
            }
        }
    }

    private void drawBesselLine(Canvas canvas) {
        if (pointData != null) {
            int layerId = -1;

//            if (pointData.isDrawPoint()) {
//                layerId = canvas
//                        .saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
//            }
            List<BesselPointData> besselPointDataList = pointData.getBesselPointDataList();
            for (BesselPointData data : besselPointDataList) {
                curvePath.reset();
                List<BesselPoint> list = data.getBesselPoints();
                for (int i = 0; i < list.size(); i = i + 3) {
                    if (i == 0) {
                        curvePath.moveTo(list.get(i).x, list.get(i).y);
                    } else {
                        curvePath.cubicTo(list.get(i - 2).x, list.get(i - 2).y, list.get(i - 1).x,
                                list.get(i - 1).y, list.get(i).x, list.get(i).y);
                    }
                }

                besselPaint.setPathEffect(null);
                if (pointData.isGradient()) {
                    linearGradient = new LinearGradient(0, pointData.getDashLineMarginTop(), 0,
                            pointData.getLineHeight(), pointData.getGradientColor(), null,
                            Shader.TileMode.CLAMP);
                    besselPaint.setShader(linearGradient);
                } else {
                    besselPaint.setShader(null);
                    besselPaint.setColor(data.getPaintColor());
                }
                besselPaint.setStyle(Paint.Style.STROKE);
                if (pointData.isDataComplete()) {
                    canvas.drawPath(curvePath, besselPaint);// 绘制光滑曲线
                } else {
                    Rect rect = new Rect();
                    rect.top = 0;
                    rect.bottom = getHeight();
                    for (int i = 0; i < (data.getPoints().size() - 1); i++) {
                        drawLineSegment(canvas, i, rect, curvePath, data.getPoints());
                    }
                }

                if (data.isDrawPointValue()) {
                    float diffValue;
                    if (data.isDrawPointValueAbove()) {
                        diffValue = -DensityUtils.dp2px(mContext, 6);
                    } else {
                        diffValue = DensityUtils.dp2px(mContext, 11);
                    }
//                    for (BesselPoint point : data.getPoints()) {
////                        if (point.willDrawing) {
////                            besselPaint.setStyle(Style.FILL);
////                            besselPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
////                            canvas.drawCircle(point.x, point.y, 8, besselPaint);
////                            besselPaint.setXfermode(null);
////                            besselPaint.setStyle(Style.STROKE);
////                            besselPaint.setColor(Color.WHITE);
////                            besselPaint.setStrokeWidth(besselPaintStrokeWidth);
////                            canvas.drawCircle(point.x, point.y, 8, besselPaint);
////                        }
//                        canvas.drawText((int)point.valueY+"",point.x,point.y + diffValue,pointValuePaint);
//                    }
                    int size = data.getPoints().size();
                    for (int i = 0; i < size; i++) {
                        BesselPoint point = data.getPoints().get(i);
                        if (point.isInvalidate) {
                            int valueY = (int) point.valueY;
                            float tempWidth;
                            int tempWidthResult;
                            if (Math.abs(valueY) < 10) {
                                tempWidth = 5f;
                            } else if (Math.abs(valueY) < 100) {
                                tempWidth = 10.7f;
                            } else {
                                tempWidth = 16f;
                            }
                            tempWidthResult = DensityUtils.dp2px(mContext, tempWidth);
                            if (i == 0) {
                                canvas.drawText((int) point.valueY + "", point.x,
                                        point.y + diffValue, pointValuePaint);
                            } else if (i == size - 1) {
                                canvas.drawText((int) point.valueY + "", point.x - tempWidthResult,
                                        point.y + diffValue, pointValuePaint);
                            } else {
                                canvas.drawText((int) point.valueY + "",
                                        point.x - tempWidthResult / 2, point.y + diffValue,
                                        pointValuePaint);
                            }
                        }
                    }
                }

//            if (pointData.isDrawPoint()) {
//                canvas.restoreToCount(layerId);
//            }
            }
        }
    }

    private void drawLineSegment(Canvas canvas, int i, Rect rect, Path path,
                                 List<BesselPoint> points) {

        if (!points.get(i + 1).isInvalidate && !points.get(i).isInvalidate) {
            canvas.save();
            besselPaint.setColor(0x66FFFFFF);
            besselPaint.setPathEffect(mEffects);
            rect.left = (int) points.get(i).x;
            rect.right = (int) points.get(i + 1).x;
            canvas.clipRect(rect);
            canvas.drawPath(path, besselPaint);
            canvas.restore();

        } else if (!points.get(i + 1).isInvalidate) {
            canvas.save();
            besselPaint.setColor(Color.WHITE);
            besselPaint.setPathEffect(null);
            rect.left = (int) points.get(i).x;
            rect.right = (int) (points.get(i).x + points.get(i + 1).x) / 2;
            canvas.clipRect(rect);
            canvas.drawPath(path, besselPaint);
            canvas.restore();

            canvas.save();
            besselPaint.setColor(0x66FFFFFF);
            besselPaint.setPathEffect(mEffects);
            rect.left = (int) (points.get(i).x + points.get(i + 1).x) / 2;
            rect.right = (int) points.get(i + 1).x;
            canvas.clipRect(rect);
            canvas.drawPath(path, besselPaint);
            canvas.restore();
        } else if (!points.get(i).isInvalidate) {
            canvas.save();
            besselPaint.setColor(0x66FFFFFF);
            besselPaint.setPathEffect(mEffects);
            rect.left = (int) points.get(i).x;
            rect.right = (int) (points.get(i).x + points.get(i + 1).x) / 2;
            canvas.clipRect(rect);
            canvas.drawPath(path, besselPaint);
            canvas.restore();

            canvas.save();
            besselPaint.setColor(Color.WHITE);
            besselPaint.setPathEffect(null);
            rect.left = (int) (points.get(i).x + points.get(i + 1).x) / 2;
            rect.right = (int) points.get(i + 1).x;
            canvas.clipRect(rect);
            canvas.drawPath(path, besselPaint);
            canvas.restore();
        } else {
            canvas.save();
            besselPaint.setColor(Color.WHITE);
            besselPaint.setPathEffect(null);
            rect.left = (int) points.get(i).x;
            rect.right = (int) points.get(i + 1).x;
            canvas.clipRect(rect);
            canvas.drawPath(path, besselPaint);
            canvas.restore();
        }
    }

    private void drawY_axis(Canvas canvas) {
        if (pointData != null) {
            int level = pointData.getLevelValue();
            int levelNum = (pointData.getMaxValue() - pointData.getStartValue()) / level;
            int levelHeight = pointData.getLineHeight() / levelNum;
            for (int i = 0; i <= levelNum; i++) {
                String text = String.valueOf(pointData.getMaxValue() - i * level);
                canvas.drawText(text,
                        pointData.getMarginLeft() + pointData.getLineWidth() + pointData
                                .getY_axisTextLeft(),
                        i * levelHeight + pointData.getDashLineMarginTop()
                                + pointData.getY_axisTextHeight() / 2, axisPaint);
            }
        }
    }

    private void drawX_axisText(Canvas canvas) {

        if (pointData != null) {
            int y = pointData.getDashLineMarginTop() + pointData.getLineHeight() + pointData
                    .getX_axisTextMarginTop();
            int textWidth;
            if (pointData.isX_axisTextWidthBig()) {
                textWidth = pointData.getX_axisTextWidthBig();
            } else {
                textWidth = pointData.getX_axisTextWidthSmall();
            }
            int x, size;
            size = pointData.getX_axisTextList().size();
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    x = pointData.getMarginLeft() + pointData.getX_axisTextItemWidth() * i;
                } else if (i == size - 1) {
                    x = pointData.getMarginLeft() + pointData.getX_axisTextItemWidth() * i
                            - textWidth;
                } else {
                    x = pointData.getMarginLeft() + pointData.getX_axisTextItemWidth() * i - (
                            textWidth / 2);
                }
                canvas.drawText(pointData.getX_axisTextList().get(i), x, y,
                        axisPaint);
            }
        }
    }

    //画X轴刻度
    private void drawX_axisScale(Canvas canvas) {
        if (pointData != null) {
            int startY = pointData.getDashLineMarginTop() + pointData.getLineHeight();
            int stopY = pointData.getDashLineMarginTop() + pointData.getLineHeight() + pointData
                    .getmX_axisScaleHeightBig();
            for (int i = 0; i < pointData.getX_axisItemNum(); i++) {
                if (!pointData.isX_axisScaleHeightSame()) {
                    if (i % 12 == 0) {
                        stopY = pointData.getDashLineMarginTop() + pointData.getLineHeight()
                                + pointData.getmX_axisScaleHeightBig();
                        XAxisScalePaint.setColor(Color.WHITE);
                        canvas.drawLine(
                                pointData.getMarginLeft() + pointData.getX_axisItemWidth() * i,
                                startY,
                                pointData.getMarginLeft() + pointData.getX_axisItemWidth() * i
                                , stopY, XAxisScalePaint);
                    } else {
                        stopY = pointData.getDashLineMarginTop() + pointData.getLineHeight()
                                + pointData.getmX_axisScaleHeightSmall();
                        XAxisScalePaint.setColor(0x80ffffff);
                        canvas.drawLine(
                                pointData.getMarginLeft() + pointData.getX_axisItemWidth() * i,
                                startY,
                                pointData.getMarginLeft() + pointData.getX_axisItemWidth() * i
                                , stopY, XAxisScalePaint);
                    }
                } else {
                    canvas.drawLine(pointData.getMarginLeft() + pointData.getX_axisItemWidth() * i,
                            startY, pointData.getMarginLeft() + pointData.getX_axisItemWidth() * i
                            , stopY, XAxisScalePaint);
                }
            }
        }
    }

}
