package com.benny.library.dynamicview.parser.property;

import android.text.TextUtils;
import android.util.Log;

import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

public class DynamicProperty {
    private String key;
    private String valueKey;
    private String defaultValue;

    private boolean isThemed = false;

    public static boolean canHandle(String key, String value) {
        if (value.startsWith("?")) {
            // for theme property
            return true;
        }
        return value.length() > 2 && value.startsWith("{") && value.endsWith("}");
    }

    public DynamicProperty(String key, String valueKey) {
        this.key = key;
        if (valueKey.startsWith("?")) {
            isThemed = true;
            this.valueKey = valueKey;
        }
        else {
            valueKey = valueKey.substring(1, valueKey.length() - 1);
            if (valueKey.contains("|")) {
                int index = valueKey.indexOf("|");
                this.valueKey = valueKey.substring(0, index);
                if (index < valueKey.length() - 1) {
                    defaultValue = valueKey.substring(index + 1);
                }
            } else {
                this.valueKey = valueKey;
            }
        }
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void set(DynamicViewBuilder builder, JSONObject data) {
        try {
            if (isThemed) {
                builder.setProperty(key, valueKey);
            }
            else if (data.has(valueKey)) {
                builder.setProperty(key, data.opt(valueKey));
            }
            else if (!TextUtils.isEmpty(defaultValue)) {
                builder.setProperty(key, defaultValue);
            }
        }
        catch (Exception e) {
            Log.e("DynamicViewEngine", "DynamicProperty.set exception " + Log.getStackTraceString(e));
        }
    }
}
