package com.benny.library.dynamicview.view.setter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

public class RelativeSetter {
    private static final String LEFT_OF = "leftOf";
    private static final String RIGHT_OF = "rightOf";
    private static final String ABOVE = "above";
    private static final String BELOW = "below";

    private static final String ALIGN_LEFT = "alignLeft";
    private static final String ALIGN_RIGHT = "alignRight";
    private static final String ALIGN_TOP = "alignTop";
    private static final String ALIGN_BOTTOM = "alignBottom";
    private static final String ALIGN_BASELINE = "alignBaseline";

    private Map<String, Integer> PROPERTIES = new HashMap<>();

    public RelativeSetter() {
        PROPERTIES.put(LEFT_OF, RelativeLayout.LEFT_OF);
        PROPERTIES.put(RIGHT_OF, RelativeLayout.RIGHT_OF);
        PROPERTIES.put(ABOVE, RelativeLayout.ABOVE);
        PROPERTIES.put(BELOW, RelativeLayout.BELOW);
        PROPERTIES.put(ALIGN_LEFT, RelativeLayout.ALIGN_LEFT);
        PROPERTIES.put(ALIGN_RIGHT, RelativeLayout.ALIGN_RIGHT);
        PROPERTIES.put(ALIGN_TOP, RelativeLayout.ALIGN_TOP);
        PROPERTIES.put(ALIGN_BOTTOM, RelativeLayout.ALIGN_BOTTOM);
        PROPERTIES.put(ALIGN_BASELINE, RelativeLayout.ALIGN_BASELINE);
    }

    public boolean canHandle(String prop) {
        return PROPERTIES.containsKey(prop);
    }

    public void set(View view, String key, String value) {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();
        if (lparams != null && lparams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) lparams).addRule(PROPERTIES.get(key), Integer.parseInt(value));
        }
    }
}
