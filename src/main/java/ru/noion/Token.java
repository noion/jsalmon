package ru.noion;

public record Token(TokenType type, String lexeme, Object literal, int line, int column) {
    @Override
    public String toString() {
        return "[%d:%d] %s %s %s\n".formatted(line, column, type, lexeme, literal);
    }
}
