package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.view.ViewCreator;
import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.widget.adapter.GridAdapter;

@DynamicView
public class Grid extends RecyclerView implements ViewType.AdapterView {
    private static final int DEFAULT_SPAN_COUNT = 1;

    private GridAdapter adapter = new GridAdapter();
    private GridLayoutManager layoutManager;

    public Grid(Context context) {
        super(context);

        layoutManager = new GridLayoutManager(context, DEFAULT_SPAN_COUNT, VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getSpanSize(position);
            }
        });

        setNestedScrollingEnabled(false);
        setLayoutManager(layoutManager);
        setAdapter(adapter);
    }

    public void setSpanCount(String spanCount) {
        try {
            layoutManager.setSpanCount(Integer.parseInt(spanCount));
        }
        catch (Exception ignored) {
        }
    }

    @Override
    public void setDataSource(String source) {
        adapter.setDataSource(source);
    }

    @Override
    public void setViewCreator(ViewCreator viewCreator) {
        adapter.setViewCreator(viewCreator);
    }
}
