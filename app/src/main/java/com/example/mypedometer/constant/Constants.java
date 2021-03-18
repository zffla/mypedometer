package com.example.mypedometer.constant;

public class Constants {
    //service的运行状态
    public static final int SERVICE_RUNNING=1;
    public static final int SERVICE_NOT_RUNNING=0;

    //SharedPreference的name
    public static final String PREFS_NAME="info";

    //传感器设置数据
    public static final float[] SENSITIVE_ARRAY = {1.97f, 2.96f, 4.44f, 6.66f, 10.0f, 15.0f, 22.50f, 33.75f, 50.62f};
    public static final int[] INTERVAL_ARRAY = {100, 200, 300, 400, 500, 600, 700, 800};

    //SharedPreference中相应数据保存的key
    public static final String SENSITIVITY = "sensitivity";
    public static final String INTERVAL = "interval";
    public static final String BODY_HEIGHT = "body_height";
    public static final String BODY_WEIGHT = "body_weight";
    public static final String TARGET_STEPS = "target_steps";

    //保存数据时间间隔,30s/60s
    public static final int SAVA_DATA_SHORT=30000;
    public static final int SAVA_DATA_LONG=60000;

    //更新UI时间间隔,1s
    public static final int UPDATE_UI_SHORT=1000;

    //运动数据类型
    public static final String DATA_TYPE="data_type";
    public static final int DATA_STEPS=0;
    public static final int DATA_CALORIE=1;

    //运动数据周期
    public static final String DATA_PERIOD="data_period";
    public static final int DATA_WEEK=0;
    public static final int DATA_MONTH=1;
}
