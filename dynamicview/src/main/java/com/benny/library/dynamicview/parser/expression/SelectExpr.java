package com.benny.library.dynamicview.parser.expression;

import org.json.JSONArray;
import org.json.JSONObject;

public class SelectExpr implements Expr {
    private Expr condition;
    private Expr trueResult;
    private Expr falseResult;

    public SelectExpr(Expr condition, Expr trueResult, Expr falseResult) {
        this.condition = condition;
        this.trueResult = trueResult;
        this.falseResult = falseResult;
    }

    @Override
    public Object eval(JSONObject data) {
        Object result = condition.eval(data);

        if (result == null) {
            return falseResult.eval(data);
        }
        else if (result instanceof JSONArray && ((JSONArray) result).length() == 0) {
            return falseResult.eval(data);
        }
        else if (result instanceof Number && ((Number) result).intValue() == 0) {
            return falseResult.eval(data);
        }
        else if (result.toString().equalsIgnoreCase("false")) {
            return falseResult.eval(data);
        }

        return trueResult.eval(data);
    }
}
