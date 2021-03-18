package com.example.mypedometer.utils;

import android.content.Context;

import com.example.mypedometer.constant.Constants;

public class SettingUtils {

    private SPUtils mSPUtils;

    public SettingUtils(Context context) {
        mSPUtils = new SPUtils(context);
    }


    /**
     * 设置身高
     *
     * @param height 身高
     */
    public void setBodyHeight(float height) {
        mSPUtils.setFloatPrefs(Constants.BODY_HEIGHT, height);
    }


    /**
     * 获取身高
     *
     * @return 身高
     */
    public float getBodyHeight() {
        return mSPUtils.getFloatPrefs(Constants.BODY_HEIGHT);
    }

    /**
     * 获取步长
     *
     * @return 步长
     */
    public float getStepLength() {
        float height = getBodyHeight();
        float stepLen;
        if (height == 0.0f) {
            stepLen = 50.0f;
        } else {
            stepLen = height * 0.45f;
        }
        return stepLen;
    }


    /**
     * 设置传感器灵敏度
     *
     * @param sensitivity 灵敏度
     */
    public void setSensitivity(float sensitivity) {
        mSPUtils.setFloatPrefs(Constants.SENSITIVITY, sensitivity);
    }


    /**
     * 获取传感器灵敏度
     *
     * @return 灵敏度
     */
    public double getSensitivity() {
        //灵敏度
        float sensitivity = mSPUtils.getFloatPrefs(Constants.SENSITIVITY);
        if (sensitivity == 0.0f) {
            return 10.0f;
        }
        return sensitivity;
    }


    /**
     * 获取时间间隔
     *
     * @return 时间间隔
     */
    public int getInterval() {
        int interval = mSPUtils.getIntPrefs(Constants.INTERVAL);
        if (interval == 0) {
            return 200;
        }
        return interval;
    }

    /**
     * 设置时间间隔
     *
     * @param interval 时间间隔
     */
    public void setInterval(int interval) {
        mSPUtils.setIntPrefs(Constants.INTERVAL, interval);
    }


    /**
     * 设置体重
     *
     * @param weight 体重
     */
    public void setBodyWeight(float weight) {
        mSPUtils.setFloatPrefs(Constants.BODY_WEIGHT, weight);
    }


    /**
     * 获取体重
     *
     * @return 体重
     */
    public float getBodyWeight() {
        float weight = mSPUtils.getFloatPrefs(Constants.BODY_WEIGHT);
        if (weight == 0.0f) {
            return 60.0f;
        } else {
            return weight;
        }
    }


    /**
     * 设置目标步数
     * @param steps 目标步数
     */
    public void setTargetSteps(int steps){
        mSPUtils.setIntPrefs(Constants.TARGET_STEPS,steps);
    }

    /**
     * 获取目标步数
     * @return 步数
     */
    public int getTargetSteps() {
        int steps = mSPUtils.getIntPrefs(Constants.TARGET_STEPS);
        if (steps == 0) {
            return 5000;
        } else {
            return steps;
        }
    }
}
