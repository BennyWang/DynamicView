package com.benny.library.dynamicview.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicViewTree {
    private static final int KEY_BINDERS = 1 + 2 << 24;
    private DynamicViewNode root;

    public DynamicViewTree(DynamicViewNode root) {
        this.root = root;
    }

    public DynamicViewNode getRoot() {
        return root;
    }

    public View createView(Context context) throws Exception {
        List<ViewBinder> binders = new ArrayList<>();
        View contentView = createView(context, root, binders);
        contentView.setTag(KEY_BINDERS, binders);
        return contentView;
    }

    public static void bindView(View view, JSONObject data) {
        try {
            List<ViewBinder> binders = (List<ViewBinder>) view.getTag(KEY_BINDERS);
            if (binders == null) {
                return;
            }
            for (ViewBinder binder : binders) {
                binder.bindView(data);
            }
        }
        catch (Exception ignored) {
        }
    }

    public static void bindView(View view, Map<String, String> data) {
        try {
            List<ViewBinder> binders = (List<ViewBinder>) view.getTag(KEY_BINDERS);
            if (binders == null) {
                return;
            }
            for (ViewBinder binder : binders) {
                binder.bindView(data);
            }
        }
        catch (Exception ignored) {
        }
    }

    private View createView(Context context, DynamicViewNode node, List<ViewBinder> binders) throws Exception {
        View contentView = node.createView(context, binders);

        List<DynamicViewNode> children = node.getChildren();
        for (DynamicViewNode child : children) {
            View childView = createView(context, child, binders);
            if (childView != null) {
                ((ViewGroup) contentView).addView(childView);
            }
        }
        return contentView;
    }
}
