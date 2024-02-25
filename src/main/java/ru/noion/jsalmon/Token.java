package ru.noion.jsalmon;

public record Token(String sourceFile, TokenType type, String lexeme, Object literal, int line, int column) {
    @Override
    public String toString() {
        return "%s: [%d:%d] %s %s %s\n".formatted(sourceFile, line, column, type, lexeme, literal);
    }
}
