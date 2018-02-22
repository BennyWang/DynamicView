package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.property.GravityProperty;
import com.benny.library.dynamicview.view.ViewType;

@DynamicView
public class VBox extends LinearLayout implements ViewType.GroupView {
    public VBox(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public void setAlign(String value) {
        GravityProperty property = GravityProperty.of(getContext(), value);
        setGravity(property.gravity);
    }
}
