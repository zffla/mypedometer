package com.example.mypedometer.utils;

import android.content.Context;
import android.util.Log;

import com.example.mypedometer.BuildConfig;

/**
 * log工具类
 */
public class LogUtils {

//    是否处于debug状态
    public static boolean isDebug= BuildConfig.DEBUG;

    public static void logv(String tag,String msg){
        if (isDebug){
            Log.v(tag, "logv: "+msg);
        }

    }

    public static void logd(String tag,String msg){
        if (isDebug){
            Log.d(tag, "logd: "+msg);
        }
    }

    public static void logi(String tag,String msg){
        if (isDebug){
            Log.i(tag, "logi: "+msg);
        }
    }

    public static void logw(String tag,String msg,Exception e){
        if (isDebug){
            Log.w(tag, "logw: "+msg,e );
        }
    }

    public static void loge(String tag,String msg,Exception e){
        if (isDebug){
            Log.e(tag, "loge: "+msg,e );
        }
    }

}
