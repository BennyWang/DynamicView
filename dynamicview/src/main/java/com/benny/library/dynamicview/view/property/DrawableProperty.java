package com.benny.library.dynamicview.view.property;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.LruCache;

public class DrawableProperty {
    private static LruCache<String, DrawableProperty> cache = new LruCache<>(10);

    public DrawableMaker drawableMaker;

    public Drawable getDrawable() {
        return drawableMaker == null ? null : drawableMaker.make();
    }

    /**
     *
     * @param context
     * @param value  "#666   xxx xxx xxx xxx"
     *                color  radius
     */
    public static DrawableProperty of(Context context, String value) {
        DrawableProperty property = cache.get(value);
        if (property == null) {
            property = new DrawableProperty(context, value);
            cache.put(value, property);
        }
        return property;
    }

    private DrawableProperty(Context context, String value) {
        int endOfColor = value.indexOf(" ");
        if (endOfColor == -1) {
            drawableMaker = new ColorDrawableMaker(ColorProperty.of(context, value));
        }
        else {
            String color = value.substring(0, endOfColor);
            String radius = value.substring(endOfColor).trim();
            drawableMaker = new ShapeDrawableMaker(RadiusProperty.of(context, radius), ColorProperty.of(context, color));
        }
    }

    private interface DrawableMaker {
        Drawable make();
    }

    private static class ColorDrawableMaker implements DrawableMaker {
        private ColorProperty colorProperty;
        public ColorDrawableMaker(ColorProperty colorProperty) {
            this.colorProperty = colorProperty;
        }

        public Drawable make() {
            return new ColorDrawable(colorProperty.getColor());
        }
    }

    private static class ShapeDrawableMaker implements DrawableMaker {
        private RadiusProperty radius;
        private ColorProperty colorProperty;

        public ShapeDrawableMaker(RadiusProperty radius, ColorProperty colorProperty) {
            this.radius = radius;
            this.colorProperty = colorProperty;
        }

        @Override
        public Drawable make() {
            Shape shape = new RoundRectShape(new float[] { radius.topLeft, radius.topLeft, radius.topRight, radius.topRight,
                    radius.bottomRight, radius.bottomRight, radius.topLeft, radius.bottomLeft }, null, null);
            ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
            shapeDrawable.getPaint().setColor(colorProperty.getColor());
            return shapeDrawable;
        }
    }
}
