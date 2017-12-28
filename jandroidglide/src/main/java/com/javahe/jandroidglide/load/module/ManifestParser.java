package com.javahe.jandroidglide.load.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zfc on 2017/12/28.
 */

public class ManifestParser {

    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    public List<GlideModule> parse() {
        ArrayList<GlideModule> modules = new ArrayList<>();

        try {
            ApplicationInfo appInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                Iterator i$ = appInfo.metaData.keySet().iterator();
                while (i$.hasNext()) {
                    String key = (String) i$.next();
                    if ("GlideModule".equals(appInfo.metaData.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
            return modules;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("unable to find metedata to parse GlideModule");
        }

    }

    /**
     * 根据反射拿到GlideModule的实体类
     *
     * @param className
     * @return
     */
    private GlideModule parseModule(String className) {
        Class clzz;
        try {
            clzz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("unable to find GlideModule impletiom", e);
        }

        Object module;
        try {
            module = clzz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("unable to init class " + className, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unable to init class " + className, e);
        }

        if (module instanceof GlideModule) {
            return (GlideModule) module;
        } else {
            throw new RuntimeException("Expected instanceof GlideModule, but found: " + module);
        }
    }
}
