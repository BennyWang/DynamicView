package com.benny.library.dynamicview;

import android.app.Activity;
import android.content.Context;
import android.util.LruCache;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

public class LifecycleCacheManager extends LifecycleCallbacks {
    private static volatile LifecycleCacheManager instance;
    private SparseArray<Map<String, LruCache > > cacheMap = new SparseArray<>();

    public static LifecycleCacheManager getInstance() {
        if (instance == null) {
            synchronized (LifecycleCacheManager.class) {
                if (instance == null) {
                    instance = new LifecycleCacheManager();
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> LruCache<String, T> makeCache(Context context, String key, Class<T> clazz) {
        Map<String, LruCache> caches = cacheMap.get(context.hashCode());
        if (caches == null) {
            caches = new HashMap<>();
            cacheMap.put(context.hashCode(), caches);
        }

        LruCache<String, T> cache = caches.get(key);
        if (cache == null) {
            cache = new LruCache<>(50);
            caches.put(key, cache);
        }
        return cache;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        cacheMap.remove(activity.hashCode());
    }
}
