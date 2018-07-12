package com.benny.library.dynamicview.parser.expression;

import org.json.JSONObject;

import java.util.List;

public class StringConcatExpr implements Expr {
    private List<Expr> exprs;

    public StringConcatExpr(List<Expr> exprs) {
        this.exprs = exprs;
    }

    @Override
    public Object eval(JSONObject data) {
        StringBuilder builder = new StringBuilder();
        for (Expr expr : exprs) {
            builder.append(expr.eval(data));
        }
        return builder.toString();
    }
}
