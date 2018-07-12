package com.benny.library.dynamicview.api;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.io.File;

public interface DynamicViewEngine {
    void compile(String layout);
    void compile(File layout);
    void compileAll(File layout);

    View inflate(Context context, ViewGroup parent, String xml);
    View inflateWithId(Context context, ViewGroup parent, String id);
    void bindView(View view, JSONObject data);

    void setThemeManager(ThemeManager manager);
    void setActionProcessor(View view, ActionProcessor processor);
    void setImageLoader(ImageLoader loader);
    void setHttpCacheProxy(HttpCacheProxy proxy);


}
