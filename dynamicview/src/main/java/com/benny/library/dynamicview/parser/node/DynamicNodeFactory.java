package com.benny.library.dynamicview.parser.node;

import com.benny.library.dynamicview.parser.property.NodeProperties;
import com.benny.library.dynamicview.view.DynamicViewBuilderFactory;
import com.benny.library.dynamicview.view.ViewType;

import java.util.HashMap;
import java.util.Map;

public class DynamicNodeFactory {
    private static Map<Class, Class> viewTypeMap = new HashMap<>();

    public static DynamicNode create(String name, NodeProperties properties) throws Exception {
        if (name.equals("animator")) {
            return new DynamicAnimatorNode(name, properties);
        }

        Class<?> clazz = DynamicViewBuilderFactory.register(name);
        Class<?> viewType = getViewType(clazz);
        if (viewType == ViewType.View.class) {
            return new DynamicViewNode(name, properties);
        }
        else if (viewType == ViewType.GroupView.class) {
            return new DynamicGroupViewNode(name, properties);
        }
        else if (viewType == ViewType.AdapterView.class) {
            return new DynamicAdapterViewNode(name, properties);
        }
        throw new Exception("Unknown node " + name);
    }

    private static Class<?> getViewType(Class<?> clazz) {
        Class<?> viewType = viewTypeMap.get(clazz);
        if (viewType != null) {
            return viewType;
        }

        if (ViewType.GroupView.class.isAssignableFrom(clazz)) {
            viewTypeMap.put(clazz, ViewType.GroupView.class);
        }
        else if (ViewType.AdapterView.class.isAssignableFrom(clazz)) {
            viewTypeMap.put(clazz, ViewType.AdapterView.class);
        }
        else if (ViewType.View.class.isAssignableFrom(clazz)) {
            viewTypeMap.put(clazz, ViewType.View.class);
        }
        return viewTypeMap.get(clazz);
    }
}
