package com.benny.library.dynamicview.parser.node;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.parser.property.NodeProperties;
import com.benny.library.dynamicview.view.DynamicViewBuilder;
import com.benny.library.dynamicview.view.DynamicViewBuilderFactory;
import com.benny.library.dynamicview.view.ViewBinder;

public class DynamicViewNode {
    private DynamicViewNode parent;

    protected String name;
    protected NodeProperties properties;

    public DynamicViewNode(String className, NodeProperties properties) {
        this.name = className;
        this.properties = properties;
    }

    public boolean isRoot() {
        return parent == null;
    }

    protected void setParent(DynamicViewNode parent) {
        this.parent = parent;
    }

    public DynamicViewNode getParent() {
        return parent;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void addChild(DynamicViewNode child) {
        throw new RuntimeException("View node " + name + " dose not allow add child");
    }

    public View createView(Context context, ViewGroup parent, ViewBinder viewBinder) throws Exception {
        DynamicViewBuilder builder = DynamicViewBuilderFactory.create(context, name);
        // first add to parent, then set static property, for create correct LayoutParameter
        if (parent != null) {
            parent.addView(builder.getView());
        }

        viewBinder.add(builder, properties);
        properties.set(builder);
        properties.setAction(builder, viewBinder.getActionProcessor());

        return builder.getView();
    }
}
