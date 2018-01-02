package com.javahe.jandroidglide.load.resource.bitmap;

import android.graphics.Bitmap;

import com.javahe.jandroidglide.load.DecodeFormat;
import com.javahe.jandroidglide.load.Encoder;
import com.javahe.jandroidglide.load.ResourceDecoder;
import com.javahe.jandroidglide.load.ResourceEncoder;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.BitmapPool;
import com.javahe.jandroidglide.provider.DataLoadProvider;

import java.io.File;
import java.io.InputStream;

/**
 * Created by zfc on 2018/1/2.
 *
 * 为流提供 编码，解码 缓存的功能
 */

public class StreamBitmapDataLoadProvider implements DataLoadProvider<InputStream, Bitmap> {

    private final StreamBitmapDecoder decoder;
    private final BitmapEncoder encoder;
    private final StreamEncoder sourceEncoder;
    private final FileToStreamDecoder<Bitmap> cacheDecoder;

    public StreamBitmapDataLoadProvider(BitmapPool bitmapPool, DecodeFormat decodeFormat) {
        this.sourceEncoder = new StreamEncoder();
        this.decoder = new StreamBitmapDecoder(bitmapPool, decodeFormat);
        this.encoder = new BitmapEncoder();
        this.cacheDecoder = new FileToStreamDecoder<Bitmap>(decoder);
    }

    @Override
    public ResourceDecoder<File, Bitmap> getCacheDecoder() {
        return cacheDecoder;
    }

    @Override
    public ResourceDecoder<InputStream, Bitmap> getSourceDecoder() {
        return decoder;
    }

    @Override
    public Encoder<InputStream> getSourceEncoder() {
        return sourceEncoder;
    }

    @Override
    public ResourceEncoder<Bitmap> getEncoder() {
        return encoder;
    }
}
