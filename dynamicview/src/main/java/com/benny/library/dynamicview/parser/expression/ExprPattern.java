package com.benny.library.dynamicview.parser.expression;

import java.util.ArrayList;
import java.util.List;

public class ExprPattern {
    private List<Token> pattern = new ArrayList<>();
    private int size = 0;
    private boolean recursive;

    public ExprPattern(boolean recursive) {
        this.recursive = recursive;
    }

    public void add(Token token) {
        pattern.add(token);
        ++size;
    }

    public boolean match(List<Token> tokens) {
        return recursive ? recurseMatch(tokens) : flatMatch(tokens);
    }

    private boolean flatMatch(List<Token> tokens) {
        if (tokens.size() != pattern.size()) {
            return false;
        }

        for (int i = 0; i < pattern.size(); ++i) {
            if (!compare(pattern.get(i), tokens.get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean recurseMatch(List<Token> tokens) {
        for (int i = 0; i < tokens.size(); ++i) {
            int index = i < size ? i : (i % size + 1);
            if (!compare(pattern.get(index), tokens.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean compare(Token t1, Token t2) {
        return t1.type == t2.type && (t1.type == Lexer.EXPRESSION || t1.cVal == t2.cVal);
    }
}
