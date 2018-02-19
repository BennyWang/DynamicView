package com.benny.library.dynamicview.view;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.benny.library.dynamicview.property.DynamicProperties;

import java.util.ArrayList;
import java.util.List;

public class DynamicViewNode {
    private DynamicViewNode parent;
    private List<DynamicViewNode> children = new ArrayList<>();

    private String name;
    private DynamicProperties properties;

    public DynamicViewNode(String className, DynamicProperties properties) {
        this.name = className;
        this.properties = properties;
        DynamicViewBuilderFactory.register(name);
    }

    public boolean isRoot() {
        return parent == null;
    }

    private void setParent(DynamicViewNode parent) {
        this.parent = parent;
    }

    public DynamicViewNode getParent() {
        return parent;
    }

    public List<DynamicViewNode> getChildren() {
        return children;
    }

    public DynamicProperties getProperties() {
        return properties;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public String getName() {
        return name;
    }

    public void addChild(DynamicViewNode child) {
        child.setParent(this);
        children.add(child);
    }

    public View createView(Context context, List<ViewBinder> binders) throws Exception {
        DynamicViewBuilder builder = DynamicViewBuilderFactory.create(context, name);
        properties.set(builder);
        ViewBinder binder = new ViewBinder(builder, properties);
        binders.add(binder);
        return builder.getView();
    }
}
