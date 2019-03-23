package com.example.trendchart.model;

public class BesselPoint {

    /**
     * 是否在图形中绘制出此结点
     */
    public boolean willDrawing;
    /**
     * 在canvas中的X坐标
     */
    public float x;
    /**
     * 在canvas中的Y坐标
     */
    public float y;
    /**
     * 实际的X数值
     */
    public int valueX;
    /**
     * 实际的Y数值
     */
    public float valueY;
    /**
     * 这一点是否存在
     */
    public boolean isInvalidate;

    public BesselPoint() {
    }

    public BesselPoint(int valueX, float valueY, boolean willDrawing,boolean isInvalidate) {
        this.valueX = valueX;
        this.valueY = valueY;
        this.willDrawing = willDrawing;
        this.isInvalidate = isInvalidate;
    }

    public BesselPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
