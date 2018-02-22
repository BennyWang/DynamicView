package com.benny.library.dynamicview.view;

import android.content.Context;
import android.util.LruCache;

import java.lang.reflect.Constructor;

public class DynamicViewBuilderFactory {
    private static final String GENERATED_PACKAGE = "com.benny.library.dynamicview.builder.";
    private static LruCache<String, Constructor<DynamicViewBuilder>> constructorCache = new LruCache<>(50);
    private static LruCache<String, Class<?>> classCache = new LruCache<>(50);

    public static DynamicViewBuilder create(Context context, String name) throws Exception {
        Constructor<DynamicViewBuilder> constructor = constructorCache.get(name);
        if (constructor == null) {
            register(name);
            constructor = constructorCache.get(name);
        }
        return constructor.newInstance(context);
    }

    public static Class<?> register(String name) throws Exception {
        Constructor<DynamicViewBuilder> constructor = constructorCache.get(name);
        if (constructor == null) {
            Class<?> clazz = classCache.get(name);
            if (clazz == null) {
                clazz = Class.forName(GENERATED_PACKAGE + name + "$$Builder");
                classCache.put(name, clazz);
            }
            constructor = (Constructor<DynamicViewBuilder>) clazz.getConstructor(Context.class);
            constructorCache.put(name, constructor);
        }
        return classCache.get(name);
    }

}
