package com.javahe.jandroidglide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

/**
 * Created by zfc on 2017/12/28.
 */

public interface BitmapPool {
    int getSize();

    void setSizeMultiplier(float sizeMultiplier);

    boolean put(Bitmap bitmap);

    Bitmap get(int width, int height, Bitmap.Config config);

    Bitmap getDirty(int width, int height, Bitmap.Config config);

    void clearMemory();

    void trimMemory(int level);
}
