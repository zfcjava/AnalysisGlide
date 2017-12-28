package com.javahe.jandroidglide.load;

/**
 * Created by zfc on 2017/12/28.
 */

public enum  DecodeFormat {
    @Deprecated
    ALWAYS_ARGB_8888,
    PREFER_ARGB_8888,
    PREFER_ARGB_565;

    public static final DecodeFormat DEFAULT = PREFER_ARGB_565;
}
