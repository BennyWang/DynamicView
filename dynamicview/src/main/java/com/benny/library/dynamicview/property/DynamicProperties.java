package com.benny.library.dynamicview.property;

import android.text.TextUtils;

import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DynamicProperties {
    private Map<String, String> staticProperties = new HashMap<>();
    private Map<String, String> dynamicProperties = new HashMap<>();

    public void add(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            if (value.startsWith("{") && value.endsWith("}")) {
                dynamicProperties.put(key, value.substring(1, value.length() - 1));
            } else {
                staticProperties.put(key, value);
            }
        }
    }

    public String get(String key) {
        return staticProperties.get(key);
    }

    public int getInt(String key, int defaultValue) {
         String value = staticProperties.get(key);
         if (TextUtils.isEmpty(value)) {
             return defaultValue;
         }

         try {
             return Integer.parseInt(value);
         }
         catch (Exception e) {
             return defaultValue;
         }
    }

    public void set(DynamicViewBuilder builder) {
        try {
            for (Map.Entry<String, String> entry : staticProperties.entrySet()) {
                builder.setProperty(entry.getKey(), entry.getValue());
            }
        }
        catch (Exception ignored) {
        }
    }

    public void set(DynamicViewBuilder builder, Map<String, String> data) {
        try {
            for (Map.Entry<String, String> entry : dynamicProperties.entrySet()) {
                if (data.containsKey(entry.getValue())) {
                    builder.setProperty(entry.getKey(), data.get(entry.getValue()));
                }
            }
        }
        catch (Exception ignored) {
        }
    }

    public void set(DynamicViewBuilder builder, JSONObject data) {
        try {
            for (Map.Entry<String, String> entry : dynamicProperties.entrySet()) {
                if (data.has(entry.getValue())) {
                    builder.setProperty(entry.getKey(), data.optString(entry.getValue()));
                }
            }
        }
        catch (Exception ignored) {
        }
    }
}
