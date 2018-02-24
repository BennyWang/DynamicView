package com.benny.library.dynamicview.property;


import android.content.Context;
import android.util.LruCache;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.benny.library.dynamicview.util.PropertyUtils.dpToPx;

public class SizeProperty {
    private static LruCache<String, SizeProperty> cache = new LruCache<>(10);

    public int width = WRAP_CONTENT;
    public int height = WRAP_CONTENT;

    public static SizeProperty of(Context context, String value) {
        String trimValue = value.trim();
        SizeProperty property = cache.get(trimValue);
        if (property == null) {
            property = new SizeProperty(context, trimValue);
            cache.put(trimValue, property);
        }
        return property;
    }

    private SizeProperty(Context context, String value) {
        String[] parts = value.split("\\s+");
        if (parts.length == 1) {
            width = height = parseSize(context, parts[0]);
        }
        else {
            width = parseSize(context, parts[0]);
            height = parseSize(context, parts[1]);
        }
    }

    public int parseSize(Context context, String value) {
        if ("wrap".equals(value)) {
            return WRAP_CONTENT;
        }
        else if ("match".equals(value)) {
            return MATCH_PARENT;
        }

        try {
            return dpToPx(context, Integer.parseInt(value));
        }
        catch (Exception ignored) {
            return WRAP_CONTENT;
        }
    }
}
