package com.javahe.jandroidglide;

import android.app.Activity;

import com.javahe.jandroidglide.manager.RequestManagerRetriever;

/**
 * Created by zfc on 2017/12/27.
 */

public class Glide {
    private final String TAG = "Glide";
    private static volatile Glide glide;


    public static RequestManager with(Activity activity) {
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(activity);
    }
}
