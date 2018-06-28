package com.benny.library.dynamicview.api;

import android.content.Context;
import android.text.Layout;

public interface LayoutCache {
    Layout get(Context context, String key);
    Layout put(Context context, String key, Layout value);
}
