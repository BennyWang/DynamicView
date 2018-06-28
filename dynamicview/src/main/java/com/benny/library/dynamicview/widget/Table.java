package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.widget.GridView;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.ViewInflater;
import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.widget.adapter.TableAdapter;

import org.json.JSONArray;

@DynamicView
public class Table extends GridView implements ViewType.AdapterView {
    private TableAdapter adapter = new TableAdapter();

    public Table(Context context) {
        super(context);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        setAdapter(adapter);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }

    public void setVerticalSpacing(int size) {
        super.setVerticalSpacing(size);
    }

    public void setHorizontalSpacing(int size) {
        super.setHorizontalSpacing(size);
    }

    public void setSpanCount(int spanCount) {
        setNumColumns(spanCount);
    }

    @Override
    public void setDataSource(JSONArray source) {
        adapter.setDataSource(source);
    }

    @Override
    public void setInflater(ViewInflater inflater) {
        adapter.setInflater(inflater);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
        int heightSpec;
        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        else {
            heightSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
