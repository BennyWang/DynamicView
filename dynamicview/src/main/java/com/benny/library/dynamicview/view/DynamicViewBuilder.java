package com.benny.library.dynamicview.view;

import android.content.Context;
import android.view.View;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.view.setter.BackgroundSetter;
import com.benny.library.dynamicview.view.setter.LayoutGravitySetter;
import com.benny.library.dynamicview.view.setter.MarginSetter;
import com.benny.library.dynamicview.view.setter.OnClickActionSetter;
import com.benny.library.dynamicview.view.setter.OnLongClickActionSetter;
import com.benny.library.dynamicview.view.setter.PaddingSetter;
import com.benny.library.dynamicview.view.setter.RelativeSetter;
import com.benny.library.dynamicview.view.setter.SizeSetter;
import com.benny.library.dynamicview.view.setter.VisibilitySetter;
import com.benny.library.dynamicview.view.setter.WeightSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class DynamicViewBuilder {
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
            case MarginSetter.PROPERTY:
                MarginSetter.getInstance().setMargin(view, (String) value);
                return true;
            case PaddingSetter.PROPERTY:
                PaddingSetter.getInstance().setPadding(view, (String) value);
                return true;
            case BackgroundSetter.PROPERTY:
                BackgroundSetter.getInstance().setBackground(view, (String) value);
                return true;
            case SizeSetter.PROPERTY:
                SizeSetter.getInstance().setSize(view, (String) value);
                return true;
            case LayoutGravitySetter.PROPERTY:
                LayoutGravitySetter.getInstance().setGravity(view, (String) value);
                return true;
            case WeightSetter.PROPERTY:
                WeightSetter.getInstance().setWeight(view, (String) value);
                return true;
            case VisibilitySetter.PROPERTY:
                VisibilitySetter.getInstance().setVisibility(view, (String) value);
                return true;
            default:
                if (RelativeSetter.canHandle(key)) {
                    RelativeSetter.getInstance().set(view, key, (String) value);
                    return true;
                }
        }
        return false;
    }

    public boolean setAction(String key, String value, ActionProcessor processor, JSONObject data) {
        switch (key) {
            case OnClickActionSetter.PROPERTY:
                OnClickActionSetter.getInstance().setOnClickAction(view, value, data, processor);
                return true;
            case OnLongClickActionSetter.PROPERTY:
                OnLongClickActionSetter.getInstance().setOnLongClickAction(view, value, data, processor);
                break;
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
