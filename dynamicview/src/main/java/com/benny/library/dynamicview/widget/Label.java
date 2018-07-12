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

import com.benny.library.dynamicview.LifecycleCacheManager;
import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.util.PropertyUtils;
import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.view.property.ColorProperty;

@DynamicView
public class Label extends View implements ViewType.View {
    private static final String LAYOUT_CACHE = "LABEL_LAYOUT";
    private TextPaint textPaint;
    private String text;
    private ColorProperty textColor;
    private int defaultTextColor;
    private int fontSize = 12;
    private Paint.Align align = Paint.Align.LEFT;
    private Typeface typeface;
    private int maxLines = 0;

    private LruCache<String, Layout> layoutCache;

    public Label(Context context) {
        super(context);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(PropertyUtils.spToPx(context, fontSize));
        defaultTextColor = textPaint.getColor();

        layoutCache = LifecycleCacheManager.getInstance().makeCache(getContext(), LAYOUT_CACHE, Layout.class);
    }

    public void setText(String text) {
        if (!text.equals(this.text)) {
            this.text = text;
            requestLayout();
        }
    }

    public void setMaxLines(int lines) {
        maxLines = lines;
    }

    public void setFontSize(int size) {
        if (fontSize != size) {
            textPaint.setTextSize(PropertyUtils.spToPx(getContext(), size));
            fontSize = size;
        }
    }

    public void setColor(String value) {
        textColor = ColorProperty.of(getContext(), value);
    }

    public void setStyle(String value) {
        switch (value) {
            case "bold":
                typeface = Typeface.create(typeface, Typeface.BOLD);
                break;
            case "italic":
                typeface = Typeface.create(typeface, Typeface.ITALIC);
                break;
        }
    }

    public void setFontFamily(String value) {
        typeface = Typeface.create(value, Typeface.NORMAL);
    }

    public void setAlign(String value) {
        switch (value) {
            case "center":
                align = Paint.Align.CENTER;
                break;
            case "right":
                align = Paint.Align.RIGHT;
            default:
                align = Paint.Align.LEFT;
        }
    }

    @Override
    public int getBaseline() {
        Layout layout = layoutFor(text, maxLines, fontSize, Integer.MAX_VALUE, textPaint);
        return layout.getLineBaseline(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        Layout layout = layoutFor(text, maxLines, fontSize, getWidth(), textPaint);
        if (layout != null) {
            if (align == Paint.Align.CENTER) {
                int left = getWidth() / 2;
                int top = (getHeight() - layout.getHeight()) / 2;
                canvas.translate(left, top);
            }
            else {
                canvas.translate(getPaddingLeft(), getPaddingTop());
            }
            layout.getPaint().setTypeface(typeface);
            layout.getPaint().setTextAlign(align);
            layout.getPaint().setColor(textColor != null ? textColor.getColor() : defaultTextColor);
            layout.draw(canvas);
        }
        canvas.restore();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize;

        Layout layout;
        if (widthMode != MeasureSpec.UNSPECIFIED) {
            widthSize = MeasureSpec.getSize(widthMeasureSpec);
        }
        else {
            widthSize = Math.min(MeasureSpec.getSize(widthMeasureSpec), (int) textPaint.measureText(text));
        }

        layout = layoutFor(text, maxLines, fontSize, widthSize, textPaint);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = layout.getWidth() + getPaddingLeft() + getPaddingRight();
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize;
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = layout.getHeight() + getPaddingTop() + getPaddingBottom();
        }
        else {
            heightSize = MeasureSpec.getSize(widthMeasureSpec);
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    public Layout layoutFor(String text, int maxLines, int textSize, int width, TextPaint paint) {
        String key = text + textSize + maxLines;
        Layout layout = layoutCache.get(key);
        if (layout == null || layout.getWidth() > width) {
            layout = new StaticLayout(text, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, true);
            int lineCount = layout.getLineCount();
            if (maxLines != 0 && lineCount > maxLines) {
                int offset = layout.getLineEnd(maxLines - 1);
                layout = new StaticLayout(text.substring(0, offset - 1) + "…", paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, true);
            }
            layoutCache.put(key, layout);
        }
        return layout;
    }
}
