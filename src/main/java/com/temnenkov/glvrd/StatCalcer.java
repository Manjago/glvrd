package com.temnenkov.glvrd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

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

        private void incWithSpaces() {
            ++symsWithSpaces;
        }

        private void incNoSpaces() {
            ++symsNoSpaces;
        }

        public String info(){
            return "знаков (без пробелов): " + symsNoSpaces + ", знаков (с пробелами): " + symsWithSpaces;
        }
    }

}
