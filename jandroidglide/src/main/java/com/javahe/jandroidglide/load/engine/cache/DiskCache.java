package com.javahe.jandroidglide.load.engine.cache;

import com.javahe.jandroidglide.Key;

import java.io.File;

/**
 * Created by zfc on 2017/12/28.
 */

public interface DiskCache {
    interface Factory{
        int DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024;
        String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";

        /**
         * 懒加载的方式创建DiskCache对象
         * @return
         */
        DiskCache build();
    }

    interface Writer{

        boolean write(File file);
    }

    File get(Key key);

    void put(Key key, Writer writer);

    void clear();
}
