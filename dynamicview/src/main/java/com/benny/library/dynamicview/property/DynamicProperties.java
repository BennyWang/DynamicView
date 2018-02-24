package com.benny.library.dynamicview.property;

import android.text.TextUtils;

import com.benny.library.dynamicview.parser.ViewIdGenerator;
import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DynamicProperties {
    private ViewIdGenerator idGenerator;
    private Map<String, String> staticProperties = new HashMap<>();
    private Map<String, String> dynamicProperties = new HashMap<>();

    public DynamicProperties(ViewIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public void add(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }

        if (key.equals("name")) {
            staticProperties.put("id", idGenerator.getId(value));
        }
        else if (value.startsWith("@")) {
            String relatedName = value.substring(1);
            if (idGenerator.contains(relatedName)) {
                staticProperties.put(key, idGenerator.getId(relatedName));
            }
        }
        else if (value.startsWith("{") && value.endsWith("}")) {
            dynamicProperties.put(key, value.substring(1, value.length() - 1));
        } else {
            staticProperties.put(key, value);
        }
    }

    public String get(String key) {
        return staticProperties.get(key);
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
