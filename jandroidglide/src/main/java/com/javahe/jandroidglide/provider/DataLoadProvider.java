package com.javahe.jandroidglide.provider;

/**
 * Created by zfc on 2018/1/2.
 */

import com.javahe.jandroidglide.load.Encoder;
import com.javahe.jandroidglide.load.ResourceDecoder;
import com.javahe.jandroidglide.load.ResourceEncoder;

import java.io.File;

/**
 * @param <T> 要被解码的类型
 * @param <Z> 解码为的类型
 */
public interface DataLoadProvider<T, Z> {

    /**
     * 获取解码图片资源的解码器
     *
     * @return
     */
    ResourceDecoder<File, Z> getCacheDecoder();

    ResourceDecoder<T, Z> getSourceDecoder();

    Encoder<T> getSourceEncoder();

    ResourceEncoder<Z> getEncoder();
}
