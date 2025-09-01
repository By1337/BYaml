package dev.by1337.yaml.util;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record Wildcard(String pattern) {

    public boolean matches(String text) {
        return matches(text, pattern);
    }

    public static boolean matches(String text, String pattern) {
        StringBuilder sb = new StringBuilder();
        for (char c : pattern.toCharArray()) {
            switch (c) {
                case '*':
                    sb.append(".*");
                    break;
                case '?':
                    sb.append('.');
                    break;
                case '.':
                    sb.append("\\.");
                    break;
                case '\\', '+', '(', ')', '^', '$', '{', '}', '|', '[', ']':
                    sb.append('\\').append(c);
                    break;
                default:
                    sb.append(c);
            }
        }
        return text.matches(sb.toString());
    }
}
