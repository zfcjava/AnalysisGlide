package com.javahe.jandroidglide.load;

import android.content.Context;
import android.net.Uri;

import com.javahe.jandroidglide.Glide;
import com.javahe.jandroidglide.load.model.FileLoader;
import com.javahe.jandroidglide.load.model.GenericLoaderFactory;
import com.javahe.jandroidglide.load.model.ModelLoader;
import com.javahe.jandroidglide.load.model.ModelLoaderFactory;
import com.javahe.jandroidglide.load.model.stream.StreamModelLoader;

import java.io.File;
import java.io.InputStream;

/**
 * Created by zfc on 2018/1/2.
 */

public class StreamFileLoader extends FileLoader<InputStream> implements StreamModelLoader<File> {

    public static class Factory implements ModelLoaderFactory<File, InputStream> {
        @Override
        public ModelLoader<File, InputStream> build(Context context, GenericLoaderFactory factories) {

            return new StreamFileLoader(factories.buildModelLoader(Uri.class, InputStream.class));
        }

        @Override
        public void teardown() {

        }
    }

//    public StreamFileLoader(Context context) {
////        this(Glide.buildStreamModelLoader(Uri.class, context));
//    }

    public StreamFileLoader(ModelLoader<Uri, InputStream> uriLoader) {
        super(uriLoader);
    }
}
