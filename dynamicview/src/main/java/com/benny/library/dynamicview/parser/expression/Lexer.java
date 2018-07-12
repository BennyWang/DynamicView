package com.benny.library.dynamicview.parser.expression;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public static final int EXPRESSION = 0;
    public static final int OPERATOR = 1;

    public List<Token> parse(String expr) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < expr.length(); ++i) {
            char b = expr.charAt(i);
            if (b == '?' || b == ':' || b == '+') {
                if (builder.length() != 0) {
                    tokens.add(new Token(EXPRESSION, builder.toString(), (char)0));
                    builder.setLength(0);
                }
                tokens.add(new Token(OPERATOR, null, b));
            }
            else if (b == ' ') {
                if (builder.length() != 0) {
                    tokens.add(new Token(EXPRESSION, builder.toString(), (char)0));
                    builder.setLength(0);
                }
            }
            else {
                builder.append(b);
            }
        }

        if (builder.length() != 0) {
            tokens.add(new Token(EXPRESSION, builder.toString(), (char)0));
        }
        return tokens;
    }
}
