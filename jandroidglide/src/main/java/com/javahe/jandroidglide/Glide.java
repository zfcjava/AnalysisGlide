package com.javahe.jandroidglide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.javahe.jandroidglide.load.DecodeFormat;
import com.javahe.jandroidglide.load.StreamFileLoader;
import com.javahe.jandroidglide.load.engine.Engine;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.BitmapPool;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.MemoryCache;
import com.javahe.jandroidglide.load.engine.prefill.BitmapPreFiller;
import com.javahe.jandroidglide.load.model.GenericLoaderFactory;
import com.javahe.jandroidglide.load.model.ModelLoaderFactory;
import com.javahe.jandroidglide.load.resource.bitmap.StreamBitmapDataLoadProvider;
import com.javahe.jandroidglide.manager.RequestManagerRetriever;
import com.javahe.jandroidglide.load.module.GlideModule;
import com.javahe.jandroidglide.load.module.ManifestParser;
import com.javahe.jandroidglide.provider.DataLoadProviderRepositry;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by zfc on 2017/12/27.
 */

public class Glide {
    private final String TAG = "Glide";
    private static volatile Glide glide;
    private static boolean moduleEnable = true;
    private final Engine engine;
    private final MemoryCache memoryCache;
    private final BitmapPool bitmapPool;
    private final DecodeFormat decodeFormat;
    /**
     * 这是一个工厂集合
     */
    private final GenericLoaderFactory loaderFactory;
    private final Handler mainHandler;
    private final BitmapPreFiller bitmapPreFiller;
    private final DataLoadProviderRepositry dataLoadProviderRepositry;

    //TODO 将配置信息放入到Glide当中
    public Glide(Engine engine, MemoryCache memoryCache, BitmapPool bitmapPool, Context context, DecodeFormat decodeFormat) {
        this.engine = engine;
        this.memoryCache = memoryCache;
        this.bitmapPool = bitmapPool;
        this.decodeFormat = decodeFormat;
        loaderFactory = new GenericLoaderFactory(context);
        mainHandler = new Handler(Looper.getMainLooper());
        bitmapPreFiller = new BitmapPreFiller(memoryCache, bitmapPool, decodeFormat);
        dataLoadProviderRepositry = new DataLoadProviderRepositry();

        StreamBitmapDataLoadProvider streamBitmapLoadProvider = new StreamBitmapDataLoadProvider(bitmapPool, decodeFormat);
        dataLoadProviderRepositry.register(InputStream.class, Bitmap.class, streamBitmapLoadProvider);

        register(File.class, InputStream.class, new StreamFileLoader.Factory());
    }

    public static RequestManager with(Activity activity) {
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(activity);
    }

    /**
     * 创建单例
     * @param context
     */
    public static Glide get(Context context) {
        if (null == glide) {
            Class var1 = Glide.class;
            synchronized (Glide.class) {
                if (null == glide) {
                    //获取应用级别的上下文
                    Context applicationContext = context.getApplicationContext();
                    GlideBuilder builder = new GlideBuilder(applicationContext);

                    List<GlideModule> modules = parseGlideModules(applicationContext);

                    for (GlideModule module : modules) {
                        module.applyOptions(applicationContext, builder);
                    }
                    glide = builder.createGlide();

                    for (GlideModule module : modules) {
                        module.registComponents(applicationContext, glide);
                    }
                }
            }
        }
        return glide;
    }

    private static List<GlideModule> parseGlideModules(Context context) {

        return moduleEnable ? (new ManifestParser(context).parse()) : Collections.<GlideModule>emptyList();
    }

    public <T, Y> void register(Class<T> modelClass, Class<Y> resourceClass, ModelLoaderFactory<T, Y> factory) {
        ModelLoaderFactory<T, Y> removed = loaderFactory.register(modelClass, resourceClass, factory);
        if (removed != null) {
            removed.teardown();
        }
    }
}
