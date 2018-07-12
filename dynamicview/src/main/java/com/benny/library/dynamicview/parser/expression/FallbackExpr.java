package com.benny.library.dynamicview.parser.expression;

import org.json.JSONObject;

public class FallbackExpr implements Expr {
    private Expr expr;
    private Expr fallback;

    public FallbackExpr(Expr expr, Expr fallback) {
        this.expr = expr;
        this.fallback = fallback;
    }

    @Override
    public Object eval(JSONObject data) {
        Object result = expr.eval(data);
        return result == null ? fallback.eval(data) : result;
    }
}
