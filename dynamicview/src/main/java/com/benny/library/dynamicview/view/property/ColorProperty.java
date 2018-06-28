package com.benny.library.dynamicview.view.property;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.LruCache;

import com.benny.library.dynamicview.util.ThemeUtils;

public class ColorProperty {
    private static LruCache<String, ColorProperty> cache = new LruCache<>(10);

    private String themeId;
    private int defaultColor;

    /**
     *
     * @param context
     * @param value  "#666   xxx xxx xxx xxx"
     *                color  radius
     */
    public static ColorProperty of(Context context, String value) {
        ColorProperty property = cache.get(value);
        if (property == null) {
            property = new ColorProperty(context, value);
            cache.put(value, property);
        }
        return property;
    }

    public ColorProperty(Context context, String value) {
        if (value.startsWith("?")) {
            int splitIndex = value.indexOf(":");
            if (splitIndex == -1) {
                themeId = value.substring(1);
                defaultColor = Color.BLACK;
            }
            else {
                themeId = value.substring(1, splitIndex);
                defaultColor = Color.parseColor(value.substring(splitIndex + 1));
            }
        }
        else {
            defaultColor = Color.parseColor(value);
        }
    }

    public int getColor() {
        if (TextUtils.isEmpty(themeId)) {
            return defaultColor;
        }

        return ThemeUtils.getThemeColor(themeId, defaultColor);
    }
}
