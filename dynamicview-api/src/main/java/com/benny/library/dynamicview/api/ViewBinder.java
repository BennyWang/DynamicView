package com.benny.library.dynamicview.api;

import org.json.JSONObject;

public interface ViewBinder {
    void bind(JSONObject data);
    void setActionProcessor(ActionProcessor processor);
}
