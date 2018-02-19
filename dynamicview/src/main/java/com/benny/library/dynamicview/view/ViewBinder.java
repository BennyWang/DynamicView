package com.benny.library.dynamicview.view;

import com.benny.library.dynamicview.property.DynamicProperties;
import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

import java.util.Map;

public class ViewBinder {
    private DynamicViewBuilder builder;
    private DynamicProperties properties;

    public ViewBinder(DynamicViewBuilder builder, DynamicProperties properties) {
        this.builder = builder;
        this.properties = properties;
    }

    public void bindView(JSONObject data) {
        properties.set(builder, data);
    }

    public void bindView(Map<String, String> data) {
        properties.set(builder, data);
    }
}
