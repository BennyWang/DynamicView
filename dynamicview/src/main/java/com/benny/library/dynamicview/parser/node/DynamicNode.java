package com.benny.library.dynamicview.parser.node;

import com.benny.library.dynamicview.parser.property.NodeProperties;

public abstract class DynamicNode {
    private DynamicViewNode parent;

    protected String name;
    protected NodeProperties properties;

    public DynamicNode(String className, NodeProperties properties) {
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

    public float getFloatProperty(String key, float fallback) {
        String prop = properties.get(key);
        try {
            return Float.parseFloat(prop);
        }
        catch (Exception e) {
            return fallback;
        }
    }

    public int getIntProperty(String key, int fallback) {
        String prop = properties.get(key);
        try {
            return Integer.parseInt(prop);
        }
        catch (Exception e) {
            return fallback;
        }
    }

    public long getLongProperty(String key, long fallback) {
        String prop = properties.get(key);
        try {
            return Long.parseLong(prop);
        }
        catch (Exception e) {
            return fallback;
        }
    }

    public boolean getBooleanProperty(String key, boolean fallback) {
        String prop = properties.get(key);
        try {
            return Boolean.parseBoolean(prop);
        }
        catch (Exception e) {
            return fallback;
        }
    }

    public abstract boolean addChild(DynamicNode child);
}
