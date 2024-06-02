package ru.noion.jsalmon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource(value = {
            "print 1 + 2;, 3",
            "print 1 + 2 * 3;, 7",
            "print 1 + \"a\";, 1a",
            "print true;, true",
            "print \"a\" + \"b\";, ab",
    })
    void interpret_print(String source, String expected) {
        // given
        var scanner = new Scanner(source, "");
        var tokens = scanner.scanTokens();
        var parser = new Parser(tokens);
        var statements = parser.parse();
        var interpreter = new Interpreter();

        // Prepare to capture output stream
        var outContent = new ByteArrayOutputStream();
        var originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // when
        interpreter.interpret(statements);

        // Reset error stream back to original
        System.setErr(originalOut);

        // then
        var result = outContent.toString().replace(System.lineSeparator(), "");
        Assertions.assertEquals(expected, result);
    }
}