package com.example.mypedometer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mypedometer.R;
import com.example.mypedometer.constant.Constants;
import com.example.mypedometer.utils.ChartDataUtils;
import com.example.mypedometer.utils.LogUtils;

import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ChartFragment extends Fragment {

    private TextView mTvNum;
    private TextView mTvUnit;
    private ColumnChartView mChartView;

    //标记当前数据是以周还是以月为周期
    private int mPeriod;
    //标记当前数据是步数还是卡路里
    private int mType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPeriod= getArguments() != null ? getArguments().getInt(Constants.DATA_PERIOD) : Constants.DATA_WEEK;
        mType=getArguments()!=null?getArguments().getInt(Constants.DATA_TYPE):Constants.DATA_STEPS;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chart,container,false);
        init(view);
        return view;
    }

    private void init(View view) {
        mTvNum=view.findViewById(R.id.tv_num);
        mTvUnit=view.findViewById(R.id.tv_unit);
        mChartView=view.findViewById(R.id.chart_show);
        ColumnChartData data=null;
        LogUtils.logd("ChartFragment","mtype"+mType);
        LogUtils.logd("ChartFragment","mperiod"+mPeriod);

        if (mType==Constants.DATA_STEPS){
            if (mPeriod==Constants.DATA_WEEK){
                //获取步数的每周数据
                data= ChartDataUtils.getStepsWeekData();
            }else {
                //获取步数的每月数据
                data=ChartDataUtils.getStepsMonthData();
            }
            mTvUnit.setText("步");
        }else {
            if (mPeriod==Constants.DATA_WEEK){
                //获取卡路里的每日数据
                data=ChartDataUtils.getCalorieWeekData();
            }else {
                //获取卡路里的每月数据
                data=ChartDataUtils.getCalorieMonthData();
            }
            mTvUnit.setText("千卡");
        }
        List<Integer> sportData=ChartDataUtils.getDataList(mPeriod,mType);
        mTvNum.setText(""+avgSteps(sportData));
        mChartView.setColumnChartData(data);
        //用来控制柱形图视图窗口的缩放
        Viewport viewport =new Viewport(0,  mChartView.getMaximumViewport().height()*1.25f, data.getColumns().size() > 7 ? 7 : data.getColumns().size(), 0);
        mChartView.setCurrentViewport(viewport);
        mChartView.setZoomEnabled(true);
        mChartView.setZoomType(ZoomType.HORIZONTAL);
//        mChartView.moveTo(0, 0);
    }


    /**
     * 获取fragment实例
     * @param period 周期标记
     * @param type 数据类型标记
     * @return instance
     */
    public static ChartFragment newInstance(int period,int type) {
        Bundle args = new Bundle();
        args.putInt(Constants.DATA_PERIOD,period);
        args.putInt(Constants.DATA_TYPE,type);
        ChartFragment fragment = new ChartFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * 计算平均数据
     */
    private int avgSteps(List<Integer> data){
        int sum=0;
        for (int num:data){
            sum+=num;
        }
        return sum/data.size();
    }

}
