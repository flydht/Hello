package com.example.trendchart.model;

import com.example.trendchart.model.BesselPoint;

import java.util.List;

public class BesselPointData {
    /**
     * 序列点集合
     */
    private List<BesselPoint> points;
    /**
     * 贝塞尔曲线点
     */
    private List<BesselPoint> besselPoints;
    /**
     * 贝塞尔曲线颜色（非渐变）
     */
    private int paintColor;

    private boolean isDrawPointValue;
    private boolean isDrawPointValueAbove;

    public BesselPointData(List<BesselPoint> points,
                           List<BesselPoint> besselPoints) {
        this.points = points;
        this.besselPoints = besselPoints;
    }

    public int getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }

    public List<BesselPoint> getPoints() {
        return points;
    }

    public void setPoints(List<BesselPoint> points) {
        this.points = points;
    }

    public List<BesselPoint> getBesselPoints() {
        return besselPoints;
    }

    public void setBesselPoints(List<BesselPoint> besselPoints) {
        this.besselPoints = besselPoints;
    }

    public boolean isDrawPointValue() {
        return isDrawPointValue;
    }

    public void setDrawPointValue(boolean drawPointValue) {
        isDrawPointValue = drawPointValue;
    }

    public boolean isDrawPointValueAbove() {
        return isDrawPointValueAbove;
    }

    public void setDrawPointValueAbove(boolean drawPointValueAbove) {
        isDrawPointValueAbove = drawPointValueAbove;
    }
}
