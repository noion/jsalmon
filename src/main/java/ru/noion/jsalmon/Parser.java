package ru.noion.jsalmon;

import java.util.List;

public class Parser {
    private static class ParserError extends RuntimeException {
    }

    private final List<Token> tokens;

    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Expr expression() {
        return equality();
    }

    private Expr equality() {
        Expr expr = comparision();

        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparision();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private boolean match(TokenType... tokenTypes) {
        for (TokenType tokenType : tokenTypes) {
            if (check(tokenType)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean check(TokenType tokenType) {
        if (isAtEnd()) {
            return false;
        }
        return peek().type() == tokenType;
    }

    private boolean isAtEnd() {
        return peek().type() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Expr comparision() {
        Expr expr = term();

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factir();

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expr right = factir();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factir() {
        Expr expr = unary();

        while (match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(TokenType.FALSE)) {
            return new Expr.Literal(false);
        }
        if (match(TokenType.TRUE)) {
            return new Expr.Literal(true);
        }
        if (match(TokenType.NIL)) {
            return new Expr.Literal(null);
        }
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Expr.Literal(previous().literal());
        }
        if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }
        throw error(peek(), "Unknown token type.");
    }

    private Token consume(TokenType tokenType, String message) {
        if (check(tokenType)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private ParserError error(Token token, String message) {
        Salmon.error(token, message);
        return new ParserError();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type() == TokenType.SEMICOLON) {
                return;
            }

            switch (peek().type()) {
                case TokenType.CLASS,
                     TokenType.FUN,
                     TokenType.VAR,
                     TokenType.FOR,
                     TokenType.IF,
                     TokenType.WHILE,
                     TokenType.PRINT,
                     TokenType.RETURN -> {
                    return;
                }
            }

            advance();
        }
    }

    Expr pars() {
        try {
            return expression();
        } catch (ParserError error) {
            return null;
        }
    }
}
