package ru.noion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ScannerTest {

    @Test
    public void test() {
        // given
        var scanner = new Scanner("1 + 2", "");
        // when
        var tokens = scanner.scanTokens();
        // then
        Assertions.assertEquals(TokenType.NUMBER, tokens.get(0).type());
        Assertions.assertEquals(TokenType.PLUS, tokens.get(1).type());
        Assertions.assertEquals(TokenType.NUMBER, tokens.get(2).type());
    }
}