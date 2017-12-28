package com.javahe.jandroidglide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

/**
 * Created by zfc on 2017/12/28.
 */

interface LruPoolStrategy {

    void put(Bitmap bitmap);

    Bitmap get(int width, int height, Bitmap.Config config);

    Bitmap removeLast();

    String logBitmap(Bitmap bitmap);

    String logBitmap(int width, int height, Bitmap.Config config);

    /**
     * 为什么没有做成抽象呢？
     * @param bitmap
     * @return
     */
    int getSize(Bitmap bitmap);

}
