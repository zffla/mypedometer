package com.example.mypedometer.utils;

import android.content.Intent;
import android.graphics.Color;

import com.example.mypedometer.constant.Constants;
import com.example.mypedometer.model.PedometerModel;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;

public class ChartDataUtils {


    /**
     * 获取近七天步数预览数据
     */
    public static ColumnChartData getRecentStepsData() {
        int numSubcolumns = 1;
        //从数据库查询数据
        List<PedometerModel> models= getWeekData();
        int numColumns = models.size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) models.get(numColumns-1-i).getStepCount(), Color.parseColor("#B0C4DE")));
            }

            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);
        data.setAxisXBottom(null);
        data.setAxisYLeft(null);
        return data;
    }


    /**
     * 获取近一周步数数据
     */
    public static ColumnChartData getStepsWeekData() {
        int numSubcolumns = 1;
        //从数据库查询数据
        List<PedometerModel> models= getWeekData();
        int numColumns = models.size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        List<String> axisXLabel=new ArrayList<>();
        List<Integer> axisYLabel=new ArrayList<>();
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) models.get(numColumns-1-i).getStepCount(), Color.parseColor("#B0C4DE")));
            }
            String date=models.get(numColumns-1-i).getDate();
            axisXLabel.add(date.substring(date.indexOf("-")+1));
            axisYLabel.add(models.get(numColumns-1-i).getStepCount());
            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);
        Axis axisX = getAxisX(axisXLabel);
        int max=Collections.max(axisYLabel);
        int min=Collections.min(axisYLabel);
        axisYLabel.clear();
        axisYLabel.add(min);
        axisYLabel.add(min+(max-min)/3);
        axisYLabel.add(min+(max-min)*2/3);
        axisYLabel.add(max);
        Axis axisY= getAxisY(axisYLabel);
        axisY.setHasLines(true);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        return data;
    }


    /**
     * 获取近一月步数数据
     */
    public static ColumnChartData getStepsMonthData() {
        int numSubcolumns = 1;
        //从数据库查询数据
        List<PedometerModel> models= getMonthData();
        int numColumns = models.size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        List<String> axisXLabel=new ArrayList<>();
        List<Integer> axisYLabel=new ArrayList<>();
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) models.get(numColumns-1-i).getStepCount(), Color.parseColor("#B0C4DE")));
            }
            String date=models.get(numColumns-1-i).getDate();
            axisXLabel.add(date.substring(date.indexOf("-")+1));
            axisYLabel.add(models.get(numColumns-1-i).getStepCount());
            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);
        Axis axisX = getAxisX(axisXLabel);
        int max=Collections.max(axisYLabel);
        int min=Collections.min(axisYLabel);
        axisYLabel.clear();
        axisYLabel.add(min);
        axisYLabel.add(min+(max-min)/3);
        axisYLabel.add(min+(max-min)*2/3);
        axisYLabel.add(max);
        Axis axisY= getAxisY(axisYLabel);
        axisY.setHasLines(true);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        return data;
    }


    /**
     * 获取近七天卡路里预览数据
     */
    public static ColumnChartData getRecentCalorieData() {
        int numSubcolumns = 1;

        //从数据库查询数据
        List<PedometerModel> models= getWeekData();
        int numColumns = models.size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) models.get(numColumns-1-i).getCalorie(), Color.parseColor("#B0C4DE")));

            }

            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);

        data.setAxisXBottom(null);
        data.setAxisYLeft(null);
        return data;
    }

    /**
     * 获取近一周卡路里数据
     */
    public static ColumnChartData getCalorieWeekData() {
        int numSubcolumns = 1;
        //从数据库查询数据
        List<PedometerModel> models= getWeekData();
        int numColumns = models.size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        List<String> axisXLabel=new ArrayList<>();
        List<Float> axisYLabel=new ArrayList<>();
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue( models.get(numColumns-1-i).getCalorie(), Color.parseColor("#B0C4DE")));
            }
            String date=models.get(numColumns-1-i).getDate();
            axisXLabel.add(date.substring(date.indexOf("-")+1));
            axisYLabel.add(models.get(numColumns-1-i).getCalorie());
            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);
        Axis axisX = getAxisX(axisXLabel);
        int max= Math.round(Collections.max(axisYLabel));
        int min=Math.round(Collections.min(axisYLabel));
        List<Integer> labels=new ArrayList<>();
        labels.add(min);
        labels.add(min+(max-min)/3);
        labels.add(min+(max-min)*2/3);
        labels.add(max);
        Axis axisY= getAxisY(labels);
        axisY.setHasLines(true);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        return data;
    }

    /**
     * 获取近一月卡路里数据
     */
    public static ColumnChartData getCalorieMonthData() {
        int numSubcolumns = 1;
        //从数据库查询数据
        List<PedometerModel> models= getMonthData();
        int numColumns = models.size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        List<String> axisXLabel=new ArrayList<>();
        List<Float> axisYLabel=new ArrayList<>();
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue( models.get(numColumns-1-i).getCalorie(), Color.parseColor("#B0C4DE")));
            }
            String date=models.get(numColumns-1-i).getDate();
            axisXLabel.add(date.substring(date.indexOf("-")+1));
            axisYLabel.add(models.get(numColumns-1-i).getCalorie());
            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);
        Axis axisX = getAxisX(axisXLabel);
        int max= Math.round(Collections.max(axisYLabel));
        int min=Math.round(Collections.min(axisYLabel));
        List<Integer> labels=new ArrayList<>();
        labels.add(min);
        labels.add(min+(max-min)/3);
        labels.add(min+(max-min)*2/3);
        labels.add(max);
        Axis axisY= getAxisY(labels);
        axisY.setHasLines(true);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        return data;
    }


    /**
     * 设置X轴
     * @param labels X轴标签
     * @return X轴
     */
    private static Axis getAxisX(List<String> labels){

        Axis axis = new Axis();//坐标轴
        axis.setHasTiltedLabels(false);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
        axis.setTextColor(Color.GRAY);//设置字体颜色

        axis.setTextSize(10);//设置字体大小

        List<AxisValue> values = new ArrayList<>();
        for (int i = 0; i < labels.size(); i++) {
            values.add(new AxisValue(i).setLabel(labels.get(i)));
        }
        axis.setValues(values);  //填充坐标轴的坐标名称
        return axis;

    }

    /**
     * 设置Y轴
     * @param labels Y轴标签
     * @return Y轴
     */
    private static Axis getAxisY(List<Integer> labels){
        Axis axisY=new Axis();
        List<AxisValue> values = new ArrayList<>();
        for (Integer label:labels) {
            AxisValue value = new AxisValue(label);
            values.add(value);
        }
        axisY.setTextSize(10);//设置字体大小
        axisY.setMaxLabelChars(6);
        axisY.setValues(values);
        return axisY;
    }


    /**
     * 获取周数据
     */
    public static List<PedometerModel> getWeekData(){
        //从数据库查询数据
        return LitePal.limit(7).order("mStartTime desc").find(PedometerModel.class);
    }

    /**
     * 获取月数据
     */
    public static List<PedometerModel> getMonthData(){
        String date=TimeUtils.TimeToDate(System.currentTimeMillis());
        String month=date.substring(0, date.indexOf("-",date.indexOf("-")+1 ));
        return LitePal.where("mDate like ?",month+"%").order("mStartTime desc").find(PedometerModel.class);

    }

    /**
     * 返回运动数据list
     */
    public static List<Integer> getDataList(int period,int type){
        List<PedometerModel> models;
        List<Integer> data=new ArrayList<>();
        if (period== Constants.DATA_WEEK){
            models=getWeekData();
        }else {
            models=getMonthData();
        }
        if (type==Constants.DATA_STEPS){
            for (PedometerModel model:models){
                data.add(model.getStepCount());
            }
        }else{
            for (PedometerModel model:models){
                data.add(Math.round(model.getCalorie()));
            }
        }
        return data;
    }

}
