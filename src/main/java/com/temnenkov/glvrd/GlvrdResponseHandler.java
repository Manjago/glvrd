package com.temnenkov.glvrd;

import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Required;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;


public class GlvrdResponseHandler {

    private StatCalcer statCalcer;
    private Declension declension;
    private Declension.DeclensionInfo info;

    public String handle(String text, ProofreadResponse resp) {

        if (resp.getFragments() == null || resp.getFragments().isEmpty()) {
            return "Замечаний нет." +
                    "\n\nСтатистика:\n" +
                    statCalcer.stat(text).info();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Исходный текст:\n\n");
        sb.append(processText(text, resp.getFragments()));
        sb.append("\n");

        sb.append(MessageFormat.format("\nОбнаружено {0} {1}.\n", resp.getFragments().size(),
                declension.decline(resp.getFragments().size(), info)));

        resp.getFragments().forEach(fragment -> sb.append(process(text, fragment)));

        sb.append("\n\nСтатистика:\n");
        sb.append(statCalcer.stat(text).info());

        return sb.toString();
    }

    private String process(String text, Fragment frag) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("<b>");
        sb.append(text.substring(frag.getStart(), frag.getEnd()));
        sb.append("</b>");
        if (frag.getHint() != null) {
            sb.append(": ");
            sb.append(smooth(frag.getHint().getName()));
            sb.append(" (");
            sb.append(smooth(frag.getHint().getDescription()));
            sb.append(")");
        }

        return sb.toString();
    }

    String processText(String text, Collection<Fragment> frags) {

        List<Fragment> workset = new ArrayList<>(frags);
        workset.sort(Comparator.comparingInt(Fragment::getStart));

        StringBuilder sb = new StringBuilder();

        int prev = 0;
        for (int i = 0; i < workset.size(); ++i) {
            Fragment f = workset.get(i);
            if (f.getStart() > prev){
                sb.append(lameEscape(text.substring(prev, f.getStart())));
            }
            sb.append("<b>");
            sb.append(lameEscape(text.substring(f.getStart(), f.getEnd())));
            sb.append("</b>");
            prev = f.getEnd();
        }
        if (text.length() > prev){
            sb.append(lameEscape(text.substring(prev, text.length())));
        }

        return sb.toString();
    }

    String lameEscape(String s) {
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

    String smooth(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        return Parser.unescapeEntities(s.substring(0, 1).toLowerCase() + s.substring(1), false);
    }

    @Required
    public void setStatCalcer(StatCalcer statCalcer) {
        this.statCalcer = statCalcer;
    }

    @Required
    public void setDeclension(Declension declension) {
        this.declension = declension;
    }

    @Required
    public void setInfo(Declension.DeclensionInfo info) {
        this.info = info;
    }


}
