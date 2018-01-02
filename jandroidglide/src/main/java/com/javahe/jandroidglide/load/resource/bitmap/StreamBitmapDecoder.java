package com.javahe.jandroidglide.load.resource.bitmap;

import android.graphics.Bitmap;

import com.javahe.jandroidglide.load.DecodeFormat;
import com.javahe.jandroidglide.load.ResourceDecoder;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.BitmapPool;

import java.io.InputStream;


/**
 * Created by zfc on 2018/1/2.
 */

class StreamBitmapDecoder implements ResourceDecoder<InputStream, Bitmap> {

    public StreamBitmapDecoder(BitmapPool bitmapPool, DecodeFormat decodeFormat) {

    }
}
