package com.benny.library.dynamicview.parser.node;

import android.content.Context;
import android.view.View;

import com.benny.library.dynamicview.view.ViewCreator;
import com.benny.library.dynamicview.property.DynamicProperties;
import com.benny.library.dynamicview.view.DynamicViewBuilder;
import com.benny.library.dynamicview.view.DynamicViewBuilderFactory;
import com.benny.library.dynamicview.view.ViewBinder;

public class DynamicViewNode implements ViewCreator {
    private DynamicViewNode parent;

    protected String name;
    protected DynamicProperties properties;

    public DynamicViewNode(String className, DynamicProperties properties) {
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

    public int getIntProperty(String key, int defaultValue) {
        return properties.getInt(key, defaultValue);
    }

    public void addChild(DynamicViewNode child) {
        throw new RuntimeException("View node " + name + " dose not allow add child");
    }

    public View createView(Context context, ViewBinder viewBinder) throws Exception {
        DynamicViewBuilder builder = DynamicViewBuilderFactory.create(context, name);
        viewBinder.add(builder, properties);
        properties.set(builder);
        return builder.getView();
    }
}
