package com.javahe.jandroidglide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

/**
 * Created by zfc on 2017/12/28.
 * 两种内存缓存策略
 */

class SizeConfigStrategy implements LruPoolStrategy {
    @Override
    public void put(Bitmap bitmap) {

    }

    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        return null;
    }

    @Override
    public Bitmap removeLast() {
        return null;
    }

    @Override
    public String logBitmap(Bitmap bitmap) {
        return null;
    }

    @Override
    public String logBitmap(int width, int height, Bitmap.Config config) {
        return null;
    }

    @Override
    public int getSize(Bitmap bitmap) {
        return 0;
    }
}
