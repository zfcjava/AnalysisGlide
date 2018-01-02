package com.javahe.jandroidglide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zfc on 2017/12/28.
 */

public class LruBitmapPool implements BitmapPool{

    private static final String TAG = "LruBitmapPool";
    private final int initialMaxSize;
    private  int maxSize;
    private final LruPoolStrategy strategy;
    private final Set<Bitmap.Config> allowedConfigs;
    private final NullBitmapTracker tracker;
    private Set<Bitmap.Config> defaultAllowedConfig;
    private int currentSize;

    private long evictions;

    public LruBitmapPool(int maxSize, LruPoolStrategy strategy, Set<Bitmap.Config> allowedConfigs) {
        super();
        this.initialMaxSize = maxSize;
        this.maxSize = maxSize;
        this.strategy = strategy;
        this.allowedConfigs = allowedConfigs;
        this.tracker = new NullBitmapTracker();
    }

    public LruBitmapPool(int maxSize) {
        this(maxSize, getDefaultStrategy(), getDefaultAllowedConfig());
    }

    public LruBitmapPool(int maxSize, Set<Bitmap.Config> allowedConfigs) {
        this(maxSize, getDefaultStrategy(), allowedConfigs);
    }


    private static LruPoolStrategy getDefaultStrategy() {
        final LruPoolStrategy strategy;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            strategy = new SizeConfigStrategy();
        } else {
            strategy = new AttributeStrategy();
        }
        return strategy;
    }


    /**
     * 不同图片密度的只读信息
     * @return
     */
    public static Set<Bitmap.Config> getDefaultAllowedConfig() {
        Set<Bitmap.Config> configs = new HashSet<>();
        configs.addAll(Arrays.asList(Bitmap.Config.values()));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            configs.add(null);
        }
        return Collections.unmodifiableSet(configs);
    }

    @Override
    public int getSize() {
        return maxSize;
    }

    @Override
    /**
     * 大小倍增值
     */
    public void setSizeMultiplier(float sizeMultiplier) {
        this.maxSize = Math.round((float) this.initialMaxSize * sizeMultiplier);
        this.evict();
    }

    /**
     * 重新规划缓存
     */
    private void evict() {
        this.trimToSize(maxSize);
    }

    private void trimToSize(int maxSize) {
        //如果currentSzie大于maxSize，则需要回收一部分资源
        while (currentSize > maxSize) {
            final Bitmap removed = strategy.removeLast();
            if (removed == null) {
                //如果全部删除
                if (Log.isLoggable(TAG, Log.WARN)) {
                    dumpUnchecked();
                }
                return;
            }

            tracker.remove(removed);
            currentSize -= strategy.getSize(removed);
            removed.recycle();
            evictions++;

            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Evicting bitmap=" + strategy.logBitmap(removed));
            }
            dump();
        }
    }

    @Override
    public boolean put(Bitmap bitmap) {
        if (bitmap == null) {
            throw new RuntimeException("bitmap cannot be null");
        }
        //排除不合规的bitmap
        //mutable易变的，只有易变的，才能够更好的复用
        if (!bitmap.isMutable() || strategy.getSize(bitmap) > maxSize || !allowedConfigs.contains(bitmap.getConfig())) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Reject bitmap from pool"
                        + ", bitmap: " + strategy.logBitmap(bitmap)
                        + ", is mutable: " + bitmap.isMutable()
                        + ", is allowed config: " + allowedConfigs.contains(bitmap.getConfig()));
            }
            return false;
        }

        final int size = strategy.getSize(bitmap);
        currentSize += size;
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Put bitmap in pool=" + strategy.logBitmap(bitmap));
        }

        dump();
        evict();
        return false;
    }

    private void dump() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            dumpUnchecked();
        }
    }

    private void dumpUnchecked() {
//        Log.v(TAG, "Hits="  + hits
//                + ", misses=" + misses
//                + ", puts=" + puts
//                + ", evictions=" + evictions
//                + ", currentSize=" + currentSize
//                + ", maxSize=" + maxSize
//                + "\nStrategy=" + strategy);
    }


    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        return null;
    }

    @Override
    public Bitmap getDirty(int width, int height, Bitmap.Config config) {
        return null;
    }

    @Override
    public void clearMemory() {

    }

    @Override
    public void trimMemory(int level) {

    }

    private interface BitmapTracker {
        void add(Bitmap bitmap);
        void remove(Bitmap bitmap);
    }

    /**
     * 不做任何处理
     */
    private static class NullBitmapTracker implements BitmapTracker{

        @Override
        public void add(Bitmap bitmap) {

        }

        @Override
        public void remove(Bitmap bitmap) {

        }
    }

}
