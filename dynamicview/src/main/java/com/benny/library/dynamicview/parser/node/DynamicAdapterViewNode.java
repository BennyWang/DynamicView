package com.benny.library.dynamicview.parser.node;

import android.content.Context;
import android.view.View;

import com.benny.library.dynamicview.view.ViewBinder;
import com.benny.library.dynamicview.view.ViewCreator;
import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.property.DynamicProperties;
import com.benny.library.dynamicview.view.DynamicViewBuilder;
import com.benny.library.dynamicview.view.DynamicViewBuilderFactory;

import java.util.ArrayList;
import java.util.List;

public class DynamicAdapterViewNode extends DynamicViewNode implements ViewCreator {
    private List<DynamicViewNode> children = new ArrayList<>();

    public DynamicAdapterViewNode(String className, DynamicProperties properties) {
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

    @Override
    public View createView(Context context, ViewBinder viewBinder) throws Exception {
        DynamicViewBuilder builder = DynamicViewBuilderFactory.create(context, name);
        viewBinder.add(builder, properties);
        properties.set(builder);

        View view = builder.getView();
        if (children.size() > 0) {
            ((ViewType.AdapterView) view).setViewCreator(children.get(0));
        }
        return view;
    }
}
