package com.example.trendchart.model;

import android.content.Context;

import com.example.trendchart.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class BesselViewData {
    private Context context;
    private List<BesselPointData> besselPointDataList = new ArrayList<>();
    /**
     * 是否绘制节点
     */
    private boolean isDrawPoint = false;

    public boolean isDataComplete() {
        return isDataComplete;
    }

    public void setDataComplete(boolean dataComplete) {
        isDataComplete = dataComplete;
    }

    /**
     * 是否数据完整（没有缺失的点）
     */
    private boolean isDataComplete =  true;

    /**
     * 温度湿度和pm2.5的最大值
     */

    private int startValue = 0;//y轴显示最小值
    private int maxValue = 30;
    private int levelValue = 10;//比如温度最大值是30，level是10，就一共画三条线

    private int dataNum = 25;
    private int lineWidth = 291;//单位都是dp，下面一样
    private int lineHeight = 100;
    private int besselLineWidth = 290;
    private int marginLeft = 18;//曲线
    private int margingRight = 10;//曲线
    private int dashLineMarginTop = 13;
    private int mY_axisTextHeight = 10;//
    private int mX_axisTextMarginTop = 20;
    private int mX_axisTextMarginLeftEnd = 280;
    private int mY_axisTextMarginLeft = 8;
    private int pointWidth;//每两个点之间的间距
    private int mX_axisItemNum = 25;
    private int mX_axisTextItemNum = 3;
    private int mX_axisItemWidth;//横轴每个单位的间距
    private ArrayList<String> mX_axisTextList = new ArrayList<>();
    private int[] gradientColor = new int[3];
    private float[] gradientPositions = new float[3];
    private boolean isGradient = false;
    private boolean isX_axisScaleHeightSame;
    private int mX_axisScaleHeightBig;
    private int mX_axisScaleHeightSmall;
    private int mX_axisTextWidthBig = 25;
    private int mX_axisTextWidthSmall = 20;
    private boolean isX_axisTextWidthBig;


    public boolean isGradient() {
        return isGradient;
    }

    public void setGradient(boolean gradient) {
        isGradient = gradient;
    }

    public int[] getGradientColor() {
        return gradientColor;
    }

    public void setGradientColor(int[] gradientColor) {
        this.gradientColor = gradientColor;
    }

    public float[] getGradientPositions() {
        return gradientPositions;
    }

    public void setGradientPositions(float[] gradientPositions) {
        this.gradientPositions = gradientPositions;
    }

    public BesselViewData(Context context) {
        this.context = context;
    }

    public List<BesselPointData> getBesselPointDataList() {
        return besselPointDataList;
    }

    public void setBesselPointDataList(
            List<BesselPointData> besselPointDataList) {
        this.besselPointDataList = besselPointDataList;
    }

    public int getStartValue() {
        return startValue;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(int levelValue) {
        this.levelValue = levelValue;
    }

    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }


    public int getPointWidth() {
        return DensityUtils.dp2px(context, besselLineWidth) / (dataNum - 1);
    }

    public int getMarginLeft() {
        return DensityUtils.dp2px(context, marginLeft);
    }

    public void setBesselLineWidth(int value){
        this.besselLineWidth = value;
    }

    public void setMarginLeft(int value){
        this.marginLeft = value;
    }

    public void setLineHeight(int value){
        this.lineHeight = value;
    }

    public int getLineWidth() {
        return DensityUtils.dp2px(context, lineWidth);
    }

    public void setLineWidth(int value){
        this.lineWidth = value;
    }

    public int getLineHeight() {
        return DensityUtils.dp2px(context, lineHeight);
    }

    public int getDashLineMarginTop() {
        return DensityUtils.dp2px(context, dashLineMarginTop);
    }

    public void setDashLineMarginTop(int value){
        this.dashLineMarginTop = value;
    }

    public int getY_axisTextHeight() {
        return DensityUtils.dp2px(context, mY_axisTextHeight);
    }


    public int getY_axisTextLeft(){
        return DensityUtils.dp2px(context, mY_axisTextMarginLeft);
    }
    public int getX_axisTextMarginTop() {
        return DensityUtils.dp2px(context, mX_axisTextMarginTop);
    }

    public boolean isDrawPoint() {
        return isDrawPoint;
    }

    public void setDrawPoint(boolean drawPoint) {
        isDrawPoint = drawPoint;
    }

    public int getX_axisItemNum() {
        return mX_axisItemNum;
    }

    public void setX_axisItemNum(int mX_axisItemNum) {
        this.mX_axisItemNum = mX_axisItemNum;
    }

    public int getX_axisTextItemWidth() {
        return DensityUtils.dp2px(context, lineWidth) / (mX_axisTextItemNum
                - 1);
    }

    public int getX_axisItemWidth(){
        return DensityUtils.dp2px(context, lineWidth) / (mX_axisItemNum - 1);
    }

    public ArrayList<String> getX_axisTextList() {
        return mX_axisTextList;
    }

    public void setX_axisTextList(ArrayList<String> mX_axisTextList) {
        this.mX_axisTextList = mX_axisTextList;
    }

    public int getmX_axisTextItemNum() {
        return mX_axisTextItemNum;
    }

    public void setmX_axisTextItemNum(int mX_axisTextItemNum) {
        this.mX_axisTextItemNum = mX_axisTextItemNum;
    }

    public boolean isX_axisScaleHeightSame() {
        return isX_axisScaleHeightSame;
    }

    public void setX_axisScaleHeightSame(boolean x_axisScaleHeightSame) {
        isX_axisScaleHeightSame = x_axisScaleHeightSame;
    }

    public int getmX_axisScaleHeightBig() {
        return mX_axisScaleHeightBig;
    }

    public void setmX_axisScaleHeightBig(int mX_axisScaleHeightBig) {
        this.mX_axisScaleHeightBig = mX_axisScaleHeightBig;
    }

    public int getmX_axisScaleHeightSmall() {
        return mX_axisScaleHeightSmall;
    }

    public void setmX_axisScaleHeightSmall(int mX_axisScaleHeightSmall) {
        this.mX_axisScaleHeightSmall = mX_axisScaleHeightSmall;
    }

    public int getX_axisTextWidthBig(){
        return DensityUtils.dp2px(context, mX_axisTextWidthBig);
    }

    public int getX_axisTextWidthSmall(){
        return DensityUtils.dp2px(context, mX_axisTextWidthSmall);
    }

    public boolean isX_axisTextWidthBig() {
        return isX_axisTextWidthBig;
    }

    public void setX_axisTextWidthBig(boolean x_axisTextWidthBig) {
        isX_axisTextWidthBig = x_axisTextWidthBig;
    }
}
