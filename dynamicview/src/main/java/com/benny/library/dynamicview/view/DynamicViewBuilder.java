package com.benny.library.dynamicview.view;

import android.content.Context;
import android.view.View;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.util.ViewUtils;
import com.benny.library.dynamicview.view.setter.BackgroundSetter;
import com.benny.library.dynamicview.view.setter.LayoutGravitySetter;
import com.benny.library.dynamicview.view.setter.MarginSetter;
import com.benny.library.dynamicview.view.setter.OnClickActionSetter;
import com.benny.library.dynamicview.view.setter.PaddingSetter;
import com.benny.library.dynamicview.view.setter.RelativeSetter;
import com.benny.library.dynamicview.view.setter.SizeSetter;
import com.benny.library.dynamicview.view.setter.VisibilitySetter;
import com.benny.library.dynamicview.view.setter.WeightSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class DynamicViewBuilder {
    private static RelativeSetter relativeSetter = new RelativeSetter();
    private static MarginSetter marginSetter = new MarginSetter();
    private static PaddingSetter paddingSetter = new PaddingSetter();
    private static BackgroundSetter backgroundSetter = new BackgroundSetter();
    private static SizeSetter sizeSetter = new SizeSetter();
    private static LayoutGravitySetter layoutGravitySetter = new LayoutGravitySetter();
    private static WeightSetter weightSetter = new WeightSetter();
    private static VisibilitySetter visibilitySetter = new VisibilitySetter();
    private static OnClickActionSetter onClickActionSetter = new OnClickActionSetter();

    protected View view;

    abstract public void createView(Context context);

    public View getView() {
        return view;
    }

    public boolean setProperty(String key, Object value) {
        switch (key) {
            case "id":
                view.setId(Integer.parseInt((String) value));
                return true;
            case "themeId":
                ViewUtils.setThemeId(view, (String) value);
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
            case VisibilitySetter.PROPERTY:
                visibilitySetter.setVisibility(view, (String) value);
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

    protected float toFloat(Object value) {
        if (value instanceof Number) {
            ((Number) value).floatValue();
        }

        return Float.parseFloat(value.toString());
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
