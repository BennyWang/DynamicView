package com.benny.library.dynamicview.util;

import android.content.Context;
import android.view.View;

import com.benny.library.dynamicview.DynamicViewEngine;
import com.benny.library.dynamicview.view.ThemeManager;

public class ViewUtils {
    private static final int KEY_THEME_ID = 2 + 2 << 24;

    public static void setThemeId(View view, String themeId) {
        view.setTag(KEY_THEME_ID, themeId);
    }

    public static String getThemeId(View view) {
        return (String) view.getTag(KEY_THEME_ID);
    }

    public static int getThemeColor(String themeId, int fallback) {
        ThemeManager themeManager = DynamicViewEngine.getInstance().getThemeManager();
        if (themeId != null && themeManager != null) {
            return themeManager.getThemeColor(themeId, fallback);
        }
        return fallback;
    }
}
