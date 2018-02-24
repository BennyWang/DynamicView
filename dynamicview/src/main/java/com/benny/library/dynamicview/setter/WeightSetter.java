package com.benny.library.dynamicview.setter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class WeightSetter {
    public static final String PROPERTY = "weight";

    public void setWeight(View view, String weight) {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();
        if (lparams == null) {
            lparams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            view.setLayoutParams(lparams);
        }

        if (lparams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) lparams).weight = Integer.parseInt(weight);
        }
    }
}
