package com.example.trendchart.view;

import com.example.trendchart.model.BesselPoint;
import com.example.trendchart.model.BesselPointData;
import com.example.trendchart.model.BesselViewData;

import java.util.List;

public class BesselCalculator {
    /**
     * 光滑因子
     */
    private float smoothness = 0.33f;
    private BesselViewData pointData;

    public static final float NULL_VALUE_FLAG = -10000.0f;//代表这个位置是空值，例如某一天或某一小时没有值，就用这个值来标记的valueY

    public BesselCalculator(BesselViewData pointData) {
        this.pointData = pointData;
    }

    public void compute() {
        if(pointData == null)
            return;
        makeupLackingPoint();
        computePointCoordinate();
        computeBesselPoints();
    }

    /**
     * 计算贝塞尔结点
     */
    private void computeBesselPoints() {
        List<BesselPointData> besselPointDataList = pointData.getBesselPointDataList();
        for (BesselPointData data : besselPointDataList) {
            List<BesselPoint> besselPoints = data.getBesselPoints();
            List<BesselPoint> points = data.getPoints();
            int count = points.size();
            besselPoints.clear();
            for (int i = 0; i < count; i++) {
                if (i == 0 || i == count - 1) {
                    computeUnMonotonePoints(i, points, besselPoints);
                } else {
                    BesselPoint p0 = points.get(i - 1);
                    BesselPoint p1 = points.get(i);
                    BesselPoint p2 = points.get(i + 1);
                    if ((p1.y - p0.y) * (p1.y - p2.y) >= 0) {// 极值点
                        computeUnMonotonePoints(i, points, besselPoints);
                    } else {
                        computeMonotonePoints(i, points, besselPoints);
                    }
                }
            }
        }

    }

    /**
     * 计算非单调情况的贝塞尔结点
     */
    private void computeUnMonotonePoints(int i, List<BesselPoint> points,
                                         List<BesselPoint> besselPoints) {
        if (i == 0) {
            BesselPoint p1 = points.get(0);
            BesselPoint p2 = points.get(1);
            besselPoints.add(p1);
            besselPoints.add(new BesselPoint(p1.x + (p2.x - p1.x) * smoothness, p1.y));
        } else if (i == points.size() - 1) {
            BesselPoint p0 = points.get(i - 1);
            BesselPoint p1 = points.get(i);
            besselPoints.add(new BesselPoint(p1.x - (p1.x - p0.x) * smoothness, p1.y));
            besselPoints.add(p1);
        } else {
            BesselPoint p0 = points.get(i - 1);
            BesselPoint p1 = points.get(i);
            BesselPoint p2 = points.get(i + 1);
            besselPoints.add(new BesselPoint(p1.x - (p1.x - p0.x) * smoothness, p1.y));
            besselPoints.add(p1);
            besselPoints.add(new BesselPoint(p1.x + (p2.x - p1.x) * smoothness, p1.y));
        }
    }

    /**
     * 计算单调情况的贝塞尔结点
     */
    private void computeMonotonePoints(int i, List<BesselPoint> points,
                                       List<BesselPoint> besselPoints) {
        BesselPoint p0 = points.get(i - 1);
        BesselPoint p1 = points.get(i);
        BesselPoint p2 = points.get(i + 1);
        float k = (p2.y - p0.y) / (p2.x - p0.x);
        float b = p1.y - k * p1.x;
        BesselPoint p01 = new BesselPoint();
        p01.x = p1.x - (p1.x - (p0.y - b) / k) * smoothness;
        p01.y = k * p01.x + b;
        besselPoints.add(p01);
        besselPoints.add(p1);
        BesselPoint p11 = new BesselPoint();
        p11.x = p1.x + (p2.x - p1.x) * smoothness;
        p11.y = k * p11.x + b;
        besselPoints.add(p11);
    }

    public void setSmoothness(float smoothness) {
        this.smoothness = smoothness;
    }

    private void computePointCoordinate() {
        List<BesselPointData> besselPointDataList = pointData.getBesselPointDataList();
        for (BesselPointData data : besselPointDataList) {
            List<BesselPoint> points = data.getPoints();

            float pointWidth = pointData.getPointWidth();
            for (int i = 0; i < points.size(); i++) {
                BesselPoint point = points.get(i);
                point.x = pointWidth * i + pointData.getMarginLeft();
                float ratio =
                        1 - (point.valueY - pointData.getStartValue()) / (pointData.getMaxValue()
                                - pointData.getStartValue());
                point.y = pointData.getLineHeight() * ratio + pointData.getDashLineMarginTop();
            }
        }

    }

    /**
     * 补全缺失的点
     */
    private void makeupLackingPoint() {
        List<BesselPointData> besselPointDataList = pointData.getBesselPointDataList();
        for (BesselPointData data : besselPointDataList) {
            List<BesselPoint> points = data.getPoints();
            for (int i = 0; i < points.size(); i++) {
                BesselPoint point = points.get(i);
                if (!point.isInvalidate) {
                    pointData.setDataComplete(false);
                    point.valueY = getGuessYValue(i, points);
                }

            }

        }
    }

    private float getGuessYValue(int index, List<BesselPoint> points) {
        float left = NULL_VALUE_FLAG;
        float right = NULL_VALUE_FLAG;

        for (int i = index - 1; i >= 0; i--) {
            if (points.get(i).isInvalidate) {
                left = points.get(i).valueY;
                break;
            }
        }
        for (int i = index + 1; i < points.size(); i++) {
            if (points.get(i).isInvalidate) {
                right = points.get(i).valueY;
                break;
            }
        }

        if (left == NULL_VALUE_FLAG
                && right == NULL_VALUE_FLAG) {
            return NULL_VALUE_FLAG;
        }
        if (left == NULL_VALUE_FLAG) {
            return right;
        }
        if (right == NULL_VALUE_FLAG) {
            return left;
        }

        return (left + right) / 2;
    }
}
