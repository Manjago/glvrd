package com.temnenkov.glvrd;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class StatCalcerTest {

    private StatCalcer statCalcer;

    @Before
    public void setUp() throws Exception {
        statCalcer = new StatCalcer();
    }

    @Test
    public void stat3() throws Exception {
      assertEquals(new StatCalcer.Stat(3, 3), statCalcer.stat("Три"));
    }

    @Test
    public void stat3_4() throws Exception {
        assertEquals(new StatCalcer.Stat(10, 9), statCalcer.stat("Три Четыре"));
    }

    @Test
    public void stat3_4_more() throws Exception {
        assertEquals(new StatCalcer.Stat(11, 9), statCalcer.stat("Три Четыре "));
    }

    @Test
    public void stat3_4_delim() throws Exception {
        assertEquals(new StatCalcer.Stat(11, 9), statCalcer.stat("Три\r\n Четыре "));
    }

    @Test
    public void stat3_4_info() throws Exception {
        assertEquals("знаков (без пробелов): " + 9 + ", знаков (с пробелами): " + 11, statCalcer.stat("Три\r\n Четыре ").info());
    }

    @Test
    public void statNull() throws Exception {
        assertEquals(new StatCalcer.Stat(), statCalcer.stat(null));
    }

    @Test
    public void statEmpty() throws Exception {
        assertEquals(new StatCalcer.Stat(), statCalcer.stat(""));
    }

}