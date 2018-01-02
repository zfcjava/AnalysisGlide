package com.javahe.jandroidglide.provider;
import com.javahe.jandroidglide.load.resource.bitmap.StreamBitmapDataLoadProvider;
import com.javahe.jandroidglide.util.MultiClassKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zfc on 2018/1/2.
 */

public class DataLoadProviderRepositry {
    /**
     * 避免重复创建  MultiClassKey
     */
    private static final MultiClassKey GET_KEY = new MultiClassKey();

    private final Map<MultiClassKey, DataLoadProvider<?, ?>> providers = new HashMap<>();

    public <T, Z>void register(Class<T> dataClass, Class<Z> resourceClass, StreamBitmapDataLoadProvider provider) {
        providers.put(new MultiClassKey(dataClass, resourceClass), provider);
    }

    public <T, Z> DataLoadProvider<T, Z> get(Class<T> dataClass, Class<Z> resourceClass) {
        DataLoadProvider<?, ?> result;
        synchronized (GET_KEY) {
            GET_KEY.set(dataClass, resourceClass);
            result = providers.get(GET_KEY);

        }
        if (result == null) {
            result = EmptyDataLoadProvider.get();
        }

        return (DataLoadProvider<T, Z>) result;
    }

}
