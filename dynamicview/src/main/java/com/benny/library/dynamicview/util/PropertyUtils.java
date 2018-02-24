package com.benny.library.dynamicview.util;

import android.content.Context;

public class PropertyUtils {
    public static int dpToPx(Context context, int dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int spToPx(Context context, int sp) {
        return (int)(sp * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }
}
