package com.benny.library.dynamicview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.parser.XMLLayoutParser;
import com.benny.library.dynamicview.parser.DynamicViewTree;
import com.benny.library.dynamicview.view.ThemeManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DynamicViewEngine implements XMLLayoutParser.SerialNumberHandler {
    private Map<String, DynamicViewTree> viewTreeMap = new HashMap<>();
    private XMLLayoutParser parser = new XMLLayoutParser(this);
    private ThemeManager themeManager;

    public static DynamicViewEngine getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void setThemeManager(ThemeManager manager) {
        themeManager = manager;
    }

    public ThemeManager getThemeManager() {
        return themeManager;
    }

    public DynamicViewTree compile(String xml) throws Exception {
        DynamicViewTree viewTree = parser.parseDocument(xml);
        String serialNumber = viewTree.getRoot().getProperty("sn");
        if (!viewTreeMap.containsKey(serialNumber)) {
            viewTreeMap.put(serialNumber, viewTree);    
        }
        return viewTree;
    }

    public View inflate(Context context, ViewGroup parent, String xml) {
        long tick = System.currentTimeMillis();
        try {
            DynamicViewTree viewTree = compile(xml);
            return viewTree.inflate(context, parent);
        }
        catch (Exception e) {
            Log.e("DynamicViewEngine", "inflate Exception: " + e);
            return null;
        }
        finally {
            Log.i("DynamicViewEngine", "inflate cost " + (System.currentTimeMillis() - tick));
        }
    }

    public static void setActionProcessor(View view, ActionProcessor processor) {
        DynamicViewTree.setActionProcessor(view, processor);
    }

    public static void bindView(View view, Map<String, String> data) {
        long tick = System.currentTimeMillis();
        DynamicViewTree.bindView(view, new JSONObject(data));
        //Log.i("DynamicViewEngine", "bindView cost " + (System.currentTimeMillis() - tick));
    }

    public static void bindView(View view, JSONObject data) {
        long tick = System.currentTimeMillis();
        DynamicViewTree.bindView(view, data);
        //Log.i("DynamicViewEngine", "bindView cost " + (System.currentTimeMillis() - tick));
    }

    @Override
    public DynamicViewTree onReceive(String serialNumber) {
        return viewTreeMap.get(serialNumber);
    }

    private static class LazyHolder {
        private static final DynamicViewEngine INSTANCE = new DynamicViewEngine();
    }
}
