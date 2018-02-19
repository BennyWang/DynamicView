package com.benny.library.dynamicview.view;

import android.content.Context;
import android.util.Log;
import android.util.LruCache;

import java.lang.reflect.Constructor;


public class DynamicViewBuilderFactory {
    private static final String GENERATED_PACKAGE = "com.benny.library.dynamicview.builder.";
    private static LruCache<String, Constructor<DynamicViewBuilder>> cache = new LruCache<>(50);

    public static DynamicViewBuilder create(Context context, String name) throws Exception {
        Constructor<DynamicViewBuilder> constructor = cache.get(name);
        if (constructor == null) {
            Class<DynamicViewBuilder> clazz = (Class<DynamicViewBuilder>) Class.forName(GENERATED_PACKAGE + name + "$$Builder");
            constructor = clazz.getConstructor(Context.class);
            cache.put(name, constructor);
        }
        return constructor.newInstance(context);
    }

    public static void register(String name) {
        try {
            Constructor<DynamicViewBuilder> constructor = cache.get(name);
            if (constructor == null) {
                Class<DynamicViewBuilder> clazz = (Class<DynamicViewBuilder>) Class.forName(GENERATED_PACKAGE + name + "$$Builder");
                constructor = clazz.getConstructor(Context.class);
                cache.put(name, constructor);
            }
        }
        catch (Exception ignored) {
        }
    }

}
