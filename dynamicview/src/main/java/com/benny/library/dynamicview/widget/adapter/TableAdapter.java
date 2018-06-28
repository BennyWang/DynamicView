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

    public void setDataSource(JSONArray source) {
        dataSource = source;
        notifyDataSetChanged();
    }

    public void setInflater(ViewInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return inflater == null || dataSource.length() == 0 ? 0 : dataSource.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return dataSource.optJSONObject(position);
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
