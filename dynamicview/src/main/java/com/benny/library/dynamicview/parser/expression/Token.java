package com.benny.library.dynamicview.parser.expression;

public class Token {
    public int type;
    public String val;
    public char cVal;

    public Token(int type, String val, char cVal) {
        this.type = type;
        this.val = val;
        this.cVal = cVal;
    }
}
