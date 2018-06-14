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
    private List<DynamicViewNode> children = new ArrayList<>();

    public DynamicAdapterViewNode(String className, NodeProperties properties) {
        super(className, properties);
    }

    @Override
    public void addChild(DynamicViewNode child) {
        if (!children.isEmpty()) {
            throw new RuntimeException("View node " + name + " only allow one child");
        }

        child.setParent(this);
        children.add(child);
    }

    public View createView(Context context, ViewGroup parent, ViewBinder viewBinder) throws Exception {
        View view = super.createView(context, parent, viewBinder);
        if (children.size() > 0) {
            ((ViewType.AdapterView) view).setInflater(new DynamicViewTree(children.get(0), viewBinder.getActionProcessor()));
        }
        return view;
    }
}
