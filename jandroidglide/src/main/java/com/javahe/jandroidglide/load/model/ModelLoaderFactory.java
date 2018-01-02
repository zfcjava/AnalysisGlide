package com.javahe.jandroidglide.load.model;

import android.content.Context;

/**
 * Created by zfc on 2018/1/2.
 */

public interface ModelLoaderFactory<T, Y> {
    ModelLoader<T, Y> build(Context context, GenericLoaderFactory factories);

    void teardown();
}
