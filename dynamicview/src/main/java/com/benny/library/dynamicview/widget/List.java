package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.parser.ViewInflater;
import com.benny.library.dynamicview.util.PropertyUtils;
import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.view.property.ColorProperty;
import com.benny.library.dynamicview.view.property.MarginProperty;
import com.benny.library.dynamicview.widget.adapter.TableAdapter;

import org.json.JSONArray;

import java.util.LinkedList;

@DynamicView
public class List extends LinearLayout implements ViewType.AdapterView {
    private LinkedList<View> cacheViews = new LinkedList<>();
    private TableAdapter adapter = new TableAdapter();

    private DividerMaker dividerMaker;

    public List(Context context) {
        super(context);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                reLayout();
            }
        });
    }

    public void setOrientation(String orientation) {
        switch (orientation) {
            case "horizontal":
                super.setOrientation(HORIZONTAL);
                break;
            default:
                super.setOrientation(VERTICAL);
        }
    }

    public void setDividerHeight(int height) {
        getDividerMaker().setDividerHeight(height);
    }

    public void setDividerColor(String color) {
        getDividerMaker().setDividerColor(color);
    }

    public void setDividerMargin(String margin) {
        MarginProperty property = MarginProperty.of(getContext(), margin);
        getDividerMaker().setMargin(property.left, property.top, property.right, property.bottom);
    }

    @Override
    public void setDataSource(JSONArray source) {
        adapter.setDataSource(source);
    }

    private DividerMaker getDividerMaker() {
        if (dividerMaker == null) {
            dividerMaker = new DividerMaker(getOrientation());
        }
        return dividerMaker;
    }

    @Override
    public void setInflater(ViewInflater inflater) {
        adapter.setInflater(inflater);
    }

    @Override
    public void setRange(String range) {
        adapter.setRange(range);
    }

    private void reLayout() {
        int viewCount = getChildCount();
        int sourceSize = adapter.getCount();
        int index = 0;
        int sourceIndex = 0;
        while (true) {
            View child = getChildAt(index);
            boolean cacheChild = child != null;
            if (child == null && !cacheViews.isEmpty()) {
                child = cacheViews.poll();
            }

            child = adapter.getView(sourceIndex++, child, this);
            if (!cacheChild) {
                addViewInLayout(child, index, child.getLayoutParams(), true);
            }

            ++index;
            if (sourceIndex == sourceSize) {
                break;
            }

            if (dividerMaker != null) {
                child = getChildAt(index);
                if (child == null) {
                    child = dividerMaker.createDivider(getContext());
                    child.setTag(Boolean.TRUE);
                    addViewInLayout(child, index, child.getLayoutParams(), true);
                }
                ++index;
            }
        }

        for (int i = viewCount - 1; i >= index; --i) {
            View view = getChildAt(i);
            if (view.getTag() == Boolean.TRUE) {
                dividerMaker.recycleDivider(view);
            } else {
                cacheViews.add(view);
            }
            removeViewsInLayout(i, 1);
        }

        requestLayout();
        invalidate();
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp == null) {
            //if (getOrientation() == VERTICAL) {
            //    return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //}
            return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        else if (lp instanceof LayoutParams) {
            return (LayoutParams) lp;
        }

        return super.generateLayoutParams(lp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public static class DividerMaker {
        private LinkedList<View> cacheViews = new LinkedList<>();
        private int orientation;
        private int dividerHeight = 0;
        private String dividerColor;
        private int marginLeft = 0;
        private int marginTop = 0;
        private int marginRight = 0;
        private int marginBottom = 0;

        public DividerMaker(int orientation) {
            this.orientation = orientation;
        }

        public void setDividerHeight(int height) {
            dividerHeight = height;
        }

        public void setDividerColor(String color) {
            dividerColor = color;
        }

        public void setMargin(int left, int top, int right, int bottom) {
            marginLeft = left;
            marginTop = top;
            marginRight = right;
            marginBottom = bottom;
        }

        public View createDivider(Context context) {
            if (!cacheViews.isEmpty()) {
                return cacheViews.poll();
            }

            View divider = new View(context);
            if (dividerColor == null) {
                divider.setBackgroundColor(Color.BLACK);
            }
            else {
                divider.setBackgroundColor(ColorProperty.of(context, dividerColor).getColor());
            }
            divider.setLayoutParams(createParams(context));
            return divider;
        }

        public void recycleDivider(View divider) {
            cacheViews.offer(divider);
        }

        private LayoutParams createParams(Context context) {
            int widthInPx = PropertyUtils.dpToPx(context, dividerHeight);
            LayoutParams params;
            if (orientation == VERTICAL) {
                params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, widthInPx);
            }
            else {
                params = new LayoutParams(widthInPx, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            params.leftMargin = marginLeft;
            params.topMargin = marginTop;
            params.rightMargin = marginRight;
            params.bottomMargin = marginBottom;
            return params;
        }
    }
}
