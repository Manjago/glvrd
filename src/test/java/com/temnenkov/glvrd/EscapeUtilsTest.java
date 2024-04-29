package com.temnenkov.glvrd;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EscapeUtilsTest {
    @Test
    public void smooth() throws Exception {
        assertEquals("большой", EscapeUtils.smooth("Большой"));
    }

    @Test
    public void smooth1() throws Exception {
        assertEquals("б", EscapeUtils.smooth("Б"));
    }

    @Test
    public void smooth0() throws Exception {
        assertEquals("", EscapeUtils.smooth(""));
    }

    @Test
    public void smoothNull() throws Exception {
        assertEquals("", EscapeUtils.smooth(null));
    }

    @Test
    public void lameEscape() {
        assertEquals("&lt;345&gt;", EscapeUtils.lameEscape("<345>"));
    }

    @Test
    public void lameEscape2() {
        assertEquals("345&gt;", EscapeUtils.lameEscape("345>"));
    }

    @Test
    public void lameEscape3() {
        assertEquals("345", EscapeUtils.lameEscape("345"));
    }


}