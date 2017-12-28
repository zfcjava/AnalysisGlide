package com.javahe.jandroidglide;

import android.app.Activity;
import android.content.Context;

import com.javahe.jandroidglide.load.DecodeFormat;
import com.javahe.jandroidglide.load.engine.Engine;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.BitmapPool;
import com.javahe.jandroidglide.load.engine.bitmap_recycle.MemoryCache;
import com.javahe.jandroidglide.manager.RequestManagerRetriever;
import com.javahe.jandroidglide.load.module.GlideModule;
import com.javahe.jandroidglide.load.module.ManifestParser;

import java.util.Collections;
import java.util.List;

/**
 * Created by zfc on 2017/12/27.
 */

public class Glide {
    private final String TAG = "Glide";
    private static volatile Glide glide;
    private static boolean moduleEnable = true;

    //TODO 将配置信息放入到Glide当中
    public Glide(Engine engine, MemoryCache memoryCache, BitmapPool bitmapPool, Context context, DecodeFormat decodeFormat) {

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
}
