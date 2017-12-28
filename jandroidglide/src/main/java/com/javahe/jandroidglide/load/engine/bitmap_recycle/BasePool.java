package com.javahe.jandroidglide.load.engine.bitmap_recycle;

import com.javahe.jandroidglide.util.Util;

import java.util.Queue;

/**
 * Created by zfc on 2017/12/28.
 *
 *  封装了一个队列，用来存储Poolable对象的
 */

public abstract class BasePool<T extends Poolable> {
    private static final int MAX_SIZE = 20;
    //创建了一个队列，缓存这20个对象
    private final Queue<T> keyPool = Util.createQueue(MAX_SIZE);

    protected T get() {
        T result = keyPool.poll();
        if (result == null) {
            result = create();
        }
        return result;
    }

    protected void offer(T key) {
        if (keyPool.size() < MAX_SIZE) {
            keyPool.offer(key);
        }
    }

    /**
     * 创建任何T的实例对象
     * @return
     */
    protected abstract T create();
}
