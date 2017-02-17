package com.temnenkov.glvrd;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextSplitter {

    private static final Pattern BOLD = Pattern.compile("<b>(.*?)</b>");
    private static final Pattern NEWLINE = Pattern.compile("\\n");

    public List<String> splitInChunks(String s, int chunkSize) {
        final List<String> done = new ArrayList<>();
        splitInChunks(done, s, chunkSize);
        return done;
    }

    private void splitInChunks(List<String> done, String s, int chunkSize) {

        if (s.length() <= chunkSize) {
            done.add(s);
            return;
        }

        List<BadFrag> bold = mark(s, BOLD);
        List<BadFrag> newLine = mark(s, NEWLINE);

        int length = s.length();
        final int end = Math.min(length, chunkSize);

        boolean founded = false;
        for (int i = end; i > 0; --i) {
            if (test(bold, i) && test(newLine, i)) {
                done.add(s.substring(0, i));
                splitInChunks(done, s.substring(i), chunkSize);
                founded = true;
                break;
            }
        }

        if (!founded){
            done.add(s);
        }

    }

    boolean test(List<BadFrag> frags, int pos) {
        for (BadFrag badFrag : frags) {
            if (badFrag.contains(pos)) {
                return false;
            }
        }
        return true;
    }

    List<BadFrag> mark(String input, Pattern pattern) {

        List<BadFrag> r = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            r.add(new BadFrag(matcher.group(),
                    matcher.start(),
                    matcher.end()));
        }

        return r;
    }

}
