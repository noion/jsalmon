package ru.noion.jsalmon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class Salmon {

    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jsalmon [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        var bytes = Files.readAllBytes(Path.of(path));
        run(path, new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
    }

    private static void runPrompt() throws IOException {
        var input = new InputStreamReader(System.in);
        var reader = new BufferedReader(input);

        while (true) {
            System.out.println("> ");
            var line = reader.readLine();
            if (line == null) break;
            run("", line);
            hadError = false;
        }
    }

    private static void run(String filePath, String source) {
        var scanner = new Scanner(source, filePath);
        var tokens = scanner.scanTokens();

        // Just for print the tokens.
        for (var token : tokens) {
            System.out.println(token);
        }
    }

    static void error(String fileName, int line, String message) {
        report(fileName, line, "", message);
    }

    private static void report(String fileName, int line, String where, String message) {
        System.err.printf("File %s [line %s] Error %s: %s%n", fileName, line, where, message);
    }
}