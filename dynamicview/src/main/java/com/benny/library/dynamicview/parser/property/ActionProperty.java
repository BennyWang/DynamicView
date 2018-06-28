package com.benny.library.dynamicview.parser.property;

import com.benny.library.dynamicview.api.ActionProcessor;
import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

public class ActionProperty {
    private String key;
    private String value;

    public static boolean canHandle(String key, String value) {
        return key.startsWith("on") && value.length() > 2 && value.startsWith("(") && value.endsWith(")");
    }

    public ActionProperty(String key, String value) {
        this.key = key;
        this.value = value.substring(1, value.length() - 1);
    }

    public void set(DynamicViewBuilder builder, ActionProcessor processor, JSONObject data) {
        builder.setAction(key, value, processor, data);
    }
}
