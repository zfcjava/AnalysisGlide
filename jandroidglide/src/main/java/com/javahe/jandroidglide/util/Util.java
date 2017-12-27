package com.javahe.jandroidglide.util;

import android.os.Looper;

/**
 * Created by zfc on 2017/12/27.
 */

public class Util {

    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }

    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
