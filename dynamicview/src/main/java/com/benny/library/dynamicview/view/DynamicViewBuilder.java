package com.benny.library.dynamicview.view;

import android.content.Context;
import android.view.View;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.view.setter.BackgroundSetter;
import com.benny.library.dynamicview.view.setter.LayoutGravitySetter;
import com.benny.library.dynamicview.view.setter.MarginSetter;
import com.benny.library.dynamicview.view.setter.OnClickActionSetter;
import com.benny.library.dynamicview.view.setter.PaddingSetter;
import com.benny.library.dynamicview.view.setter.RelativeSetter;
import com.benny.library.dynamicview.view.setter.SizeSetter;
import com.benny.library.dynamicview.view.setter.WeightSetter;

import org.json.JSONObject;

public abstract class DynamicViewBuilder {
    protected View view;
    private RelativeSetter relativeSetter = new RelativeSetter();
    private MarginSetter marginSetter = new MarginSetter();
    private PaddingSetter paddingSetter = new PaddingSetter();
    private BackgroundSetter backgroundSetter = new BackgroundSetter();
    private SizeSetter sizeSetter = new SizeSetter();
    private LayoutGravitySetter layoutGravitySetter = new LayoutGravitySetter();
    private WeightSetter weightSetter = new WeightSetter();

    private OnClickActionSetter onClickActionSetter = new OnClickActionSetter();

    abstract public void createView(Context context);

    public View getView() {
        return view;
    }

    public boolean setProperty(String key, String value) {
        switch (key) {
            case "id":
                view.setId(Integer.parseInt(value));
                return true;
            case MarginSetter.PROPERTY:
                marginSetter.setMargin(view, value);
                return true;
            case PaddingSetter.PROPERTY:
                paddingSetter.setPadding(view, value);
                return true;
            case BackgroundSetter.PROPERTY:
                backgroundSetter.setBackground(view, value);
                return true;
            case SizeSetter.PROPERTY:
                sizeSetter.setSize(view, value);
                return true;
            case LayoutGravitySetter.PROPERTY:
                layoutGravitySetter.setGravity(view, value);
                return true;
            case WeightSetter.PROPERTY:
                weightSetter.setWeight(view, value);
                return true;
            default:
                if (relativeSetter.canHandle(key)) {
                    relativeSetter.set(view, key, value);
                    return true;
                }
        }
        return false;
    }

    public boolean setAction(String key, String value, ActionProcessor processor, JSONObject data) {
        switch (key) {
            case OnClickActionSetter.PROPERTY:
                onClickActionSetter.setOnClickAction(view, value, data, processor);
                return true;
        }
        return false;
    }
}
