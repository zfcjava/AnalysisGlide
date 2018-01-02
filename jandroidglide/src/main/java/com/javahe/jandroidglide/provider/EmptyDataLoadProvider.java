package com.javahe.jandroidglide.provider;

import com.javahe.jandroidglide.load.Encoder;
import com.javahe.jandroidglide.load.ResourceDecoder;
import com.javahe.jandroidglide.load.ResourceEncoder;

import java.io.File;

/**
 * Created by zfc on 2018/1/2.
 */

public class EmptyDataLoadProvider<T,Z> implements DataLoadProvider<T,Z> {

    private static final DataLoadProvider<?, ?> EMPTY_DATA_LOAD_PROVIDER = new EmptyDataLoadProvider<Object, Object>();

    @SuppressWarnings("unchecked")
    public static <T, Z> DataLoadProvider<T, Z> get() {
        return (DataLoadProvider<T, Z>) EMPTY_DATA_LOAD_PROVIDER;
    }


    @Override
    public ResourceDecoder<File, Z> getCacheDecoder() {
        return null;
    }

    @Override
    public ResourceDecoder<T, Z> getSourceDecoder() {
        return null;
    }

    @Override
    public Encoder<T> getSourceEncoder() {
        return null;
    }

    @Override
    public ResourceEncoder<Z> getEncoder() {
        return null;
    }
}
