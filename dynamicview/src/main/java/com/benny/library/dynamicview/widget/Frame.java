package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.ViewType;

@DynamicView
public class Frame extends FrameLayout implements ViewType.GroupView {
    private float ratio = 0;

    public Frame(@NonNull Context context) {
        super(context);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (ratio > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);

            if (widthMode != MeasureSpec.UNSPECIFIED && heightMode != MeasureSpec.EXACTLY) {
                int contentWidth = width - getPaddingLeft() - getPaddingRight();
                int contentHeight = (int) (contentWidth / ratio);
                height = contentHeight + getPaddingBottom() + getPaddingTop();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            } else if (height != MeasureSpec.UNSPECIFIED && width != MeasureSpec.EXACTLY) {
                int contentHeight = height - getPaddingTop() - getPaddingBottom();
                int contentWidth = (int) (contentHeight * ratio);
                width = contentWidth + getPaddingLeft() + getPaddingRight();
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
