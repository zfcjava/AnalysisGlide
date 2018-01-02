package com.javahe.jandroidglide;

import android.content.Context;
import android.os.Build;

import com.javahe.jandroidglide.load.DecodeFormat;
import com.javahe.jandroidglide.load.engine.Engine;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.BitmapPool;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.LruBitmapPool;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.MemoryCache;
import com.javahe.jandroidglide.load.engine.cache.DiskCache;
import com.javahe.jandroidglide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.javahe.jandroidglide.load.engine.cache.LruResourceCache;
import com.javahe.jandroidglide.load.engine.cache.MemorySizeCaculator;
import com.javahe.jandroidglide.load.engine.executor.FifoPriorityThreadPoolExecutor;

import java.util.concurrent.ExecutorService;

/**
 * Created by zfc on 2017/12/28.
 *
 * 利用Buidler设计模式丰富Glide的功能属性
 *
 */

public class GlideBuilder {

    private final Context context;
    private Engine engine;
    private BitmapPool bitmapPool;
    private MemoryCache memoryCache;
    private ExecutorService sourceService;
    private ExecutorService diskCacheService;
    private DecodeFormat decodeFormat;
    private DiskCache.Factory diskCacheFacotory;


    public GlideBuilder(Context context) {
        //保存应用级别的Context
        this.context = context;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setBitmapPool(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    public void setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
    }

    public void setSourceService(ExecutorService sourceService) {
        this.sourceService = sourceService;
    }

    public void setDiskCacheService(ExecutorService diskCacheService) {
        this.diskCacheService = diskCacheService;
    }

    public void setDecodeFormat(DecodeFormat decodeFormat) {
        this.decodeFormat = decodeFormat;
    }

    public void setDiskCacheFacotory(DiskCache.Factory diskCacheFacotory) {
        this.diskCacheFacotory = diskCacheFacotory;
    }

    public Glide createGlide() {
        if (sourceService == null) {
            final int cores = Math.max(1, Runtime.getRuntime().availableProcessors());
            sourceService = new FifoPriorityThreadPoolExecutor(cores);
        }

        if (diskCacheService == null) {
            diskCacheService = new FifoPriorityThreadPoolExecutor(1);
        }

        //创建Glide对象之前，根据配置信息设置缓存大小
        MemorySizeCaculator caculator = new MemorySizeCaculator(context);

        //1.图片缓存
        if (bitmapPool == null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                int size = caculator.getBitmapSize();
                bitmapPool = new LruBitmapPool(size);
            } else {
                //低于版本11的不做缓存处理（面向接口编程，适配器模式还是很常见的）
                bitmapPool = new BitmapPoolAdapter();
            }
        }

        //2.除了图片之外的缓存
        if (memoryCache == null) {
            memoryCache = new LruResourceCache(caculator.getMemoryCacheSize());
        }

        //3.磁盘缓存
        if (diskCacheFacotory == null) {
            diskCacheFacotory = new InternalCacheDiskCacheFactory(context);
        }

        if (engine == null) {
            engine = new Engine(memoryCache, diskCacheFacotory, diskCacheService, sourceService);
        }

        //设置默认画质
        if (decodeFormat == null) {
            //默认图片画质565
            decodeFormat = DecodeFormat.DEFAULT;
        }

        return new Glide(engine, memoryCache, bitmapPool, context, decodeFormat);
    }
}
