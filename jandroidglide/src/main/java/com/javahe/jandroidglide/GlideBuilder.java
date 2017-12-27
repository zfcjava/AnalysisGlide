package com.javahe.jandroidglide;

import android.content.Context;

/**
 * Created by zfc on 2017/12/28.
 */

public class GlideBuilder {

    private final Context context;

    public GlideBuilder(Context context) {
        //保存应用级别的Context
        this.context = context;
    }
}
