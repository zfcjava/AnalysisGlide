package com.javahe.jandroidglide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;

import com.javahe.jandroidglide.Key;

/**
 * Created by zfc on 2017/12/28.
 */

class AttributeStrategy implements LruPoolStrategy {
    private final KeyPool keyPool = new KeyPool();
    private final GroupedLinkedHashMap<Key, Bitmap> groupedMap = new GroupedLinkedHashMap();

    @Override
    public void put(Bitmap bitmap) {

    }

    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        return null;
    }

    @Override
    public Bitmap removeLast() {
        return null;
    }

    @Override
    public String logBitmap(Bitmap bitmap) {
        return null;
    }

    @Override
    public String logBitmap(int width, int height, Bitmap.Config config) {
        return null;
    }

    @Override
    public int getSize(Bitmap bitmap) {
        return 0;
    }


    static class KeyPool extends BasePool<Key> {
        public Key get(int width, int height, Bitmap.Config config) {
            //没有，就创建新的
            Key result = get();
            result.init(width, height, config);
            return result;
        }

        @Override
        protected Key create() {
            return new Key(this);
        }
    }

    /**
     * 为什么存储缓存bitmap 配置
     */
    static class Key implements Poolable {
        private final KeyPool keyPool;
        private int width;
        private int height;
        private Bitmap.Config config;

        public Key(KeyPool keyPool) {
            this.keyPool = keyPool;
        }

        @Override
        public void offer() {

        }

        public  void init(int width, int height, Bitmap.Config config) {
            this.width = width;
            this.height = height;
            this.config = config;
        }

        @Override
        public String toString() {
            return AttributeStrategy.getBitmapString(width, height, config);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Key) {
                Key others = (Key) obj;
                return others.width == width && others.height == height && others.config.equals(config);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = width;
            result = 31 * result + height;
            result = 31 * result + (config == null ? 0 : config.hashCode());
            return result;
        }
    }

    private static String getBitmapString(int width, int height, Bitmap.Config config) {
        return "[" + width + "X" + height + "]," + config;
    }
}

