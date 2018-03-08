package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.widget.RelativeLayout;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.property.GravityProperty;
import com.benny.library.dynamicview.view.ViewType;

@DynamicView
public class RBox extends RelativeLayout implements ViewType.GroupView {
    public RBox(Context context) {
        super(context);
    }

    public void setAlign(String value) {
        GravityProperty property = GravityProperty.of(getContext(), value);
        setGravity(property.gravity);
    }
}
