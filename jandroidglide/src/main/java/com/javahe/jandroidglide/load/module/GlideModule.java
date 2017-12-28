package com.javahe.jandroidglide.load.module;

import android.content.Context;

import com.javahe.jandroidglide.Glide;
import com.javahe.jandroidglide.GlideBuilder;

/**
 * Created by zfc on 2017/12/28.
 *  将GlideBuilder内容bind到Glide中
 *
 */

public interface GlideModule {
    void applyOptions(Context context, GlideBuilder glideBuilder);

    void registComponents(Context context, Glide glide);
}
