package com.benny.library.dynamicview.parser.expression;

import org.json.JSONObject;

public interface Expr {
    Object eval(JSONObject data);
}
