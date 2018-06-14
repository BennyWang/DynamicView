package com.benny.library.dynamicview.action;

import android.view.View;

import org.json.JSONObject;

public interface ActionProcessor {
    void processAction(View view, String tag, JSONObject data);
}
