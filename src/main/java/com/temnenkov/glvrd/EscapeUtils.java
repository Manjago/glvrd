package com.temnenkov.glvrd;

import org.jsoup.parser.Parser;

public final class EscapeUtils {

    private EscapeUtils() {
    }

    public static String lameEscape(String s) {
        if (s == null) {
            return s;
        }

        if (!s.contains("<") && !s.contains(">")) {
            return s;
        }

        return s
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    public static String smooth(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        return Parser.unescapeEntities(s.substring(0, 1).toLowerCase() + s.substring(1), false);
    }


}
