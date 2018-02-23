package com.benny.library.dynamicview.property;

import android.content.Context;
import android.util.LruCache;

import static com.benny.library.dynamicview.util.PropertyUtils.dpToPx;

public class RadiusProperty {
    private static LruCache<String, RadiusProperty> cache = new LruCache<>(10);

    public int topLeft;
    public int topRight;
    public int bottomRight;
    public int bottomLeft;

    public static RadiusProperty of(Context context, String value) {
        RadiusProperty property = cache.get(value);
        if (property == null) {
            property = new RadiusProperty(context, value);
            cache.put(value, property);
        }
        return property;
    }

    private RadiusProperty(Context context, String value) {
        int[] radius = parseRadius(context, value.trim());
        switch (radius.length) {
            case 1:
            case 2:
                topLeft = topRight = radius[0];
                bottomLeft = bottomRight = radius.length == 1 ? radius[0] : radius[1];
                break;
            case 3:
            case 4:
                topLeft = radius[0];
                topRight = radius[1];
                bottomRight = radius[2];
                bottomLeft = radius.length == 3 ? 0 : radius[3];
                break;
        }
    }

    protected int[] parseRadius(Context context, String value) {
        String[] radius = value.split("\\s+");
        int[] intRadius = new int[radius.length];
        for (int i = 0; i < radius.length; ++i) {
            intRadius[i] = dpToPx(context, Integer.parseInt(radius[i]));
        }
        return intRadius;
    }
}
