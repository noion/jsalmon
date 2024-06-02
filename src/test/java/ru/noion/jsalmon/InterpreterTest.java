package ru.noion.jsalmon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class InterpreterTest {

    @Test
    void interpret_divide_by_zero() {
        // given
        var scanner = new Scanner("1 / 0;", "");
        var tokens = scanner.scanTokens();
        var parser = new Parser(tokens);
        var statements = parser.parse();
        var interpreter = new Interpreter();

        // Prepare to capture error stream
        var errorContent = new ByteArrayOutputStream();
        var originalErr = System.err;
        System.setErr(new PrintStream(errorContent));

        // when
        interpreter.interpret(statements);

        // Reset error stream back to original
        System.setErr(originalErr);

        // then
        Assertions.assertTrue(errorContent.toString().contains("Division by zero"));
    }
}