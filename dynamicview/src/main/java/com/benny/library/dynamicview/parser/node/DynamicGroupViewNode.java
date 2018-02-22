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

public class DynamicGroupViewNode extends DynamicViewNode implements ViewCreator {
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
    public View createView(Context context, ViewBinder viewBinder) throws Exception {
        DynamicViewBuilder builder = DynamicViewBuilderFactory.create(context, name);
        viewBinder.add(builder, properties);
        properties.set(builder);

        View view = builder.getView();
        createChildren(context, viewBinder, (ViewType.GroupView) view);
        return view;
    }

    private void createChildren(Context context, ViewBinder viewBinder, ViewType.GroupView groupView) throws Exception {
        for (DynamicViewNode child : children) {
            groupView.addView(child.createView(context, viewBinder));
        }
    }
}
