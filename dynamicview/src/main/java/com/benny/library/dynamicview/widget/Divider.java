package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.view.View;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.ViewType;

@DynamicView
public class Divider extends View implements ViewType.View {
    public Divider(Context context) {
        super(context);
    }
}
