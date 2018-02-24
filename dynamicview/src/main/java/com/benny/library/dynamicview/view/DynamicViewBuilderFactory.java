package com.benny.library.dynamicview.view;

import android.content.Context;
import android.util.LruCache;

public class DynamicViewBuilderFactory {
    private static final String GENERATED_PACKAGE = "com.benny.library.dynamicview.builder.";
    private static LruCache<String, Class<?>> classCache = new LruCache<>(50);

    public static DynamicViewBuilder create(Context context, String name) throws Exception {
        Class<?> clazz = register(name);
        DynamicViewBuilder builder = (DynamicViewBuilder) clazz.newInstance();

        builder.createView(context);
        return builder;
    }

    public static Class<?> register(String name) throws Exception {
        Class<?> clazz = classCache.get(name);
        if (clazz == null) {
            clazz = Class.forName(GENERATED_PACKAGE + name + "$$Builder");
            classCache.put(name, clazz);

            clazz.newInstance();
        }
        return clazz;
    }

}
