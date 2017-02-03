package com.temnenkov.glvrd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.MessageFormat;

public class StatCalcer {

    public Stat stat(String text) {

        if (text == null || text.isEmpty()) {
            return new Stat();
        }

        try {
            return stat(new StringReader(text));
        } catch (IOException e) {
            throw new GlvrdException("fail read text " + text, e);
        }
    }

    private Stat stat(Reader isr) throws IOException {
        Stat r = new Stat();
        int c;
        while ((c = isr.read()) != -1) {

            if (c == ' ') {
                r.incWithSpaces();
            } else if (c != '\n' && c != '\r') {
                r.incNoSpaces();
                r.incWithSpaces();
            }

        }
        return r;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Stat {
        private int symsWithSpaces;
        private int symsNoSpaces;

        private static final Declension.DeclensionInfo INFO_NO_SPACES = new Declension.DeclensionInfo("знак (без пробелов)",
                "знака (без пробелов)", "знаков (без пробелов)");
        private static final Declension.DeclensionInfo INFO_WITH_SPACES = new Declension.DeclensionInfo("знак (с пробелами)",
                "знака (с пробелами)", "знаков (с пробелами)");

        private static final Declension DECLENSION = new Declension();

        private void incWithSpaces() {
            ++symsWithSpaces;
        }

        private void incNoSpaces() {
            ++symsNoSpaces;
        }

        public String info(){
            return MessageFormat.format("{0} {1}, {2} {3}",
                    symsNoSpaces,  DECLENSION.decline(symsNoSpaces, INFO_NO_SPACES), symsWithSpaces, DECLENSION.decline(symsWithSpaces, INFO_WITH_SPACES));
        }
    }

}
