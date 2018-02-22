package com.benny.library.dynamicview.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface ViewCreator {
    View createView(Context context, ViewGroup parent, ViewBinder viewBinder) throws Exception ;
}
