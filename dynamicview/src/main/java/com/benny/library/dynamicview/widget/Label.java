package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.view.View;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.util.PropertyUtils;
import com.benny.library.dynamicview.view.ThemeManager;
import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.util.ViewUtils;

@DynamicView
public class Label extends View implements ViewType.View {
    private TextPaint textPaint;
    private String text;
    private int fontSize = 12;

    public Label(Context context) {
        super(context);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(PropertyUtils.spToPx(context, fontSize));
        setWillNotDraw(false);
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
        int color = ViewUtils.getThemeColor(getContext(), ViewUtils.getThemeId(this));
        if (color > 0) {
            textPaint.setColor(color);
        }
        else {
            textPaint.setColor(Color.parseColor(value));
        }
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        Layout layout = LayoutManager.getInstance().layoutFor(text, fontSize, getWidth(), textPaint);
        if (layout != null) {
            canvas.translate(getPaddingLeft(), getPaddingTop());
            layout.draw(canvas);
        }
        canvas.restore();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        Layout layout = LayoutManager.getInstance().layoutFor(text, fontSize, widthSize, textPaint);
        widthSize = layout.getWidth() + getPaddingLeft() + getPaddingRight();
        int heightSize = layout.getHeight() + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthSize, heightSize);
    }
}
