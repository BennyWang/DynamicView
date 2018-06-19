package com.benny.library.dynamicview.parser.node;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import com.benny.library.dynamicview.parser.property.NodeProperties;
import com.benny.library.dynamicview.view.DynamicViewBuilder;
import com.benny.library.dynamicview.view.DynamicViewBuilderFactory;
import com.benny.library.dynamicview.view.ViewBinder;

import java.util.ArrayList;
import java.util.List;

public class DynamicViewNode extends DynamicNode {
    private List<DynamicAnimatorNode> animators = new ArrayList<>();

    public DynamicViewNode(String className, NodeProperties properties) {
        super(className, properties);
    }

    @Override
    public boolean addChild(DynamicNode child) {
        if (child instanceof DynamicAnimatorNode) {
            child.setParent(this);
            animators.add((DynamicAnimatorNode) child);
            return true;
        }

        return false;
    }

    public View createView(Context context, ViewGroup parent, ViewBinder viewBinder) throws Exception {
        DynamicViewBuilder builder = DynamicViewBuilderFactory.create(context, name);
        // first add to parent, then set static property, for create correct LayoutParameter
        if (parent != null) {
            parent.addView(builder.getView());
        }

        viewBinder.add(builder, properties);
        viewBinder.addAnimation(createAnimation(builder.getView()));
        properties.set(builder);
        properties.setAction(builder, viewBinder.getActionProcessor());

        return builder.getView();
    }

    private Animation createAnimation(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        for (DynamicAnimatorNode node : animators) {
            animationSet.addAnimation(node.createAnimation());
        }
        view.setAnimation(animationSet);
        return animationSet;
    }
}
