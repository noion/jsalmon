package ru.noion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ScannerTest {

    public static Stream<Arguments> scannerTestSource() {
        return Stream.of(
                Arguments.of("1 + 2", new TokenType[]{TokenType.NUMBER, TokenType.PLUS, TokenType.NUMBER, TokenType.EOF}),
                Arguments.of("or and", new TokenType[]{TokenType.OR, TokenType.AND, TokenType.EOF}),
                Arguments.of("and or", new TokenType[]{TokenType.AND, TokenType.OR, TokenType.EOF}),
                Arguments.of("and or and", new TokenType[]{TokenType.AND, TokenType.OR, TokenType.AND, TokenType.EOF}),
                Arguments.of("(", new TokenType[]{TokenType.LEFT_PAREN, TokenType.EOF}),
                Arguments.of(")", new TokenType[]{TokenType.RIGHT_PAREN, TokenType.EOF}),
                Arguments.of("{", new TokenType[]{TokenType.LEFT_BRACE, TokenType.EOF}),
                Arguments.of("}", new TokenType[]{TokenType.RIGHT_BRACE, TokenType.EOF}),
                Arguments.of(",", new TokenType[]{TokenType.COMMA, TokenType.EOF}),
                Arguments.of(".", new TokenType[]{TokenType.DOT, TokenType.EOF}),
                Arguments.of("-", new TokenType[]{TokenType.MINUS, TokenType.EOF}),
                Arguments.of("+", new TokenType[]{TokenType.PLUS, TokenType.EOF}),
                Arguments.of(";", new TokenType[]{TokenType.SEMICOLON, TokenType.EOF}),
                Arguments.of("/", new TokenType[]{TokenType.SLASH, TokenType.EOF}),
                Arguments.of("*", new TokenType[]{TokenType.STAR, TokenType.EOF}),
                Arguments.of("!", new TokenType[]{TokenType.BANG, TokenType.EOF}),
                Arguments.of("!=", new TokenType[]{TokenType.BANG_EQUAL, TokenType.EOF}),
                Arguments.of("=", new TokenType[]{TokenType.EQUAL, TokenType.EOF}),
                Arguments.of("==", new TokenType[]{TokenType.EQUAL_EQUAL, TokenType.EOF}),
                Arguments.of(">", new TokenType[]{TokenType.GREATER, TokenType.EOF}),
                Arguments.of(">=", new TokenType[]{TokenType.GREATER_EQUAL, TokenType.EOF}),
                Arguments.of("<", new TokenType[]{TokenType.LESS, TokenType.EOF}),
                Arguments.of("<=", new TokenType[]{TokenType.LESS_EQUAL, TokenType.EOF}),
                Arguments.of("or", new TokenType[]{TokenType.OR, TokenType.EOF}),
                Arguments.of("and", new TokenType[]{TokenType.AND, TokenType.EOF}),
                Arguments.of("class", new TokenType[]{TokenType.CLASS, TokenType.EOF}),
                Arguments.of("else", new TokenType[]{TokenType.ELSE, TokenType.EOF}),
                Arguments.of("false", new TokenType[]{TokenType.FALSE, TokenType.EOF}),
                Arguments.of("fun", new TokenType[]{TokenType.FUN, TokenType.EOF}),
                Arguments.of("for", new TokenType[]{TokenType.FOR, TokenType.EOF}),
                Arguments.of("if", new TokenType[]{TokenType.IF, TokenType.EOF}),
                Arguments.of("nil", new TokenType[]{TokenType.NIL, TokenType.EOF}),
                Arguments.of("print", new TokenType[]{TokenType.PRINT, TokenType.EOF}),
                Arguments.of("return", new TokenType[]{TokenType.RETURN, TokenType.EOF}),
                Arguments.of("super", new TokenType[]{TokenType.SUPER, TokenType.EOF}),
                Arguments.of("this", new TokenType[]{TokenType.THIS, TokenType.EOF}),
                Arguments.of("true", new TokenType[]{TokenType.TRUE, TokenType.EOF}),
                Arguments.of("var", new TokenType[]{TokenType.VAR, TokenType.EOF}),
                Arguments.of("while", new TokenType[]{TokenType.WHILE, TokenType.EOF}),
                Arguments.of("1", new TokenType[]{TokenType.NUMBER, TokenType.EOF}),
                Arguments.of("1.0", new TokenType[]{TokenType.NUMBER, TokenType.EOF}),
                Arguments.of("1.0e1", new TokenType[]{TokenType.NUMBER, TokenType.IDENTIFIER, TokenType.EOF}),
                Arguments.of("1.0e+1", new TokenType[]{TokenType.NUMBER, TokenType.IDENTIFIER, TokenType.PLUS, TokenType.NUMBER, TokenType.EOF}),
                Arguments.of("1.0e-1", new TokenType[]{TokenType.NUMBER, TokenType.IDENTIFIER, TokenType.MINUS, TokenType.NUMBER, TokenType.EOF}),
                Arguments.of("abs", new TokenType[]{TokenType.IDENTIFIER, TokenType.EOF}),
                Arguments.of(" ", new TokenType[]{TokenType.EOF}),
                Arguments.of("\t", new TokenType[]{TokenType.EOF}),
                Arguments.of("\n", new TokenType[]{TokenType.EOF}),
                Arguments.of("\r", new TokenType[]{TokenType.EOF}),
                Arguments.of("\\", new TokenType[]{TokenType.EOF}),
                Arguments.of(("/*asas*/"), new TokenType[]{TokenType.EOF}),
                Arguments.of(("""
                        /*asd fa 123 {}A    1
                        steadfast \r\\ suber for , . - + ; / * ! != = == > >= < <= or and class else false fun for if nil print return super this true var while
                        steadfast*/
                        """), new TokenType[]{TokenType.EOF}),
                Arguments.of("""
                        /*sadfsadfsadf
                        /*sdfasdfsaf */
                        """, new TokenType[]{TokenType.EOF}),
                Arguments.of("1 /*/*/ + /*/*/ 2", new TokenType[]{TokenType.NUMBER, TokenType.PLUS, TokenType.NUMBER, TokenType.EOF})
        );
    }

    @ParameterizedTest
    @MethodSource("scannerTestSource")
    public void scanner(String source, TokenType[] expectedTypes) {
        // given
        var scanner = new Scanner(source, "");
        // when
        var tokens = scanner.scanTokens();
        System.out.println(tokens);
        for (int i = 0; i < expectedTypes.length; i++) {
            Assertions.assertEquals(expectedTypes[i], tokens.get(i).type());
        }
    }
}