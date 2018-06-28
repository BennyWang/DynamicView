package com.benny.library.dynamicview.view.setter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class WeightSetter {
    public static final String PROPERTY = "weight";
    private static WeightSetter instance;

    public static WeightSetter getInstance() {
        if (instance == null) {
            instance = new WeightSetter();
        }
        return instance;
    }

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
