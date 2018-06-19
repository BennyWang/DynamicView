package com.benny.library.dynamicview.parser.node;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.parser.DynamicViewTree;
import com.benny.library.dynamicview.view.ViewBinder;
import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.parser.property.NodeProperties;

import java.util.ArrayList;
import java.util.List;

public class DynamicAdapterViewNode extends DynamicViewNode {
    private DynamicViewNode adapter;

    public DynamicAdapterViewNode(String className, NodeProperties properties) {
        super(className, properties);
    }

    @Override
    public boolean addChild(DynamicNode child) {
        if (!super.addChild(child)) {
            if (adapter != null) {
                throw new RuntimeException("View node " + name + " only allow one child");
            }
            child.setParent(this);
            adapter = (DynamicViewNode) child;
        }
        return true;
    }

    public View createView(Context context, ViewGroup parent, ViewBinder viewBinder) throws Exception {
        View view = super.createView(context, parent, viewBinder);
        if (adapter != null) {
            ((ViewType.AdapterView) view).setInflater(new DynamicViewTree(adapter, viewBinder.getActionProcessor()));
        }
        return view;
    }
}
