package com.javahe.jandroidglide.manager;

/**
 * Created by zfc on 2017/12/27.
 */

public interface ConectivityMonitor extends LifecycleListener{
    interface ConectivityListener{
        void onConnectivityChanged(boolean isConnect);
    }
}
