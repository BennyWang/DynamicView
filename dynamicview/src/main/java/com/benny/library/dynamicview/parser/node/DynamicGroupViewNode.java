package com.benny.library.dynamicview.parser.node;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.view.ViewBinder;
import com.benny.library.dynamicview.property.DynamicProperties;

import java.util.ArrayList;
import java.util.List;

public class DynamicGroupViewNode extends DynamicViewNode {
    private List<DynamicViewNode> children = new ArrayList<>();

    public DynamicGroupViewNode(String className, DynamicProperties properties) {
        super(className, properties);
    }

    @Override
    public void addChild(DynamicViewNode child) {
        child.setParent(this);
        children.add(child);
    }

    @Override
    public View createView(Context context, ViewGroup parent, ViewBinder viewBinder) throws Exception {
        View view = super.createView(context, parent, viewBinder);
        createChildren(context, viewBinder, (ViewGroup) view);
        return view;
    }

    private void createChildren(Context context, ViewBinder viewBinder, ViewGroup parent) throws Exception {
        for (DynamicViewNode child : children) {
            child.createView(context, parent, viewBinder);
        }
    }
}
