package com.javahe.jandroidglide;

import java.nio.charset.UnsupportedCharsetException;
import java.security.MessageDigest;

/**
 * Created by zfc on 2017/12/28.
 */

public interface Key {
    String STRING_CHAR_SET_NAME = "UTF-8";

    /**
     * 以摘要当做Key
     * @param messageDigest
     * @throws UnsupportedCharsetException
     */
    void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedCharsetException;

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
