package com.benny.library.dynamicview.parser.node;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.benny.library.dynamicview.parser.property.NodeProperties;

public class DynamicAnimatorNode extends DynamicNode {
    public DynamicAnimatorNode(String className, NodeProperties properties) {
        super(className, properties);
    }

    @Override
    public boolean addChild(DynamicNode child) {
        throw new RuntimeException("animator node dose not allow add child");
    }

    public Animation createAnimation() {
        Animation animation = null;
        String type = getProperty("type");
        switch (type) {
            case "alpha":
                animation = alphaAnimation();
                break;
            case "scale":
                animation = scaleAnimation();
                break;
            case "translate":
                animation = translateAnimation();
                break;
            case "rotate":
                animation = rotateAnimation();
        }

        if (animation != null) {
            animation.setDuration(getLongProperty("duration", 200));
            animation.setRepeatCount(getIntProperty("repeatCount", 0));
            animation.setRepeatMode(getIntProperty("repeatMode", 1));
        }
        return animation;
    }

    private Animation alphaAnimation() {
        float fromAlpha = getFloatProperty("fromAlpha", 0);
        float toAlpha = getFloatProperty("toAlpha", 1);
        return new AlphaAnimation(fromAlpha, toAlpha);
    }

    private Animation scaleAnimation() {
        float fromX = getFloatProperty("fromX", 0);
        float toX = getFloatProperty("toX", 1);
        float fromY = getFloatProperty("fromY", 0);
        float toY = getFloatProperty("toY", 1);
        float pivotX = getFloatProperty("pivotX", 0.5f);
        float pivotY = getFloatProperty("pivotX", 0.5f);
        return new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY);
    }

    private Animation translateAnimation() {
        float fromX = getFloatProperty("fromX", 0);
        float toX = getFloatProperty("toX", 1);
        float fromY = getFloatProperty("fromY", 0);
        float toY = getFloatProperty("toY", 0);
        return new TranslateAnimation(Animation.RELATIVE_TO_PARENT, fromX, Animation.RELATIVE_TO_PARENT, toX, Animation.RELATIVE_TO_PARENT, fromY, Animation.RELATIVE_TO_PARENT, toY);
    }

    private Animation rotateAnimation() {
        float fromX = getFloatProperty("from", 0);
        float toX = getFloatProperty("to", 1);
        float pivotX = getFloatProperty("pivotX", 0.5f);
        float pivotY = getFloatProperty("pivotX", 0.5f);

        return new RotateAnimation(fromX, toX, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY);
    }
}
