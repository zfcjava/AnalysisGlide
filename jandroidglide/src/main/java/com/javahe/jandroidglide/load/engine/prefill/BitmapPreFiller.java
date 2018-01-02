package com.javahe.jandroidglide.load.engine.prefill;

import com.javahe.jandroidglide.load.DecodeFormat;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.BitmapPool;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.MemoryCache;

/**
 * Created by zfc on 2018/1/2.
 */

public class BitmapPreFiller {

    private final MemoryCache memoryCache;
    private final BitmapPool bitmapPool;
    private final DecodeFormat decodeFormat;

    public BitmapPreFiller(MemoryCache memoryCache, BitmapPool bitmapPool, DecodeFormat decodeFormat) {
        this.memoryCache = memoryCache;
        this.bitmapPool = bitmapPool;
        this.decodeFormat = decodeFormat;
    }
}
