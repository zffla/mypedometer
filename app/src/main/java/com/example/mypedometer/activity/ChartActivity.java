package com.example.mypedometer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mypedometer.R;
import com.example.mypedometer.constant.Constants;
import com.example.mypedometer.fragment.ChartFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends BaseActivity {

    private int type;
    private Toolbar mToolbar;

    private TabLayout mTabLayout;

    private ViewPager2 mPager;

    private List<String> mTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Intent intent = getIntent();
        type = intent.getIntExtra(Constants.DATA_TYPE, 0);
        initView();
        initData();

        initListener();
    }

    @Override
    public void initView() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.left_arrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (type == Constants.DATA_STEPS) {
            mToolbar.setTitle("步数");
        } else {
            mToolbar.setTitle("消耗");
        }
        mTabLayout = findViewById(R.id.tab_chart);

        mPager = findViewById(R.id.vp_chart);

    }

    @Override
    public void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return ChartFragment.newInstance(position,type);
            }

            @Override
            public int getItemCount() {
                return 2;
            }


        });

        new TabLayoutMediator(mTabLayout, mPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTitleList.get(position));
            }
        }).attach();
    }

    @Override
    public void initData() {
        mTitleList = new ArrayList<>();
        mTitleList.add("周");
        mTitleList.add("月");
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, ChartActivity.class);
    }
}
