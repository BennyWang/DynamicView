package com.benny.library.dynamicview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.api.ActionProcessor;
import com.benny.library.dynamicview.api.DynamicViewEngine;
import com.benny.library.dynamicview.api.ImageLoader;
import com.benny.library.dynamicview.api.LayoutCache;
import com.benny.library.dynamicview.api.ThemeManager;
import com.benny.library.dynamicview.parser.XMLLayoutParser;
import com.benny.library.dynamicview.parser.DynamicViewTree;
import com.benny.library.dynamicview.util.ThemeUtils;
import com.benny.library.dynamicview.widget.Image;
import com.benny.library.dynamicview.widget.Label;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DynamicViewEngineImpl implements DynamicViewEngine, XMLLayoutParser.SerialNumberHandler {
    private static volatile DynamicViewEngineImpl instance;

    private Map<String, DynamicViewTree> viewTreeMap = new HashMap<>();
    private XMLLayoutParser parser = new XMLLayoutParser(this);

    public static DynamicViewEngineImpl getInstance() {
        if (instance == null) {
            synchronized (DynamicViewEngineImpl.class) {
                instance = new DynamicViewEngineImpl();
            }
        }
        return instance;
    }

    public void compile(String xml) {
        try {
            preprocess(xml);
        }
        catch (Exception e) {
            Log.e("DynamicViewEngineImpl", "compile Exception: " + Log.getStackTraceString(e));
        }

    }

    public View inflate(Context context, ViewGroup parent, String xml) {
        long tick = System.currentTimeMillis();
        try {
            DynamicViewTree viewTree = preprocess(xml);
            return viewTree.inflate(context, parent);
        }
        catch (Exception e) {
            Log.e("DynamicViewEngineImpl", "inflate Exception: " + Log.getStackTraceString(e));
            return null;
        }
        finally {
            Log.i("DynamicViewEngineImpl", "inflate cost " + (System.currentTimeMillis() - tick));
        }
    }

    public void setThemeManager(ThemeManager manager) {
        ThemeUtils.setThemeManager(manager);
    }

    public void setActionProcessor(View view, ActionProcessor processor) {
        DynamicViewTree.setActionProcessor(view, processor);
    }

    @Override
    public void setImageLoader(ImageLoader loader) {
        Image.setImageLoader(loader);
    }

    @Override
    public void setLayoutCache(LayoutCache cache) {
        Label.setLayoutCache(cache);
    }

    public void bindView(View view, JSONObject data) {
        DynamicViewTree.bindView(view, data);
    }

    @Override
    public DynamicViewTree onReceive(String serialNumber) {
        return viewTreeMap.get(serialNumber);
    }

    private DynamicViewTree preprocess(String xml) throws Exception {
        DynamicViewTree viewTree = parser.parseDocument(xml);
        String serialNumber = viewTree.getRoot().getProperty("sn");
        if (!viewTreeMap.containsKey(serialNumber)) {
            viewTreeMap.put(serialNumber, viewTree);
        }
        return viewTree;
    }
}
