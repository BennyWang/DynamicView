package com.benny.library.dynamicview.widget.property;


import android.content.Context;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.Gravity;

public class GravityProperty {
    private static LruCache<String, GravityProperty> cache = new LruCache<>(10);
    public int gravity = 0;

    public static GravityProperty of(Context context, String value) {
        GravityProperty property = cache.get(value);
        if (property == null) {
            property = new GravityProperty(context, value);
            cache.put(value, property);
        }
        return property;
    }

    private GravityProperty(Context context, String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }

        String[] gravities = value.trim().split("\\s*\\|\\s*");
        for (String s : gravities) {
            gravity = gravity | parseGravity(s);
        }
    }

    private int parseGravity(String value) {
        switch (value) {
            case "top":
                return Gravity.TOP;
            case "bottom":
                return Gravity.BOTTOM;
            case "start":
                return Gravity.START;
            case "end":
                return Gravity.END;
            case "center":
                return Gravity.CENTER;
            case "vcenter":
                return Gravity.CENTER_VERTICAL;
            case "hcenter":
                return Gravity.CENTER_HORIZONTAL;
        }
        return 0;
    }
}
