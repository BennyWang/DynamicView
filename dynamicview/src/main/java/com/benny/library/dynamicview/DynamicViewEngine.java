package com.benny.library.dynamicview;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.benny.library.dynamicview.parser.XMLLayoutParser;
import com.benny.library.dynamicview.parser.DynamicViewTree;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DynamicViewEngine implements XMLLayoutParser.SerialNumberHandler {
    private Map<String, DynamicViewTree> viewTreeMap = new HashMap<>();
    private XMLLayoutParser parser = new XMLLayoutParser(this);

    public static DynamicViewEngine getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void compile(String xml) {
        try {
            DynamicViewTree viewTree = parser.parseDocument(xml);
            String serialNumber = viewTree.getRoot().getProperty("sn");
            if (!viewTreeMap.containsKey(serialNumber)) {
                viewTreeMap.put(serialNumber, viewTree);
            }
        }
        catch (Exception ignored) {
        }
    }

    public View createView(Context context, String xml) {
        try {
            DynamicViewTree viewTree = parser.parseDocument(xml);
            String serialNumber = viewTree.getRoot().getProperty("sn");
            if (!viewTreeMap.containsKey(serialNumber)) {
                viewTreeMap.put(serialNumber, viewTree);
            }
            return viewTree.createView(context);
        }
        catch (Exception e) {
            Log.e("DynamicViewEngine", "createView Exception: " + e);
            return null;
        }
    }

    public void bindView(View view, Map<String, String> data) {
        DynamicViewTree.bindView(view, data);
    }

    public void bindView(View view, JSONObject data) {
        DynamicViewTree.bindView(view, data);
    }

    @Override
    public DynamicViewTree onReceive(String serialNumber) {
        return viewTreeMap.get(serialNumber);
    }

    private static class LazyHolder {
        private static final DynamicViewEngine INSTANCE = new DynamicViewEngine();
    }
}
