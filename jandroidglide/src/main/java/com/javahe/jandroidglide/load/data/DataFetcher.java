package com.javahe.jandroidglide.load.data;

/**
 * Created by zfc on 2018/1/2.
 */

import com.javahe.jandroidglide.Priority;

/**
 *
 * @param <T>要被加载的数据类型
 */
public interface DataFetcher<T> {
    T loadData(Priority priority) throws Exception;

    void cleanup();

    String getId();

    void canncel();
}
