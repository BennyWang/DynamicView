package com.benny.library.dynamicview.property;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.LruCache;

public class BackgroundProperty {
    private static LruCache<String, BackgroundProperty> cache = new LruCache<>(10);

    public Drawable background;

    /**
     *
     * @param context
     * @param value  "#666   xxx xxx xxx xxx"
     *                color  radius
     */
    public static BackgroundProperty of(Context context, String value) {
        BackgroundProperty property = cache.get(value);
        if (property == null) {
            property = new BackgroundProperty(context, value);
            cache.put(value, property);
        }
        return property;
    }

    private BackgroundProperty(Context context, String value) {
        int endOfColor = value.indexOf(" ");
        if (endOfColor == -1) {
            background = new ColorDrawable(Color.parseColor(value));
        }
        else {
            String color = value.substring(0, endOfColor);
            ShapeDrawable shapeDrawable = new ShapeDrawable(parseRadius(context, value.substring(endOfColor)));
            shapeDrawable.getPaint().setColor(Color.parseColor(color));
            background = shapeDrawable;
        }
    }

    protected RoundRectShape parseRadius(Context context, String value) {
        RadiusProperty property = RadiusProperty.of(context, value);
        return new RoundRectShape(new float[] { property.topLeft, property.topLeft, property.topRight, property.topRight,
                                                property.bottomRight, property.bottomRight, property.topLeft, property.bottomLeft }, null, null);
    }
}
