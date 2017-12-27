package com.javahe.jandroidglide.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.javahe.jandroidglide.RequestManager;
import com.javahe.jandroidglide.RequestManagerFragment;
import com.javahe.jandroidglide.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zfc on 2017/12/27.
 */

public class RequestManagerRetriever implements Handler.Callback {

    private final static RequestManagerRetriever INSTANCE = new RequestManagerRetriever();
    static final String FRAGMENT_TAG = "com.javahe.jandroidglide.manager";
    static final String TAG = "RequestManagerRetriever";

    private volatile Handler handler;

    private  volatile RequestManager applicationManager;
    private Map<FragmentManager, RequestManagerFragment> pendingRequestManagerFragments = new HashMap<>();
    private final static int ID_REMOVE_FRAGMENT_MANAGER = 100;
    private final static int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 101;

    public static RequestManagerRetriever get() {
        return INSTANCE;
    }

    public RequestManagerRetriever() {
        //创建主线程的对象
        handler = new Handler(Looper.getMainLooper(),this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RequestManager get(Activity activity) {
        if (Util.isOnBackgroundThread() || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return get(activity.getApplicationContext());
        } else {
            assertNotDestroyed(activity);
            android.app.FragmentManager fragmentManager = activity.getFragmentManager();
            return get(activity, fragmentManager);
        }
    }


    private RequestManager get(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("you cannot start a load on a null context");
        }

        if (Util.isOnMainThread() && !(context instanceof Application)) {
            if (context instanceof FragmentActivity) {
                return get((FragmentActivity) context);
            } else if (context instanceof Activity) {
                return get((Activity) context);
            } else if (context instanceof ContextWrapper) {
                return get(((ContextWrapper) context).getBaseContext());
            }
        }
        return getApplicationManager(context);
    }

    private RequestManager getApplicationManager(Context context) {
        if (applicationManager == null) {
            synchronized (this) {
                if (applicationManager == null) {
                    applicationManager = new RequestManager();
                }
            }
        }
        return applicationManager;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private RequestManager get(Activity activity, FragmentManager fm) {
        //先去获取fragment中的RequestManager
        RequestManagerFragment current = getRequestManagerFragment(fm);
        RequestManager requestManager = current.getRequestManager();
        if (requestManager == null) {
            requestManager = new RequestManager();
        }
        return requestManager;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private RequestManagerFragment getRequestManagerFragment(FragmentManager fm) {
        //利用缓存机制，找到当前的Fragment
        RequestManagerFragment current = (RequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = pendingRequestManagerFragments.get(fm);
            if (current == null) {
                current = new RequestManagerFragment();
                pendingRequestManagerFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG);
                //发送详细
                handler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void assertNotDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            throw new IllegalArgumentException("you cannot start a load when the activity distroyed");
        }
    }


    @Override
    public boolean handleMessage(Message message) {
        boolean handle = true;
        Object key = null;
        Object result = null;
        switch (message.what) {
            case ID_REMOVE_FRAGMENT_MANAGER:
                key = message.obj;
                result = pendingRequestManagerFragments.remove(key);
                break;
            case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER:
                break;
            default:
                handle = false;
                break;

        }
        if (handle && result == null && Log.isLoggable(TAG, Log.WARN)) { //TAG 最多为23个字符
            Log.w(TAG, "移除失败");
        }
        return false;
    }
}
