package com.scalpel.log;

import android.util.Log;

/**
 * Author: Mrchen on 15/12/26.
 */
public class KLog {
    private static final String TAG = "Scalpel_";
    public static String sProcessName;

    public static void output(String msg) {
        i(TAG+sProcessName, msg);
    }
    public static void output(Throwable throwable) {
        w(TAG+sProcessName, throwable);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void w(String tag, Throwable throwable) {
        Log.w(tag, throwable);
    }
}
