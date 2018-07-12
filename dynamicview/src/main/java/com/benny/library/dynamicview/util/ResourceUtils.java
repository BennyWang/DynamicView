package com.benny.library.dynamicview.util;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class ResourceUtils {
    private static Map<String, Integer> resIdMap = new HashMap<>();

    public static int getResourceIdByName(Context context, String name) {
        Integer drawableId = resIdMap.get(name);
        if (drawableId != null) {
            return drawableId;
        }

        int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        if (resId == 0) {
            resId = context.getResources().getIdentifier(name, "mipmap", context.getPackageName());
        }
        resIdMap.put(name, resId);
        return resId;
    }
}
