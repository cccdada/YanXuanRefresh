package com.sheng.yanxuanrefresh.refresh;

//点坐标的模型 包括x，y两个属性
public class Point {
    public float pointX;
    public float pointY;

    public Point() {
    }

    public Point(float pointX, float pointY) {
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public void setPointX(float pointX) {
        this.pointX = pointX;
    }

    public void setPointY(float pointY) {
        this.pointY = pointY;
    }
}