package com.benny.library.dynamicview.util;

import com.benny.library.dynamicview.DynamicViewEngineImpl;
import com.benny.library.dynamicview.ThemeManager;

public class ThemeUtils {
    private static ThemeManager manager;

    public static void setThemeManager(ThemeManager manager) {
        ThemeUtils.manager = manager;
    }

    public static int getThemeColor(String themeId, int fallback) {
        if (themeId != null && manager != null) {
            return manager.getThemeColor(themeId, fallback);
        }
        return fallback;
    }
}
