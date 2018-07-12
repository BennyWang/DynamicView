package com.benny.library.dynamicview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.api.ActionProcessor;
import com.benny.library.dynamicview.api.DynamicViewEngine;
import com.benny.library.dynamicview.api.HttpCacheProxy;
import com.benny.library.dynamicview.api.ImageLoader;
import com.benny.library.dynamicview.api.ThemeManager;
import com.benny.library.dynamicview.parser.DynamicViewTree;
import com.benny.library.dynamicview.parser.XMLLayoutParser;
import com.benny.library.dynamicview.util.ThemeUtils;
import com.benny.library.dynamicview.widget.Image;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DynamicViewEngineImpl implements DynamicViewEngine, XMLLayoutParser.SerialNumberHandler {
    private Map<String, DynamicViewTree> viewTreeMap = new HashMap<>();
    private XMLLayoutParser parser = new XMLLayoutParser(this);

    public void compile(String layout) {
        try {
            preprocess(layout);
        }
        catch (Exception e) {
            Log.e("DynamicViewEngineImpl", "compile Exception: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void compile(File layout) {
        try {
            preprocess(layout);
        }
        catch (Exception e) {
            Log.e("DynamicViewEngineImpl", "compile Exception: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void compileAll(File dir) {
        File[] layoutFiles = dir.listFiles();
        if (layoutFiles != null) {
            for (File layoutFile : layoutFiles) {
                compile(layoutFile);
            }
        }
    }

    public View inflate(Context context, ViewGroup parent, String xml) {
        long tick = System.currentTimeMillis();
        LifeCycleCallbacksManager.initialize(context);
        try {
            DynamicViewTree viewTree = preprocess(xml);
            return viewTree.inflate(context, parent);
        }
        catch (Exception e) {
            Log.e("DynamicViewEngineImpl", "inflate Exception: " + e);
            return null;
        }
        finally {
            Log.i("DynamicViewEngineImpl", "inflate cost " + (System.currentTimeMillis() - tick));
        }
    }

    @Override
    public View inflateWithId(Context context, ViewGroup parent, String id) {
        long tick = System.currentTimeMillis();
        LifeCycleCallbacksManager.initialize(context);
        try {
            DynamicViewTree viewTree = viewTreeMap.get(id);
            return viewTree.inflate(context, parent);
        }
        catch (Exception e) {
            Log.e("DynamicViewEngineImpl", "inflate Exception: " + e);
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
    public void setHttpCacheProxy(HttpCacheProxy proxy) {
        HttpCacheProxyManager.setProxy(proxy);
    }

    public void bindView(View view, JSONObject data) {
        DynamicViewTree.bindView(view, data);
    }

    @Override
    public DynamicViewTree onReceive(String serialNumber, long version) {
        DynamicViewTree viewTree = viewTreeMap.get(serialNumber);
        if (viewTree != null && viewTree.getVersion() >= version) {
            return viewTree;
        }
        return null;
    }

    private DynamicViewTree preprocess(String layout) throws Exception {
        final DynamicViewTree viewTree = parser.parseDocument(layout);
        final String serialNumber = viewTree.getSerialNumber();
        if (!viewTreeMap.containsKey(serialNumber)) {
            viewTreeMap.put(serialNumber, viewTree);
        }
        return viewTree;
    }

    private DynamicViewTree preprocess(File layout) throws Exception {
        final DynamicViewTree viewTree = parser.parseDocument(layout);
        final String serialNumber = viewTree.getSerialNumber();
        if (!viewTreeMap.containsKey(serialNumber)) {
            viewTreeMap.put(serialNumber, viewTree);
        }
        return viewTree;
    }
}
