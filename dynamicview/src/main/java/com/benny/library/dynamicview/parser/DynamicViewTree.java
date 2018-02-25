package com.benny.library.dynamicview.parser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.view.ViewBinder;
import com.benny.library.dynamicview.parser.node.DynamicViewNode;
import com.benny.library.dynamicview.view.ViewInflater;

import org.json.JSONObject;

import java.util.Map;

public class DynamicViewTree implements ViewInflater {
    private static final int KEY_BINDERS = 1 + 2 << 24;
    private DynamicViewNode root;

    public DynamicViewTree(DynamicViewNode root) {
        this.root = root;
    }

    public DynamicViewNode getRoot() {
        return root;
    }

    public View inflate(Context context, ViewGroup parent) throws Exception {
        ViewBinder viewBinder = new ViewBinder();
        View contentView = root.createView(context, parent, viewBinder);
        contentView.setTag(KEY_BINDERS, viewBinder);
        return contentView;
    }

    public static void setActionProcessor(View view, ActionProcessor processor) {
        try {
            ViewBinder viewBinder = (ViewBinder) view.getTag(KEY_BINDERS);
            if (viewBinder != null) {
                viewBinder.setActionProcessor(processor);
            }
        }
        catch (Exception ignored) {
        }
    }

    public static void bindView(View view, JSONObject data) {
        try {
            ViewBinder viewBinder = (ViewBinder) view.getTag(KEY_BINDERS);
            if (viewBinder != null) {
                viewBinder.bind(data);
            }
        }
        catch (Exception ignored) {
        }
    }

    public static void bindView(View view, Map<String, String> data) {
        try {
            ViewBinder viewBinder = (ViewBinder) view.getTag(KEY_BINDERS);
            if (viewBinder != null) {
                viewBinder.bind(data);
            }
        }
        catch (Exception ignored) {
        }
    }
}
