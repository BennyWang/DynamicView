package com.benny.library.dynamicview.parser.property;

import android.util.Log;

import com.benny.library.dynamicview.parser.expression.Expr;
import com.benny.library.dynamicview.parser.expression.ExprBuilder;
import com.benny.library.dynamicview.parser.expression.IllegalExprException;
import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

public class DynamicProperty {
    private String key;
    private String valueKey;

    private Expr expr;
    private boolean passthrough = false;

    public static boolean canHandle(String key, String value) {
        return value.length() > 2 && value.startsWith("{") && value.endsWith("}");
    }

    public DynamicProperty(String key, String value) throws IllegalExprException {
        this.key = key;
        valueKey = value.substring(1, value.length() - 1);

        if (valueKey.startsWith("@")) {
            passthrough = true;
        }
        else {
            expr = ExprBuilder.build(valueKey);
        }
    }

    public void set(DynamicViewBuilder builder, JSONObject data) {
        try {
            if (passthrough) {
                builder.setProperty(key, valueKey);
            }
            else {
                builder.setProperty(key, expr.eval(data));
            }
        }
        catch (Exception e) {
            Log.e("DynamicViewEngineImpl", "DynamicProperty.set exception " + Log.getStackTraceString(e));
        }
    }
}
