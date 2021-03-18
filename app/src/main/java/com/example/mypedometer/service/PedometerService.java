package com.example.mypedometer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.example.mypedometer.constant.Constants;
import com.example.mypedometer.model.PedometerModel;
import com.example.mypedometer.utils.LogUtils;
import com.example.mypedometer.utils.SettingUtils;
import com.example.mypedometer.utils.TimeUtils;

import org.litepal.LitePal;

public class PedometerService extends Service implements SensorEventListener {

    //记录行走数据
    private PedometerModel mPedometerModel;
    //传感器
    private SensorManager mSensorManager;
    //传感器监听器
//    private PedometerListener mPedometerListener;
    //当前运行状态
    private int runningStatus = 0;
    //获取已设置的相关数据
    private SettingUtils mSettingUtils;
    private int duration = Constants.SAVA_DATA_SHORT;
    private int currentStep;

    private final String TAG = "PedometerService";

    private Handler mHandler = new Handler();
    private Runnable mSaveRunnable = new Runnable() {
        @Override
        public void run() {
            saveDataToDB();
            mHandler.postDelayed(mSaveRunnable, duration);
        }
    };


    public PedometerService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化setting
        mSettingUtils = new SettingUtils(this);
        LogUtils.logd(TAG, "service created");
        new Thread(new Runnable() {
            @Override
            public void run() {
                startSteps();
            }
        }).start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //这里获取的是系统上次启动以来的总步数
        int step = (int) event.values[0];
        if (currentStep == 0) {
            if (mPedometerModel.getSystemStepCount() == 0) {
                //每天开始计步时，先设置当前刚开始的系统总步数
                mPedometerModel.setSystemStepCount(step);
                mPedometerModel.setStartTime(System.currentTimeMillis());
            }
            currentStep=1;
        } else {
            currentStep = step - mPedometerModel.getSystemStepCount();
        }
        mPedometerModel.setStepCount(currentStep);
        mPedometerModel.setStopTime(System.currentTimeMillis());
        LogUtils.logd("Listener", "当前步数" + currentStep);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public class PedometerBinder extends Binder {


        public int getStepsCount() {
            if (mPedometerModel != null) {
                LogUtils.logd(TAG, "获取步数" + mPedometerModel.getStepCount());
                return mPedometerModel.getStepCount();
            }
            return 0;
        }

        public void newDayReset(PedometerModel model) {
            reset(model);
        }


        public void resetCount() {
            if (mPedometerModel != null) {
                mPedometerModel.setStepCount(0);
                mPedometerModel.setStartTime(System.currentTimeMillis());
                mPedometerModel.setCalorie(0.0f);
                mPedometerModel.setDistance(0.0f);
//                mPedometerListener.resetStep();
            }
        }


        public void stopStepsCount() {
            stopSteps();
        }


        public double getCalorie() {
            if (mPedometerModel != null) {
                return getCalorieByStep(mPedometerModel.getStepCount());
            }
            return 0;
        }


        public double getDistance() {
            if (mPedometerModel != null) {
                return mPedometerModel.getStepCount() * mSettingUtils.getStepLength() / 100000;
            }
            return 0;
        }


        public void saveData() {
            saveDataToDB();
        }


        public long getStartTimestamp() {
            if (mPedometerModel != null) {
                return mPedometerModel.getStartTime();
            }
            return System.currentTimeMillis();
        }


        public int getServiceRunningStatus() {
            return runningStatus;
        }

        /**
         * 设置保存时间间隔
         *
         * @param time
         */
        public void setDuration(int time) {
            duration = time;
        }
    }


    /**
     * 新的一天重置数据
     *
     * @param model 传入的新model
     */
    private void reset(PedometerModel model) {
        mPedometerModel = model;
//        mPedometerListener.setModel(model);
        resetStep();
    }


    /**
     * 重置步数
     */
    private void resetStep() {
        currentStep = 0;
    }

    /**
     * 保存数据至数据库
     */
    private void saveDataToDB() {
        if (mPedometerModel != null && mSettingUtils != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mPedometerModel.setDistance(mPedometerModel.getStepCount() * mSettingUtils.getStepLength());
                    mPedometerModel.setCalorie(getCalorieByStep(mPedometerModel.getStepCount()));
                    mPedometerModel.save();
                }
            }).start();
        }
    }


    /**
     * 根据步数获取消耗的卡路里值
     *
     * @param stepCount 步数
     * @return 卡路里
     */
    private float getCalorieByStep(int stepCount) {
        //double METRIC_RUNNING_FACTOR = 1.02784823;//跑步
        double METRIC_WALKING_FACTOR = 0.708;//走路
        // 跑步热量（kcal）＝体重（kg）×距离（公里）×1.02784823
        // 走路热量（kcal）＝体重（kg）×距离（公里）×0.708
        double mCalories = (mSettingUtils.getBodyWeight() * METRIC_WALKING_FACTOR) * mSettingUtils.getStepLength() * stepCount / 100000.0;
        return (float) mCalories;
    }


    /**
     * 停止计步
     */
    private void stopSteps() {
        if (mSensorManager != null) {
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mSensorManager.unregisterListener(this, sensor);
            runningStatus = Constants.SERVICE_NOT_RUNNING;
            mPedometerModel.setStopTime(System.currentTimeMillis());
        }
    }


    /**
     * 开始计步
     */
    private void startSteps() {
        String dateNow = TimeUtils.TimeToDate(System.currentTimeMillis());
        PedometerModel model = LitePal.where("mDate=?", dateNow).findFirst(PedometerModel.class);
        if (model == null) {
            model = new PedometerModel();
            model.setDate(dateNow);
            model.save();
        }
        mPedometerModel = model;
        currentStep = mPedometerModel.getStepCount();
        //初始化SensorManager
        mSensorManager = (SensorManager) getSystemService(android.content.Context.SENSOR_SERVICE);
        //初始化监听器
//        mPedometerListener = new PedometerListener(mPedometerModel);
        //判断当前硬件设备是否支持计步传感器
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
            LogUtils.logd("PedometerService start", "支持计步传感器");
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            //注册传感器
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        mHandler.post(mSaveRunnable);
        runningStatus = Constants.SERVICE_RUNNING;

    }

    private PedometerBinder mBinder = new PedometerBinder();

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.logd(TAG, "service binded");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveDataToDB();
        stopSteps();
        if (mHandler != null) {
            mHandler.removeCallbacks(mSaveRunnable);
        }
    }
}
