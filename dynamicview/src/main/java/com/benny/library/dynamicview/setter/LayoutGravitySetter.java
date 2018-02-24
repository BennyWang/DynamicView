package com.benny.library.dynamicview.setter;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.benny.library.dynamicview.property.GravityProperty;

public class LayoutGravitySetter {
    public static final String PROPERTY = "gravity";

    public void setGravity(View view, String value) {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();
        if (lparams != null) {
            GravityProperty property = GravityProperty.of(view.getContext(), value);
            if (lparams instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) lparams).gravity = property.gravity;
            } else if (lparams instanceof RelativeLayout.LayoutParams) {
                setRelativeLayoutGravity((RelativeLayout.LayoutParams) lparams, property.gravity);
            }
        }
    }

    private void setRelativeLayoutGravity(RelativeLayout.LayoutParams lparams, int gravity) {
        if ((gravity & Gravity.START) == Gravity.START) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                lparams.addRule(RelativeLayout.ALIGN_PARENT_START);
            }
            else {
                lparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
        }

        if ((gravity & Gravity.END) == Gravity.END) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                lparams.addRule(RelativeLayout.ALIGN_PARENT_END);
            }
            else {
                lparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
        }

        if ((gravity & Gravity.TOP) == Gravity.TOP) {
            lparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }

        if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            lparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }

        if ((gravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
            lparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }

        if ((gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
            lparams.addRule(RelativeLayout.CENTER_VERTICAL);
        }
    }
}
