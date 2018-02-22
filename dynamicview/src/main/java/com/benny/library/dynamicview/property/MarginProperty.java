package com.benny.library.dynamicview.property;

import android.content.Context;
import android.util.LruCache;

import static com.benny.library.dynamicview.property.PropertyUtils.dpToPx;

public class MarginProperty {
    private static LruCache<String, MarginProperty> cache = new LruCache<>(10);

    public int left;
    public int top;
    public int right;
    public int bottom;

    public static MarginProperty of(Context context, String value) {
        MarginProperty property = cache.get(value);
        if (property == null) {
            property = new MarginProperty(context, value);
            cache.put(value, property);
        }
        return property;
    }

    private MarginProperty(Context context, String margins) {
        int[] intMargins = parseMargin(context, margins.trim());
        switch (intMargins.length) {
            case 1:
            case 2:
                left = right = intMargins[0];
                top = bottom = intMargins.length == 1 ? intMargins[0] : intMargins[1];
                break;
            case 3:
            case 4:
                left = intMargins[0];
                top = intMargins[1];
                right = intMargins[2];
                bottom = intMargins.length == 3 ? 0 : intMargins[4];
                break;
        }
    }

    protected int[] parseMargin(Context context, String value) {
        String[] margins = value.split("\\s+");
        int[] intMargins = new int[margins.length];
        for (int i = 0; i < margins.length; ++i) {
            intMargins[i] = dpToPx(context, Integer.parseInt(margins[i]));
        }
        return intMargins;
    }
}
