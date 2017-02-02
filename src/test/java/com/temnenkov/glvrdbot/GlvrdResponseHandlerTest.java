package com.temnenkov.glvrdbot;

import com.temnenkov.glvrd.Fragment;
import com.temnenkov.glvrd.Hint;
import com.temnenkov.glvrd.ProofreadResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class GlvrdResponseHandlerTest {

    private GlvrdResponseHandler h;

    @Before
    public void setUp() throws Exception {
        h = new GlvrdResponseHandler();

    }

    @Test
    public void handleNoErr() throws Exception {
        final ProofreadResponse resp = new ProofreadResponse();
        String s = h.handle("any", resp);
        System.out.println(s);
        assertEquals("0 стоп-слов, замечаний к тексту нет.", s);
    }

    @Test
    public void handleOneErr() throws Exception {
        final ProofreadResponse resp = new ProofreadResponse();
        final List<Fragment> f = new ArrayList<>();
        Fragment frag = new Fragment();
        frag.setStart(0);
        frag.setEnd(13);
        f.add(frag);
        final Hint hint = new Hint();
        hint.setName("Необъективная оценка");
        hint.setDescription("Удалите или докажите фактами");
        frag.setHint(hint);
        resp.setFragments(f);
        String s = h.handle("Замечательный", resp);
        assertEquals("Обнаружено стоп-слов: 1\n" +
                "\n" +
                "<b>Замечательный</b>: необъективная оценка (удалите или докажите фактами)\n" +
                "\nИсходный текст:\n" +
                "<b>Замечательный</b>\n\nСтатистика:\n" +
                "знаков (без пробелов): 13, знаков (с пробелами): 13", s);
    }

    @Test
    public void handleTwoErr() throws Exception {
        final ProofreadResponse resp = getProofreadResponseTwo();
        String s = h.handle("Я замечательный", resp);
        System.out.println(s);
        assertEquals("Обнаружено стоп-слов: 2\n" +
                "\n" +
                "<b>Я</b>: личное местоимение (проверьте, можно ли удалить это местоимение без потери смысла)\n" +
                "<b>замечательный</b>: необъективная оценка (удалите или докажите фактами)\n\nИсходный текст:\n" +
                "<b>Я</b> <b>замечательный</b>\n\nСтатистика:\n" +
                "знаков (без пробелов): 14, знаков (с пробелами): 15", s);
    }

    @Test
    public void processTextTwo() throws Exception {
        final ProofreadResponse resp = getProofreadResponseTwo();

        String s = h.processText("Я замечательный", resp.getFragments());
        assertEquals("<b>Я</b> <b>замечательный</b>", s);
    }

    @Test
    public void processTextTwoPlus() throws Exception {
        final ProofreadResponse resp = getProofreadResponseTwo();

        String s = h.processText("Я замечательный чувак", resp.getFragments());
        assertEquals("<b>Я</b> <b>замечательный</b> чувак", s);
    }

    @Test
    public void processTextTwoPlusPlus() throws Exception {
        final ProofreadResponse resp = getProofreadResponseTwo();

        resp.getFragments().forEach(fragment -> {
            fragment.setStart(fragment.getStart() + 3);
            fragment.setEnd(fragment.getEnd() + 3);
        });

        String s = h.processText("Не я замечательный чувак", resp.getFragments());
        assertEquals("Не <b>я</b> <b>замечательный</b> чувак", s);
    }

    private ProofreadResponse getProofreadResponseTwo() {
        final ProofreadResponse resp = new ProofreadResponse();
        final List<Fragment> f = new ArrayList<>();
        {
            Fragment frag = new Fragment();
            frag.setStart(0);
            frag.setEnd(1);
            f.add(frag);
            final Hint hint = new Hint();
            hint.setName("Личное местоимение");
            hint.setDescription("Проверьте, можно ли&nbsp;удалить это местоимение без&nbsp;потери смысла");
            frag.setHint(hint);
        }
        {
            Fragment frag = new Fragment();
            frag.setStart(2);
            frag.setEnd(15);
            f.add(frag);
            final Hint hint = new Hint();
            hint.setName("Необъективная оценка");
            hint.setDescription("Удалите или докажите фактами");
            frag.setHint(hint);
        }
        resp.setFragments(f);
        return resp;
    }

    @Test
    public void smooth() throws Exception {
        assertEquals("большой", h.smooth("Большой"));
    }

    @Test
    public void smooth1() throws Exception {
        assertEquals("б", h.smooth("Б"));
    }

    @Test
    public void smooth0() throws Exception {
        assertEquals("", h.smooth(""));
    }

    @Test
    public void smoothNull() throws Exception {
        assertEquals("", h.smooth(null));
    }

}