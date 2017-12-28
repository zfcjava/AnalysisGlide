package com.javahe.jandroidglide.util;

import android.os.Looper;

import com.javahe.jandroidglide.load.engine.bitmap_recycle.Poolable;

import java.util.ArrayDeque;
import java.util.Queue;

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

    public static <T> Queue<T> createQueue(int maxSize) {
        return new ArrayDeque<>();
    }
}
