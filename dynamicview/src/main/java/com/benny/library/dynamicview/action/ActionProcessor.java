package com.benny.library.dynamicview.action;

import android.view.View;

public interface ActionProcessor {
    void processAction(View view, String tag, Object... data);
}
