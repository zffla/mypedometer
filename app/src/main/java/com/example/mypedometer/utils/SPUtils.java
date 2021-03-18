package com.example.mypedometer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mypedometer.constant.Constants;

public class SPUtils {



    private Context mContext;

    public SPUtils(Context context){
        mContext=context;
    }


    /**
     * 保存数据
     * @param key 键
     * @param value 值
     */
    public void setStringPrefs(String key,String value){
        SharedPreferences.Editor editor= getSharedPrefs().edit();
        editor.putString(key,value);
        editor.apply();
    }


    /**
     * 获取数据
     * @param key 键
     * @return 值
     */
    public String getStringPrefs(String key){
        SharedPreferences sp=getSharedPrefs();
        return sp.getString(key,"");
    }

    public void setFloatPrefs(String key,float value){
        SharedPreferences.Editor editor= getSharedPrefs().edit();
        editor.putFloat(key,value);
        editor.apply();
    }

    public float getFloatPrefs(String key){
        SharedPreferences sp=getSharedPrefs();
        return sp.getFloat(key,0.0f);
    }

    public void setIntPrefs(String key,int value){
        SharedPreferences.Editor editor= getSharedPrefs().edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public int getIntPrefs(String key){
        SharedPreferences sp=getSharedPrefs();
        return sp.getInt(key,0);
    }

    public void setBooleanPrefs(String key,boolean value){
        SharedPreferences.Editor editor= getSharedPrefs().edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public boolean getBooleanPrefs(String key){
        SharedPreferences sp=getSharedPrefs();
        return sp.getBoolean(key,false);
    }


    /**
     * 清除数据
     */
    public void clear(){
        SharedPreferences.Editor editor=getSharedPrefs().edit();
        editor.clear();
        editor.apply();
    }


    /**
     * 是否包含某个键值对
     * @param key 键
     * @return boolean
     */
    public boolean isContain(String key){
        SharedPreferences sp=getSharedPrefs();
        return sp.contains(key);
    }


    /**
     * 通过context获取sp
     * @return SharedPreferences
     */
    private SharedPreferences getSharedPrefs() {
        return mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }
}
