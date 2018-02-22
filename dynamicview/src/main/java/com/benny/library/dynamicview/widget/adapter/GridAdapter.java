package com.benny.library.dynamicview.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.view.ViewBinder;
import com.benny.library.dynamicview.view.ViewCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private JSONArray dataSource = new JSONArray();
    private ViewCreator viewCreator;

    public void setDataSource(String source) {
        try {
            dataSource = new JSONArray(source);
        }
        catch (JSONException ignored) {
        }
    }

    public void setViewCreator(ViewCreator viewCreator) {
        this.viewCreator = viewCreator;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            ViewBinder viewBinder = new ViewBinder();
            View view = viewCreator.createView(parent.getContext(), parent, viewBinder);
            view.setTag(viewBinder);
            return new ViewHolder(view);
        }
        catch (Exception e) {
            Log.e("GridAdapter", "create view exception: " + e);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).notifyDataChange(getItem(position));
    }

    @Override
    public int getItemCount() {
        return viewCreator == null || dataSource.length() == 0 ? 0 : dataSource.length();
    }

    public JSONObject getItem(int position) {
        return dataSource.optJSONObject(position);
    }

    public int getSpanSize(int position) {
        return getItem(position).optInt("colspan", 1);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View itemView) {
            super(itemView);
        }

        void notifyDataChange(JSONObject data) {
            ((ViewBinder)itemView.getTag()).bind(data);
        }
    }
}
