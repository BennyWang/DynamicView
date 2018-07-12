package com.benny.library.dynamicview.parser.node;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.parser.property.NodeProperties;
import com.benny.library.dynamicview.view.ViewBinderImpl;

import java.util.ArrayList;
import java.util.List;

public class DynamicGroupViewNode extends DynamicViewNode {
    private List<DynamicViewNode> children = new ArrayList<>();

    public DynamicGroupViewNode(String className, NodeProperties properties) {
        super(className, properties);
    }

    @Override
    public boolean addChild(DynamicNode child) {
        if (!super.addChild(child)) {
            child.setParent(this);
            children.add((DynamicViewNode) child);
        }

        return true;
    }

    @Override
    public View createView(Context context, ViewGroup parent, ViewBinderImpl viewBinder) throws Exception {
        View view = super.createView(context, parent, viewBinder);
        createChildren(context, viewBinder, (ViewGroup) view);
        return view;
    }

    private void createChildren(Context context, ViewBinderImpl viewBinder, ViewGroup parent) throws Exception {
        for (DynamicViewNode child : children) {
            child.createView(context, parent, viewBinder);
        }
    }
}
