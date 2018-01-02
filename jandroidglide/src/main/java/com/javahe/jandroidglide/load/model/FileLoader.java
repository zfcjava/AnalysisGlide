package com.javahe.jandroidglide.load.model;

import android.graphics.YuvImage;
import android.net.Uri;

import com.javahe.jandroidglide.load.data.DataFetcher;

import java.io.File;

/**
 * Created by zfc on 2018/1/2.
 */

public class FileLoader<T> implements ModelLoader<File, T> {

    private final ModelLoader<Uri, T> uriLoader;

    public FileLoader(ModelLoader<Uri, T> uriLoader) {
        this.uriLoader = uriLoader;
    }

    @Override
    public DataFetcher<T> getResourceFetcher(File model, int width, int height) {
        return uriLoader.getResourceFetcher(Uri.fromFile(model), width, height);
    }
}
