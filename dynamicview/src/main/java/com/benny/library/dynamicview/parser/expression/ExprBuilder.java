package com.benny.library.dynamicview.parser.expression;

import java.util.ArrayList;
import java.util.List;

public class ExprBuilder {
    private static Lexer lexer = new Lexer();
    private static ExprPattern variablePattern = new ExprPattern(false);
    private static ExprPattern fallbackPattern = new ExprPattern(false);
    private static ExprPattern selectPattern = new ExprPattern(false);
    private static ExprPattern concatPattern = new ExprPattern(true);

    public static Expr build(String expression) throws IllegalExprException {
        List<Token> tokens = lexer.parse(expression);

        if (tokens.isEmpty()) {
            throw new IllegalExprException("empty expression is illegal");
        }

        if (variablePattern.match(tokens)) {
            return new VariableExpr(tokens.get(0).val);
        }
        else if (fallbackPattern.match(tokens)) {
            VariableExpr expr = new VariableExpr(tokens.get(0).val);
            VariableExpr fallbackExpr = new VariableExpr(tokens.get(2).val);
            return new FallbackExpr(expr, fallbackExpr);
        }
        else if (selectPattern.match(tokens)) {
            VariableExpr conditionExpr = new VariableExpr(tokens.get(0).val);
            VariableExpr trueExpr = new VariableExpr(tokens.get(2).val);
            VariableExpr falseExpr = new VariableExpr(tokens.get(4).val);
            return new SelectExpr(conditionExpr, trueExpr, falseExpr);
        }
        else if (concatPattern.match(tokens)) {
            List<Expr> exprs = new ArrayList<>();
            for (Token token : tokens) {
                if (token.type == Lexer.EXPRESSION) {
                    exprs.add(new VariableExpr(token.val));
                }
            }
            return new StringConcatExpr(exprs);
        }

        throw new IllegalExprException(expression + " expression is illegal");
    }

    static  {
        variablePattern.add(new Token(Lexer.EXPRESSION, null, (char) 0));

        fallbackPattern.add(new Token(Lexer.EXPRESSION, null, (char) 0));
        fallbackPattern.add(new Token(Lexer.OPERATOR, null, ':'));
        fallbackPattern.add(new Token(Lexer.EXPRESSION, null, (char) 0));

        selectPattern.add(new Token(Lexer.EXPRESSION, null, (char) 0));
        selectPattern.add(new Token(Lexer.OPERATOR, null, '?'));
        selectPattern.add(new Token(Lexer.EXPRESSION, null, (char) 0));
        selectPattern.add(new Token(Lexer.OPERATOR, null, ':'));
        selectPattern.add(new Token(Lexer.EXPRESSION, null, (char) 0));

        concatPattern.add(new Token(Lexer.EXPRESSION, null, (char) 0));
        concatPattern.add(new Token(Lexer.OPERATOR, null, '+'));
        concatPattern.add(new Token(Lexer.EXPRESSION, null, (char) 0));
    }
}
