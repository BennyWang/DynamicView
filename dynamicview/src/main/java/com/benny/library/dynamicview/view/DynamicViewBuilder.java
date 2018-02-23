package com.benny.library.dynamicview.view;

import android.view.View;

import com.benny.library.dynamicview.setter.BackgroundSetter;
import com.benny.library.dynamicview.setter.LayoutGravitySetter;
import com.benny.library.dynamicview.setter.MarginSetter;
import com.benny.library.dynamicview.setter.PaddingSetter;
import com.benny.library.dynamicview.setter.SizeSetter;
import com.benny.library.dynamicview.setter.WeightSetter;

public abstract class DynamicViewBuilder {
    protected View view;
    private MarginSetter marginSetter = new MarginSetter();
    private PaddingSetter paddingSetter = new PaddingSetter();
    private BackgroundSetter backgroundSetter = new BackgroundSetter();
    private SizeSetter sizeSetter = new SizeSetter();
    private LayoutGravitySetter layoutGravitySetter = new LayoutGravitySetter();
    private WeightSetter weightSetter = new WeightSetter();

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
            case SizeSetter.PROPERTY_WIDTH:
                sizeSetter.setSize(view, value, null);
                return true;
            case SizeSetter.PROPERTY_HEIGHT:
                sizeSetter.setSize(view, null, value);
                return true;
            case LayoutGravitySetter.PROPERTY:
                layoutGravitySetter.setGravity(view, value);
            case WeightSetter.PROPERTY:
                weightSetter.setWeight(view, value);
                return true;
            default:
                return false;
        }
    }

    public View getView() {
        return view;
    }
}
