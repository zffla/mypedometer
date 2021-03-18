package com.example.mypedometer.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypedometer.R;
import com.example.mypedometer.constant.Constants;
import com.example.mypedometer.utils.ActivityCollector;
import com.example.mypedometer.utils.SettingUtils;

public class SettingActivity extends BaseActivity {

    private Toolbar mToolbar;

    private ImageView mIvBack;
    private ListView mListSetting;

    private SettingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActivityCollector.addActivity(this);
        initView();
        initListener();
        initData();
    }


    @Override
    public void initView() {
        mToolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mIvBack = findViewById(R.id.iv_back);
        mListSetting = findViewById(R.id.list_setting);

    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void initData() {
        String[] listTitle = {"身高", "体重", "目标步数", "传感器敏感度", "传感器采样时间"};
        String[] listDesc = {"通过身高计算步长", "通过体重计算消耗的热量", "设定每日计划步数", "传感器感应数据的敏感程度", "每隔一定毫秒进行一次数据采样"};
        mAdapter = new SettingAdapter(listTitle, listDesc);
        mListSetting.setAdapter(mAdapter);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    /**
     * listview的adapter
     */
    class SettingAdapter extends BaseAdapter {

        private String[] mTitleList;
        private String[] mDescList;
        private SettingUtils mSettingUtils;

        SettingAdapter(String[] titleList, String[] descList) {
            mTitleList = titleList;
            mDescList = descList;
            mSettingUtils = new SettingUtils(SettingActivity.this);
        }

        @Override
        public int getCount() {
            return mTitleList.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleList[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SettingViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(SettingActivity.this).inflate(R.layout.list_item, parent, false);
                viewHolder = new SettingViewHolder();
                viewHolder.tvTitle = convertView.findViewById(R.id.tv_item_title);
                viewHolder.tvDesc = convertView.findViewById(R.id.tv_item_desc);
                viewHolder.tvValue = convertView.findViewById(R.id.tv_value);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (SettingViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(mTitleList[position]);
            viewHolder.tvDesc.setText(mDescList[position]);
            switch (position) {
                case 0:
                    float height = mSettingUtils.getBodyHeight();
                    viewHolder.tvValue.setText(String.format("%.1f cm", height));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setHeight();
                        }
                    });
                    break;
                case 1:
                    float weight = mSettingUtils.getBodyWeight();
                    viewHolder.tvValue.setText(String.format("%.1f kg", weight));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setWeight();
                        }
                    });
                    break;
                case 2:
                    int steps = mSettingUtils.getTargetSteps();
                    viewHolder.tvValue.setText(String.valueOf(steps));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setTargetSteps();
                        }
                    });
                    break;
                case 3:
                    double sensitivity = mSettingUtils.getSensitivity();
                    viewHolder.tvValue.setText(String.format("%.2f", sensitivity));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            setSensitivity();
                        }
                    });
                    break;
                case 4:
                    int interval = mSettingUtils.getInterval();
                    viewHolder.tvValue.setText(String.format("%d ms", interval));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setInterval();
                        }
                    });
            }
            return convertView;
        }


        /**
         * 设置时间间隔
         */
        private void setInterval() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivity.this);
            mBuilder.setItems(R.array.interval_array, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mSettingUtils.setInterval(Constants.INTERVAL_ARRAY[which]);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
            mBuilder.setTitle("设置采样时间间隔");
            mBuilder.create().show();
        }


        /**
         * 设置传感器敏感度
         */
        private void setSensitivity() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivity.this);
            mBuilder.setItems(R.array.sensitive_array, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mSettingUtils.setSensitivity(Constants.SENSITIVE_ARRAY[which]);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
            mBuilder.setTitle("设置传感器灵敏度");
            mBuilder.create().show();
        }


        /**
         * 设置体重
         */
        private void setWeight() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setTitle("设置体重");
            final View view = View.inflate(SettingActivity.this, R.layout.dialog_input, null);
            builder.setView(view);
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText input = view.findViewById(R.id.et_input);
                    String weightStr = input.getText().toString();
                    if (!weightStr.equals("")) {
                        float weight = Float.parseFloat(weightStr);
                        mSettingUtils.setBodyWeight(weight);
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(SettingActivity.this, "请输入正确的数值", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            builder.create().show();
        }


        /**
         * 设置身高
         */
        private void setHeight() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setTitle("设置身高");
            final View view = View.inflate(SettingActivity.this, R.layout.dialog_input, null);
            builder.setView(view);
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText input = view.findViewById(R.id.et_input);
                    String heightStr = input.getText().toString();
                    if (!heightStr.equals("")) {
                        float height = Float.parseFloat(heightStr);
                        mSettingUtils.setBodyHeight(height);
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(SettingActivity.this, "请输入正确的数值", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            builder.create().show();
        }

        /**
         * 设置目标步数
         */
        private void setTargetSteps() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setTitle("设置目标步数");
            final View view = View.inflate(SettingActivity.this, R.layout.dialog_input, null);
            builder.setView(view);
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText input = view.findViewById(R.id.et_input);
                    String target = input.getText().toString();
                    if (!target.equals("")) {
                        int targetSteps = Integer.parseInt(target);
                        mSettingUtils.setTargetSteps(targetSteps);
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(SettingActivity.this, "请输入正确的数值", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            builder.create().show();
        }

    }


    class SettingViewHolder {
        //子项标题
        TextView tvTitle;
        //各子项描述
        TextView tvDesc;
        //具体设置的值
        TextView tvValue;
    }
}
