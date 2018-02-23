package com.benny.library.dynamicview.property;


import android.content.Context;
import android.util.LruCache;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.benny.library.dynamicview.util.PropertyUtils.dpToPx;

public class SizeProperty {
    private static LruCache<String, SizeProperty> cache = new LruCache<>(10);

    public int size = MATCH_PARENT;

    public static SizeProperty of(Context context, String value) {
        SizeProperty property = cache.get(value);
        if (property == null) {
            property = new SizeProperty(context, value);
            cache.put(value, property);
        }
        return property;
    }

    private SizeProperty(Context context, String value) {
        if ("wrap".equals(value)) {
            size = WRAP_CONTENT;
        }
        else if ("match".equals(value)) {
            size = MATCH_PARENT;
        }
        else {
            try {
                size = dpToPx(context, Integer.parseInt(value));
            }
            catch (Exception ignored) {
            }
        }
    }
}
