package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.widget.LinearLayout;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.view.property.GravityProperty;

@DynamicView
public class Linear extends LinearLayout implements ViewType.GroupView {
    public Linear(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    public void setOrientation(String orientation) {
        switch (orientation) {
            case "horizontal":
                super.setOrientation(HORIZONTAL);
                break;
            default:
                super.setOrientation(VERTICAL);

        }
    }

    public void setAlign(String value) {
        GravityProperty property = GravityProperty.of(getContext(), value);
        setGravity(property.gravity);
    }
}
