package com.example.mypedometer.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 初始化view
     */
    public void initView(){}

    /**
     * 初始化数据，请求data或本地读取
     */
    public void initData(){}

    /**
     * 初始化监听器
     */
    public void initListener(){}
}
