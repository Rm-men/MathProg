package org.example.lab;

public class FormatTextUtils {

    public static String format(String input, TextFormat... textFormats) {
        StringBuilder inputBuilder = new StringBuilder(input);
        for (TextFormat textFormat  : textFormats) {
            inputBuilder = new StringBuilder(textFormat.code + inputBuilder + TextFormat.RESET.code);
        }
        return inputBuilder.toString();
    }

    public enum TextFormat {
        RESET("\u001B[0m"),
        BOLD("\033[1m"),
        ITALIC("\033[3m"),
        UNDERLINE("\033[4m"),
        RED("\u001B[31m"),
        ORANGE("\u001B[38;5;208m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        BLACK("\u001B[30m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m");

        public final String code;

        TextFormat(String code) {
            this.code = code;
        }
    }

    public enum Symbols {
        STAR("* "),
        CHECK("✓ "),
        CROSS("✘ "),
        PLUS("+ "),
        EXCLAMATION("! "),
        HASH("# "),
        DOT("· "),
        MINUS("- ");

        public final String text;

        Symbols(String text) {
            this.text = text;
        }
    }
}
