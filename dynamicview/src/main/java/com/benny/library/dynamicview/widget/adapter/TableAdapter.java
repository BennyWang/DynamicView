package com.benny.library.dynamicview.widget.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.benny.library.dynamicview.parser.ViewInflater;

import org.json.JSONArray;
import org.json.JSONObject;

public class TableAdapter extends BaseAdapter {
    private JSONArray dataSource = new JSONArray();
    private ViewInflater inflater;
    private int start = 0;
    private int end = -1;

    public void setDataSource(JSONArray source) {
        if (dataSource != source) {
            dataSource = source;
            notifyDataSetChanged();
        }
    }

    public void setRange(String range) {
        try {
            int index = range.indexOf("..");
            if (index == -1) {
                start = Integer.parseInt(range);
                end = -1;
            }
            else {
                start = Integer.parseInt(range.substring(0, index));
                end = Integer.parseInt(range.substring(index + 2));
            }
        }
        catch (Exception e) {
            Log.e("DynamicViewEngine", "GridAdapter.setRange exception: " + Log.getStackTraceString(e));
        }
    }

    public void setInflater(ViewInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        if (inflater == null || dataSource.length() == 0) {
            return 0;
        }

        int skip = (end == -1) ? start : end - start;
        return Math.max(dataSource.length() - skip, 0);
    }

    @Override
    public JSONObject getItem(int position) {
        return dataSource.optJSONObject(position + start);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = inflater.inflate(parent.getContext(), null);
            }

            inflater.bind(convertView, getItem(position));
            return convertView;
        }
        catch (Exception e) {
            Log.e("TableAdapter", "create view exception: " + Log.getStackTraceString(e));
            return null;
        }
    }
}
