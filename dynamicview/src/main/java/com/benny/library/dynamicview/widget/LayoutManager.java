package com.benny.library.dynamicview.widget;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.LruCache;

public class LayoutManager {
    private static volatile LayoutManager instance;
    private LruCache<String, Layout> cache = new LruCache<>(100);

    public static LayoutManager getInstance() {
        if (instance == null) {
            synchronized (LayoutManager.class) {
                if (instance == null) {
                    instance = new LayoutManager();
                }
            }
        }

        return instance;
    }

    private LayoutManager() {
    }

    public Layout layoutFor(String text, int textSize, int width, TextPaint paint) {
        String key = text + textSize;
        Layout layout = cache.get(key);
        if (layout != null && layout.getWidth() <= width) {
            return layout;
        }

        layout = new StaticLayout(text, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, true);
        cache.put(key, layout);
        return layout;

    }
}
