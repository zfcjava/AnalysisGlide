package com.javahe.jandroidglide;

import android.content.Context;

import com.javahe.jandroidglide.manager.ConectivityMonitorFactory;
import com.javahe.jandroidglide.manager.Lifecycle;
import com.javahe.jandroidglide.manager.LifecycleListener;
import com.javahe.jandroidglide.manager.RequestManagerTreeNode;
import com.javahe.jandroidglide.manager.RequestTracker;

/**
 * Created by zfc on 2017/12/27.
 */

public class RequestManager implements LifecycleListener {
    private final Lifecycle lifecycle;
    private final RequestManagerTreeNode treeNode;
    private Context context;
    private RequestTracker requestTracker;
    private final Glide glide;

    public RequestManager(Context context, Lifecycle lifecycle, RequestManagerTreeNode treeNode) {
        this(context, lifecycle, treeNode, new RequestTracker(), new ConectivityMonitorFactory());
    }

    public RequestManager(Context context, Lifecycle lifecycle, RequestManagerTreeNode treeNode, RequestTracker requestTracker, ConectivityMonitorFactory factory) {
        this.context = context.getApplicationContext();
        this.lifecycle = lifecycle;
        this.treeNode = treeNode;
        this.requestTracker = requestTracker;
        this.glide = Glide.get(context);


    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
