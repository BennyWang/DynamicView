package com.benny.library.dynamicview.view;

import android.content.Context;
import android.view.View;

public interface ViewCreator {
    View createView(Context context, ViewBinder viewBinder) throws Exception ;
}
