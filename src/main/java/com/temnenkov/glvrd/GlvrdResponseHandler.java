package com.temnenkov.glvrd;

import org.springframework.beans.factory.annotation.Required;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static com.temnenkov.glvrd.EscapeUtils.lameEscape;
import static com.temnenkov.glvrd.EscapeUtils.smooth;


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
        sb.append(text, frag.getStart(), frag.getEnd());
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
        for (Fragment f : workset) {
            if (f.getStart() > prev) {
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
