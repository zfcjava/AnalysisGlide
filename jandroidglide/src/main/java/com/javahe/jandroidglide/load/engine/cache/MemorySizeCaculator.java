package com.javahe.jandroidglide.load.engine.cache;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by zfc on 2017/12/28.
 */

public class MemorySizeCaculator {

    private static final String TAG = "MemorySizeCaculator";

    private int memoryCacheSize;
    private int bitmapPoolSize;
    private Context context;

    //设置当前应用占用应用栈的比例
    static final float MAX_SIZE_MUTIPLIER = 0.4f;
    static final float LOW_MEMORY_MAX_SIZE_MUTIPLIER = 0.33f;
    //每一个像素占用4BYTE
    static final int BYTES_PER_ARGB_8888_PIXEL = 4;
    static final int BITMAP_POOL_TARGET_SCREEN = 4;
    static final int MEMORY_CACHE_TARGET_SCREEN = 2;

    public MemorySizeCaculator(Context context) {
        this(context, (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE),
                new DisplayMetricsScreenDimensions(context.getResources().getDisplayMetrics()));

    }

    public MemorySizeCaculator(Context context, ActivityManager amService, DisplayMetricsScreenDimensions screenDimensions) {
        this.context = context;
        //定义中最大的可用手机内存
        final int maxSize = getMaxSize(amService);
        //获取一张图片的大小
        final int screenSize = screenDimensions.getHeightPixels() * screenDimensions.getWidthPixels() * BYTES_PER_ARGB_8888_PIXEL;
        int targetMemoryCacheSize = screenSize * MEMORY_CACHE_TARGET_SCREEN;
        int targetPoolSize = screenSize * BITMAP_POOL_TARGET_SCREEN;

        if (targetMemoryCacheSize + targetPoolSize < maxSize) {
            this.memoryCacheSize = targetMemoryCacheSize;
            this.bitmapPoolSize = targetPoolSize;
        } else {
            float part = Math.round((float) maxSize / (BITMAP_POOL_TARGET_SCREEN + MEMORY_CACHE_TARGET_SCREEN));
            this.memoryCacheSize = (int) (part * targetMemoryCacheSize);
            this.bitmapPoolSize = (int) (part * targetPoolSize);
        }

        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Calculated memory cache size: " + toMb(memoryCacheSize) + " pool size: " + toMb(bitmapPoolSize)
                    + " memory class limited? " + (targetMemoryCacheSize + targetPoolSize > maxSize) + " max size: "
                    + toMb(maxSize) + " memoryClass: " + amService.getMemoryClass() + " isLowMemoryDevice: "
                    + isLowMemoryDevice(amService));
        }

    }

    private String toMb(int bytes) {
        return Formatter.formatFileSize(context, bytes);
    }

    /**
     * android:largeHeap=“true”    amService.getLargeMemoryClass()
     * @param amService
     * @return
     */
    private int getMaxSize(ActivityManager amService) {
        final int memoryClassBytes = amService.getLargeMemoryClass() * 1024 * 1024;
        final boolean isLowMemoryDevice = isLowMemoryDevice(amService);
        return Math.round(memoryClassBytes * (isLowMemoryDevice ? LOW_MEMORY_MAX_SIZE_MUTIPLIER : MAX_SIZE_MUTIPLIER));
    }

    /**
     * 小于11的肯定是低内存设备；
     * 低内存设备为512MB
     * @param amService
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isLowMemoryDevice(ActivityManager amService) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return amService.isLowRamDevice();
        } else {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB;
        }
    }

    public int getBitmapSize() {
        return bitmapPoolSize;
    }

    public int getMemoryCacheSize() {
        return memoryCacheSize;
    }

    interface ScreenDimensions{
        int getWidthPixels();

        int getHeightPixels();
    }


    /**
     * DisplayMetric给该类注入灵魂
     */
    private static class DisplayMetricsScreenDimensions implements ScreenDimensions{

        private final DisplayMetrics displayMetrics;

        public DisplayMetricsScreenDimensions(DisplayMetrics displayMetrics) {
            this.displayMetrics = displayMetrics;
        }


        @Override
        public int getWidthPixels() {
            return displayMetrics.widthPixels;
        }

        @Override
        public int getHeightPixels() {
            return displayMetrics.heightPixels;
        }

    }
}
