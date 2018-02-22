package com.benny.library.dynamicview.view;

import android.view.View;

import com.benny.library.dynamicview.setter.BackgroundSetter;
import com.benny.library.dynamicview.setter.MarginSetter;
import com.benny.library.dynamicview.setter.PaddingSetter;

public abstract class DynamicViewBuilder {
    protected View view;
    private MarginSetter marginSetter = new MarginSetter();
    private PaddingSetter paddingSetter = new PaddingSetter();
    private BackgroundSetter backgroundSetter = new BackgroundSetter();

    public boolean setProperty(String key, String value) {
        switch (key) {
            case MarginSetter.PROPERTY:
                marginSetter.setMargin(view, value);
                return true;
            case PaddingSetter.PROPERTY:
                paddingSetter.setPadding(view, value);
                return true;
            case BackgroundSetter.PROPERTY:
                backgroundSetter.setBackground(view, value);
                return true;
            default:
                return false;
        }
    }

    public View getView() {
        return view;
    }
}
