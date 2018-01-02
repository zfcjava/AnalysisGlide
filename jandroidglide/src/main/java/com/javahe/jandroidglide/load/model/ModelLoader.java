package com.javahe.jandroidglide.load.model;

/**
 * Created by zfc on 2018/1/2.
 */

import com.javahe.jandroidglide.load.data.DataFetcher;

/**
 *
 * @param <T>module类型
 * @param <Y>用于解码资源的类型
 */
public interface ModelLoader<T, Y> {


    DataFetcher<Y> getResourceFetcher(T model, int width, int height);
}
