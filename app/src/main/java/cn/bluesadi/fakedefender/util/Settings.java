package cn.bluesadi.fakedefender.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.bluesadi.fakedefender.ui.base.BaseActivity;

public class Settings {

    public static final int LONG_PERIOD = 5;
    public static final int MIDDLE_PERIOD = 2;
    public static final int SHORT_PERIOD = 1;
    public static final int ONE_SECOND = 1000;

    public static final double HIGH_THRESHOLD = 0.7;
    public static final double MIDDLE_THRESHOLD = 0.5;
    public static final double LOW_THRESHOLD = 0.3;
    public static final double VERY_LOW_THRESHOLD = 0.15;

    public static final String SERVER_URL = "http://10.133.63.14:5000";
    public static int period = MIDDLE_PERIOD;
    public static double threshold = MIDDLE_THRESHOLD;
    public static double secondaryThreshold = LOW_THRESHOLD;

    private static SharedPreferences config;

    public static void init(Context context){
        config = context.getSharedPreferences("settings",  Context.MODE_PRIVATE);
        period = config.getInt("period", MIDDLE_PERIOD);
        threshold = config.getFloat("threshold", (float)MIDDLE_THRESHOLD);
        secondaryThreshold = config.getFloat("secondaryThreshold", (float)LOW_THRESHOLD);
    }

    public static void save(){
        SharedPreferences.Editor editor = config.edit();
        editor.putInt("period", period);
        editor.putFloat("threshold", (float)threshold);
        editor.putFloat("secondaryThreshold", (float)secondaryThreshold);
        editor.apply();
    }

    public static String getServerUrl(String service){
        return SERVER_URL + "/" + service;
    }

    public static String getString(int id){
        return BaseActivity.getTopActivity().getString(id);
    }

}
