package com.example.sanjay.sanjaysystemtest.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * Created by Sanjay on 3/6/2018.
 */
public class AppPreference {
    private static final String PREFERENCE_NAME = "MyPreference";
    private static final String KEY_SYNC_TIME = "syn_time";
    private static final String KEY_FIRST_RUN = "is_first_run";

    public static void setSynTime(Context context, long lastSyncAt) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_SYNC_TIME, lastSyncAt);
        editor.commit();
    }

    public static long getSynTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(KEY_SYNC_TIME, 0L);
    }

    public static void setFirstRun(Context context, boolean isFirstRun) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_FIRST_RUN, isFirstRun);
        editor.commit();
    }

    public static boolean isFirstRun(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_FIRST_RUN, true);
    }
}
