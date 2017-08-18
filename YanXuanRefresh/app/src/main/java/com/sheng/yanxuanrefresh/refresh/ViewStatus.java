package com.sheng.yanxuanrefresh.refresh;

public enum ViewStatus {
    START("初始状态",0),
    REFRESHING("正在刷新",1), //这个状态由刷新框架设置
    START_SLOGAN("绘制标语",2),
    START_SHAKE("抖动",3);

    private String name;
    private int id;

    ViewStatus(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}