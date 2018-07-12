package com.benny.library.dynamicview.parser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.api.ActionProcessor;
import com.benny.library.dynamicview.api.ViewBinder;
import com.benny.library.dynamicview.parser.node.DynamicViewNode;
import com.benny.library.dynamicview.view.ViewBinderImpl;

import org.json.JSONObject;

public class DynamicViewTree implements ViewInflater {
    private static final int KEY_BINDERS = 1 + 2 << 24;
    private DynamicViewNode root;
    private ActionProcessor defaultProcessor;

    public DynamicViewTree(DynamicViewNode root) {
        this.root = root;
    }

    public DynamicViewTree(DynamicViewNode root, ActionProcessor processor) {
        this.root = root;
        defaultProcessor = processor;
    }

    public String getSerialNumber() {
        return root == null ? "" : root.getProperty("sn");
    }

    public long getVersion() {
        return root == null ? 0 : root.getLongProperty("version", 0);
    }

    public View inflate(Context context, ViewGroup parent) throws Exception {
        ViewBinderImpl viewBinder = new ViewBinderImpl();
        View contentView = root.createView(context, parent, viewBinder);
        if (defaultProcessor != null) {
            viewBinder.setActionProcessor(defaultProcessor);
        }
        contentView.setTag(KEY_BINDERS, viewBinder);
        return contentView;
    }

    @Override
    public void bind(View view, JSONObject data) {
        bindView(view, data);
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
}
