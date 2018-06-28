package com.benny.library.dynamicview.api;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

public interface DynamicViewEngine {
    void compile(String xml) throws Exception;
    View inflate(Context context, ViewGroup parent, String xml);
    void bindView(View view, JSONObject data);

    void setThemeManager(ThemeManager manager);
    void setActionProcessor(View view, ActionProcessor processor);

    void setImageLoader(ImageLoader loader);
    void setLayoutCache(LayoutCache cache);


}
