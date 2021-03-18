package com.example.mypedometer.model;

import org.litepal.crud.LitePalSupport;

public class PedometerModel extends LitePalSupport {
    //所走步数
    private int mStepCount;
    //消耗卡路里
    private float mCalorie;
    //行走距离
    private float mDistance;
    //开始记录的时间
    private long mStartTime;
    //结束记录的时间
    private long mStopTime;
    //当前系统总步数
    private int mSystemStepCount;
    //当天日期(年月日)
    private String mDate;


    public int getStepCount() {
        return mStepCount;
    }

    public void setStepCount(int stepCount) {
        mStepCount = stepCount;
    }

    public float getCalorie() {
        return mCalorie;
    }

    public void setCalorie(float calorie) {
        mCalorie = calorie;
    }

    public float getDistance() {
        return mDistance;
    }

    public void setDistance(float distance) {
        mDistance = distance;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    public long getStopTime() {
        return mStopTime;
    }

    public void setStopTime(long stopTime) {
        mStopTime = stopTime;
    }

    public int getSystemStepCount() {
        return mSystemStepCount;
    }

    public void setSystemStepCount(int systemStepCount) {
        mSystemStepCount = systemStepCount;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }
}
