package com.javahe.jandroidglide.manager;

import com.javahe.jandroidglide.RequestManager;

import java.util.Set;

/**
 * Created by zfc on 2017/12/27.
 */

public interface RequestManagerTreeNode {
    Set<RequestManager> getDescendants();
}
