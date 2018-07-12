package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.widget.RelativeLayout;

import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.view.property.GravityProperty;

@com.benny.library.dynamicview.annotations.DynamicView
public class Relative extends RelativeLayout implements ViewType.GroupView {
    public Relative(Context context) {
        super(context);
    }

    public void setAlign(String value) {
        GravityProperty property = GravityProperty.of(getContext(), value);
        setGravity(property.gravity);
    }
}
