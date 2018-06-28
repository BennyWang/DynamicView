package com.benny.library.dynamicview.parser.property;

import android.text.TextUtils;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.util.ViewIdGenerator;
import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NodeProperties {
    private ViewIdGenerator idGenerator;
    private Map<String, StaticProperty> staticProperties = new HashMap<>();
    private Map<String, DynamicProperty> dynamicProperties = new HashMap<>();
    private Map<String, ActionProperty> actions = new HashMap<>();

    public NodeProperties(ViewIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public void add(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            if (ActionProperty.canHandle(key, value)) {
                actions.put(key, new ActionProperty(key, value));
            }
            else if (DynamicProperty.canHandle(key, value)) {
                DynamicProperty property = new DynamicProperty(key, value);
                dynamicProperties.put(key, property);

                // 如果存在默认值，添加到静态属性中
                String defaultValue = property.getDefaultValue();
                if (!TextUtils.isEmpty(defaultValue)) {
                    processStaticProperty(key, defaultValue);
                }
            } else {
                processStaticProperty(key, value);
            }
        }
    }

    public String get(String key) {
        return staticProperties.containsKey(key) ? staticProperties.get(key).getValue() : null;
    }

    public void set(DynamicViewBuilder builder) {
        for (Map.Entry<String, StaticProperty> entry : staticProperties.entrySet()) {
            entry.getValue().set(builder);
        }
    }

    public void setAction(DynamicViewBuilder builder, ActionProcessor processor) {
        if (!actions.isEmpty()) {
            for (Map.Entry<String, ActionProperty> entry : actions.entrySet()) {
                entry.getValue().set(builder, processor, null);
            }
        }
    }

    public void set(DynamicViewBuilder builder, ActionProcessor processor, JSONObject data) {
        for (Map.Entry<String, DynamicProperty> entry : dynamicProperties.entrySet()) {
            entry.getValue().set(builder, data);
        }

        for (Map.Entry<String, ActionProperty> entry : actions.entrySet()) {
            entry.getValue().set(builder, processor, data);
        }
    }

    private void processStaticProperty(String key, String value) {
        if (key.equals("name")) {
            staticProperties.put("id", new StaticProperty("id", idGenerator.getId(value)));
        }
        else if (value.startsWith("@")) {
            String relatedName = value.substring(1);
            if (idGenerator.contains(relatedName)) {
                staticProperties.put(key, new StaticProperty(key, idGenerator.getId(relatedName)));
            }
        }
        else {
            staticProperties.put(key, new StaticProperty(key, value));
        }
    }
}
