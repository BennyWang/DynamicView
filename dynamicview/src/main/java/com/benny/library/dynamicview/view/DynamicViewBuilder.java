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

import org.json.JSONArray;
import org.json.JSONException;
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

    public boolean setProperty(String key, Object value) {
        switch (key) {
            case "id":
                view.setId(Integer.parseInt((String) value));
                return true;
            case MarginSetter.PROPERTY:
                marginSetter.setMargin(view, (String) value);
                return true;
            case PaddingSetter.PROPERTY:
                paddingSetter.setPadding(view, (String) value);
                return true;
            case BackgroundSetter.PROPERTY:
                backgroundSetter.setBackground(view, (String) value);
                return true;
            case SizeSetter.PROPERTY:
                sizeSetter.setSize(view, (String) value);
                return true;
            case LayoutGravitySetter.PROPERTY:
                layoutGravitySetter.setGravity(view, (String) value);
                return true;
            case WeightSetter.PROPERTY:
                weightSetter.setWeight(view, (String) value);
                return true;
            default:
                if (relativeSetter.canHandle(key)) {
                    relativeSetter.set(view, key, (String) value);
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

    protected int toInt(Object value) {
        if (value instanceof Number) {
            ((Number) value).intValue();
        }

        return Integer.parseInt(value.toString());
    }

    protected long toLong(Object value) {
        if (value instanceof Number) {
            ((Number) value).intValue();
        }

        return Long.parseLong(value.toString());
    }

    protected double toDouble(Object value) {
        if (value instanceof Number) {
            ((Number) value).intValue();
        }

        return Double.parseDouble(value.toString());
    }

    protected String toString(Object value) {
        return value.toString();
    }

    protected JSONObject toJSONObject(Object value) {
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }

        try {
            return new JSONObject(value.toString());
        }
        catch (JSONException e) {
            return null;
        }
    }

    protected JSONArray toJSONArray(Object value) {
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }

        try {
            return new JSONArray(value.toString());
        }
        catch (JSONException e) {
            return null;
        }
    }
}
