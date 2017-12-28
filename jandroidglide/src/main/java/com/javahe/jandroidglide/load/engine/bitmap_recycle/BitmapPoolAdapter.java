package com.javahe.jandroidglide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

/**
 * Created by zfc on 2017/12/28.
 */

public class BitmapPoolAdapter implements BitmapPool {
    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void setSizeMultiplier(float sizeMultiplier) {

    }

    @Override
    public boolean put(Bitmap bitmap) {
        return false;
    }

    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        return null;
    }

    @Override
    public Bitmap getDirty(int width, int height, Bitmap.Config config) {
        return null;
    }

    @Override
    public void clearMemory() {

    }

    @Override
    public void trimMemory(int level) {

    }
}
