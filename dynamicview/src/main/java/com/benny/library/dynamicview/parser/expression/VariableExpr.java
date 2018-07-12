package com.benny.library.dynamicview.parser.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VariableExpr implements Expr {
    private ExtractorChain chain = new ExtractorChain();
    private String expression;

    public VariableExpr(String expr) {
        if (expr.startsWith("$")) {
            expr = expr.substring(1);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < expr.length(); ++i) {
                char c = expr.charAt(i);
                if (c == '.') {
                    if (builder.length() > 0) {
                        chain.add(new Extractor(builder.toString()));
                        builder.setLength(0);
                    }
                } else {
                    builder.append(c);
                }
            }

            if (builder.length() > 0) {
                chain.add(new Extractor(builder.toString()));
            }
        }
        else {
            this.expression = expr;
        }
    }

    @Override
    public Object eval(JSONObject data) {
        return expression != null ? expression : chain.extract(data);
    }

    public static class ExtractorChain {
        List<Extractor> extractors = new ArrayList<>();

        public void add(Extractor extractor) {
            extractors.add(extractor);
        }

        public Object extract(Object data) {
            for (Extractor extractor : extractors) {
                if (data == null) {
                    break;
                }
                data = extractor.extract(data);
            }
            return data;
        }
    }

    public static class Extractor {
        private String key;
        private int index = -1;

        public Extractor(String value) {
            for (int i = 0; i < value.length(); ++i) {
                char c = value.charAt(i);
                if (c == '[') {
                    key = value.substring(0, i);
                    index = Integer.parseInt(value.substring(i + 1, value.length() - 1));
                    return;
                }
            }
            key = value;
        }

        public Object extract(Object data) {
            if (index >= 0) {
                JSONArray array = ((JSONObject) data).optJSONArray(key);
                return array.opt(index);
            }

            return ((JSONObject) data).opt(key);
        }
    }
}
