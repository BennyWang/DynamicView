package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.LruCache;
import android.view.View;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.util.PropertyUtils;
import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.view.property.ColorProperty;

@DynamicView
public class Label extends View implements ViewType.View {
    private LayoutManager layoutManager;
    private TextPaint textPaint;
    private String text;
    private int fontSize = 12;

    public static void setLayoutCache(LayoutCache cache) {
        LayoutManager.getInstance().setLayoutCache(cache);
    }

    public Label(Context context) {
        super(context);
        layoutManager = LayoutManager.getInstance();
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(PropertyUtils.spToPx(context, fontSize));
    }

    public void setText(String text) {
        if (!text.equals(this.text)) {
            this.text = text;
            requestLayout();
        }
    }

    public void setFontSize(int size) {
        if (fontSize != size) {
            textPaint.setTextSize(PropertyUtils.spToPx(getContext(), size));
            fontSize = size;
        }
    }

    public void setColor(String value) {
        ColorProperty property = ColorProperty.of(getContext(), value);
        textPaint.setColor(property.getColor());
    }

    public void setStyle(String value) {
        switch (value) {
            case "bold":
                textPaint.setTypeface(Typeface.create(textPaint.getTypeface(), Typeface.BOLD));
                break;
            case "italic":
                textPaint.setTypeface(Typeface.create(textPaint.getTypeface(), Typeface.ITALIC));
                break;
        }
    }

    @Override
    public int getBaseline() {
        Layout layout = layoutManager.layoutFor(getContext(), text, fontSize, Integer.MAX_VALUE, textPaint);
        return layout.getLineBaseline(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        Layout layout = layoutManager.layoutFor(getContext(), text, fontSize, getWidth(), textPaint);
        if (layout != null) {
            canvas.translate(getPaddingLeft(), getPaddingTop());
            layout.draw(canvas);
        }
        canvas.restore();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = Math.min(MeasureSpec.getSize(widthMeasureSpec), (int)textPaint.measureText(text));
        Layout layout = layoutManager.layoutFor(getContext(), text, fontSize, widthSize, textPaint);
        widthSize = layout.getWidth() + getPaddingLeft() + getPaddingRight();

        int heightSize = layout.getHeight() + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthSize, heightSize);
    }

    public interface LayoutCache {
        Layout get(Context context, String key);
        Layout put(Context context, String key, Layout value);
    }

    static class LayoutManager {
        private static LayoutManager instance;
        private LayoutCache cache;

        public static LayoutManager getInstance() {
            if (instance == null) {
                instance = new LayoutManager();
            }
            return instance;
        }

        public void setLayoutCache(LayoutCache cache) {
            this.cache = cache;
        }

        private LayoutCache getLayoutCache() {
            if (cache == null) {
                cache = new LruLayoutCache();
            }
            return cache;
        }

        public Layout layoutFor(Context context, String text, int textSize, int width, TextPaint paint) {
            String key = text + textSize;
            Layout layout = getLayoutCache().get(context, key);
            if (layout != null && layout.getWidth() <= width) {
                return layout;
            }

            layout = new StaticLayout(text, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, true);
            getLayoutCache().put(context, key, layout);
            return layout;

        }

        static class LruLayoutCache implements LayoutCache {
            private LruCache<String, Layout> cache = new LruCache<>(100);
            @Override
            public Layout get(Context context, String key) {
                return cache.get(key);
            }

            @Override
            public Layout put(Context context, String key, Layout value) {
                return cache.put(key, value);
            }
        }
    }
}
