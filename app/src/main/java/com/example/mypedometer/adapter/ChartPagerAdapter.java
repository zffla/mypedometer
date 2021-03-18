package com.example.mypedometer.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mypedometer.fragment.ChartFragment;

import java.util.List;

public class ChartPagerAdapter extends FragmentStateAdapter {

    private List<ChartFragment> mFragmentList;
    private List<String> mTitleList;

    public ChartPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,List<ChartFragment> fragmentList,List<String> titleList) {
        super(fragmentManager, lifecycle);
        mFragmentList=fragmentList;
        mTitleList=titleList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return mTitleList.size();
    }


}
