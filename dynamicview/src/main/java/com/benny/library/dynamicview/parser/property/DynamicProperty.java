package com.benny.library.dynamicview.parser.property;

import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

public class DynamicProperty {
    private String key;
    private String valueKey;

    public static boolean canHandle(String key, String value) {
        return value.length() > 2 && value.startsWith("{") && value.endsWith("}");
    }

    public DynamicProperty(String key, String valueKey) {
        this.key = key;
        this.valueKey = valueKey.substring(1, valueKey.length() - 1);
    }

    public void set(DynamicViewBuilder builder, JSONObject data) {
        try {
            if (data.has(valueKey)) {
                builder.setProperty(key, data.opt(valueKey));
            }
        }
        catch (Exception ignored) {
        }
    }
}
