package com.benny.library.dynamicview.parser.property;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

import java.util.Map;

public class DynamicActionProperty {
    private String key;
    private String value;

    public static boolean canHandle(String key, String value) {
        return key.startsWith("on") && value.length() > 4 && value.startsWith("({") && value.endsWith("})");
    }

    public DynamicActionProperty(String key, String value) {
        this.key = key;
        this.value = value.substring(2, value.length() - 2);
    }

    public void set(DynamicViewBuilder builder, ActionProcessor processor, JSONObject data) {
        builder.setAction(key, data.optString(value), processor);
    }

    public void set(DynamicViewBuilder builder, ActionProcessor processor, Map<String, String> data) {
        builder.setAction(key, data.get(value), processor);
    }
}
