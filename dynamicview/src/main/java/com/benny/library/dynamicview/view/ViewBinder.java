package com.benny.library.dynamicview.view;

import android.util.Pair;

import com.benny.library.dynamicview.property.DynamicProperties;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewBinder {
    private List<Pair<DynamicViewBuilder, DynamicProperties>> pairs = new ArrayList<>();

    public void add(DynamicViewBuilder builder, DynamicProperties properties) {
        pairs.add(Pair.create(builder, properties));
    }

    public void bind(JSONObject data) {
        for (Pair<DynamicViewBuilder, DynamicProperties> pair : pairs) {
            pair.second.set(pair.first, data);
        }
    }

    public void bind(Map<String, String> data) {
        for (Pair<DynamicViewBuilder, DynamicProperties> pair : pairs) {
            pair.second.set(pair.first, data);
        }
    }
}
