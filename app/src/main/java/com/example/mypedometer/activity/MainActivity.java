package com.example.mypedometer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypedometer.R;
import com.example.mypedometer.constant.Constants;
import com.example.mypedometer.model.PedometerModel;
import com.example.mypedometer.service.PedometerService;
import com.example.mypedometer.utils.ActivityCollector;
import com.example.mypedometer.utils.ChartDataUtils;
import com.example.mypedometer.utils.LogUtils;
import com.example.mypedometer.utils.SettingUtils;
import com.example.mypedometer.utils.TimeUtils;
import com.example.mypedometer.view.CircleProgressBar;

import org.litepal.LitePal;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.view.ColumnChartView;

public class MainActivity extends BaseActivity {

    private TextView mTvStepsCount;
    private TextView mTvCalorie;
    private TextView mTvDistance;
    private TextView mTvTime;
    private TextView mTvTargetSteps;
    private ColumnChartView mChartSteps, mChartCalorie;
    private TextView mTvCardDistance, mTvCardCalorie;

    private CardView mCardSteps;
    private CardView mCardCalorie;

    private CircleProgressBar mProgressBar;

    private Button mBtnReset;
    private PedometerService.PedometerBinder mPedometerBinder;

    private int mTargetSteps;
    private BroadcastReceiver mReceiver;
    private final String TAG = "Main Activity";

    private Handler mHandler;
    private Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            updateUI();
            mHandler.postDelayed(mRunnable,Constants.UPDATE_UI_SHORT);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        initView();
        initListener();
        initData();
        initBroadcastReceiver();
        //创建数据库
        LitePal.getDatabase();
        LogUtils.logd("MainActivity", "数据库创建成功");

        //初始化handler
        mHandler = new Handler();

        //开启及绑定服务
        Intent intent = new Intent(MainActivity.this, PedometerService.class);
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.post(mRunnable);
        mTargetSteps = new SettingUtils(this).getTargetSteps();
        LogUtils.logd(TAG,"onResume target "+mTargetSteps);
        mTvTargetSteps.setText(String.format(getResources().getString(R.string.target_steps), mTargetSteps));
    }

    @Override
    public void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTvStepsCount = findViewById(R.id.tv_step_count);
        mTvCalorie = findViewById(R.id.tv_calorie);
        mTvDistance = findViewById(R.id.tv_distance);
        mTvTime = findViewById(R.id.tv_time);
        mTvTargetSteps=findViewById(R.id.tv_step_goal);
        mProgressBar=findViewById(R.id.progress_bar);

        mBtnReset = findViewById(R.id.btn_reset);

        mChartSteps = findViewById(R.id.preview_steps_chart);
        mChartCalorie = findViewById(R.id.preview_calorie_chart);

        mTvCardDistance = findViewById(R.id.tv_steps_distance);
        mTvCardCalorie = findViewById(R.id.tv_card_calorie);

        mCardSteps=findViewById(R.id.card_steps);
        mCardCalorie=findViewById(R.id.card_calorie);
