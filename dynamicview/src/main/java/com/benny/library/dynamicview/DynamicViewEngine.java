package com.benny.library.dynamicview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.parser.DynamicViewTree;
import com.benny.library.dynamicview.widget.Image;
import com.benny.library.dynamicview.widget.Label;

import org.json.JSONObject;

public interface DynamicViewEngine {
    DynamicViewTree compile(String xml) throws Exception;
    View inflate(Context context, ViewGroup parent, String xml);
    void bindView(View view, JSONObject data);

    void setThemeManager(ThemeManager manager);
    void setActionProcessor(View view, ActionProcessor processor);

    void setImageLoader(Image.ImageLoader loader);
    void setLayoutCache(Label.LayoutCache cache);


}
