package com.benny.library.dynamicview.parser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

public interface ViewInflater {
    View inflate(Context context, ViewGroup parent) throws Exception ;
    void bind(View view, JSONObject data);
}