//        mBtnStart = findViewById(R.id.btn_start);
    }

    @Override
    public void initListener() {


        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("该操作将重置步数为0，确定重置吗");
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mPedometerBinder!=null){
                            mPedometerBinder.resetCount();
                            updateUI();
                            saveData();
                        }else {
                            Toast.makeText(MainActivity.this,"当前尚未启动计步",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.create().show();
            }
        });


        mCardCalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=ChartActivity.newInstance(MainActivity.this);
                intent.putExtra(Constants.DATA_TYPE,Constants.DATA_CALORIE);
                startActivity(intent);
            }
        });

        mCardSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=ChartActivity.newInstance(MainActivity.this);
                intent.putExtra(Constants.DATA_TYPE,Constants.DATA_STEPS);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        ColumnChartData dataSteps= ChartDataUtils.getRecentStepsData();
        ColumnChartData dataCalorie=ChartDataUtils.getRecentCalorieData();
        mChartSteps.setColumnChartData(dataSteps);
        mChartCalorie.setColumnChartData(dataCalorie);
        mChartSteps.setZoomEnabled(false);
        mChartCalorie.setZoomEnabled(false);
    }

    private void initBroadcastReceiver() {
        //注册亮屏、息屏广播等
        IntentFilter filter = new IntentFilter();
        // 息屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        //监听日期变化
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    //亮屏广播
                    LogUtils.logd(TAG, "Screen on");
                    if (mPedometerBinder!=null){
                        mPedometerBinder.setDuration(Constants.SAVA_DATA_SHORT);
                    }
//                    delaySave = Constants.SAVA_DATA_SHORT;
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    //息屏广播
                    LogUtils.logd(TAG, "Screen off");
                    if (mPedometerBinder!=null){
                        mPedometerBinder.setDuration(Constants.SAVA_DATA_LONG);
                    }
                } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
                    //关机广播
                    saveData();
                    stopStepsCount();
                    LogUtils.logd(TAG, "Shut down");
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    //屏幕解锁
                    LogUtils.logd(TAG, "User unlocked phone");
                    updateUI();
                    if (mPedometerBinder!=null){
                        mPedometerBinder.setDuration(Constants.SAVA_DATA_SHORT);
                    }
                } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {
                    //日期变更
                    LogUtils.logd(TAG, "Date changed");
                    //新的一天更新数据
                    newDayUpdate();
                } else if (Intent.ACTION_TIME_CHANGED.equals(action)) {
                    LogUtils.logd(TAG, "Time changed");
                }
            }
        };
        registerReceiver(mReceiver,filter);

    }


    /**
     * 新的一天更新数据：
     * 查询数据库，判断是否已有当天日期的数据
     * -已有数据：更新数据
     * -没有数据：插入新记录，重置步数并更新UI;将service中的model设置为当前model
     */
    private void newDayUpdate() {
        String dateNow = TimeUtils.TimeToDate(System.currentTimeMillis());
        PedometerModel model = (PedometerModel) LitePal.where("mDate=?", dateNow).find(PedometerModel.class);
        if (model==null){
            model=new PedometerModel();
            model.setDate(dateNow);
            model.save();
            if (mPedometerBinder!=null){
                mPedometerBinder.newDayReset(model);
            }
        }else {
            updateUI();
        }
    }


    /**
     * 保存数据
     */
    private void saveData() {
        if (mPedometerBinder != null) {
            mPedometerBinder.saveData();
        }
    }

    /**
     * 更新步数、卡路里、距离
     */
    private void updateUI() {
        if (mPedometerBinder != null) {
            int steps = mPedometerBinder.getStepsCount();
            //更新步数
            mTvStepsCount.setText(String.valueOf(steps));
            LogUtils.logd(TAG, "更新步数" + steps);
            //更新卡路里
            mTvCalorie.setText(String.format("%.2f", mPedometerBinder.getCalorie()));
            //更新活动时间
            mTvTime.setText(TimeUtils.TimeToHMS(System.currentTimeMillis() - mPedometerBinder.getStartTimestamp()));
            LogUtils.logd(TAG, "更新时间" + (System.currentTimeMillis() - mPedometerBinder.getStartTimestamp()));
            //更新距离
            mTvDistance.setText(String.format("%.2f", mPedometerBinder.getDistance()));
            //更新进度
            mProgressBar.setProgress(calculateProgress());
            mTvCardDistance.setText(String.format("%.2f", mPedometerBinder.getDistance()));
            mTvCardCalorie.setText(String.format("%.2f", mPedometerBinder.getCalorie()));
        }
    }


    /**
     * 停止计步
     */
    private void stopStepsCount() {
        if (mPedometerBinder != null) {
            mPedometerBinder.stopStepsCount();
        }
    }

    /**
     * 开始计步,更新UI
     */
    private void startStepsCount() {
        if (mPedometerBinder != null) {
            mHandler.post(mRunnable);
        }

    }


    /**
     * 计算进度
     * @return 当前进度
     */
    private double calculateProgress(){
        double progress=0;
        if (mPedometerBinder!=null){
            int steps=mPedometerBinder.getStepsCount();
            if (steps<mTargetSteps){
                progress=steps*1.0/mTargetSteps*1.0;
                LogUtils.logd(TAG,"steps&mTargetSteps&progress "+steps+"&"+mTargetSteps+"&"+progress);
            }else {
                progress=1;
            }

        }

        return progress;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.logd(TAG, "service connected");
            mPedometerBinder = (PedometerService.PedometerBinder) service;
            startStepsCount();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


//    private String timeToHMS(long timeDelt) {
//        int hours = (int) (timeDelt / (1000 * 60 * 60));
//        int minutes = (int) (timeDelt % (1000 * 60 * 60)) / (1000 * 60);
//        long temp = (timeDelt % (1000 * 60 * 60)) % (1000 * 60);
//        int seconds = (int) (temp / 1000);
//        return String.format("%d:%d:%d", hours, minutes, seconds);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_setting) {
            Intent intent = SettingActivity.getIntent(MainActivity.this);
            startActivity(intent);
        }
        return true;
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //解绑服务
        if (mConnection != null) {
            unbindService(mConnection);
        }
        if (mHandler!=null){
            mHandler.removeCallbacks(mRunnable);
        }
        unregisterReceiver(mReceiver);

        ActivityCollector.removeActivity(this);
    }


}
