package com.benny.library.dynamicview.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface ViewInflater {
    View inflate(Context context, ViewGroup parent) throws Exception ;
}
