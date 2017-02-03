package com.temnenkov.glvrd;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DeclensionTest {

    private Declension declension;
    private Declension.DeclensionInfo info;

    @Before
    public void setUp() throws Exception {
        declension = new Declension();
        info = new Declension.DeclensionInfo("замечание", "замечания", "замечаний");
    }


    @Test
    public void decline1() throws Exception {
       assertEquals("замечание", declension.decline(1, info));
    }

    @Test
    public void decline2() throws Exception {
       assertEquals("замечания", declension.decline(2, info));
    }

    @Test
    public void decline3() throws Exception {
       assertEquals("замечания", declension.decline(3, info));
    }

    @Test
    public void decline4() throws Exception {
       assertEquals("замечания", declension.decline(4, info));
    }

    @Test
    public void decline5() throws Exception {
       assertEquals("замечаний", declension.decline(5, info));
    }

    @Test
    public void decline6() throws Exception {
       assertEquals("замечаний", declension.decline(6, info));
    }

    @Test
    public void decline7() throws Exception {
       assertEquals("замечаний", declension.decline(7, info));
    }

    @Test
    public void decline8() throws Exception {
       assertEquals("замечаний", declension.decline(8, info));
    }

    @Test
    public void decline9() throws Exception {
       assertEquals("замечаний", declension.decline(9, info));
    }

    @Test
    public void decline10() throws Exception {
       assertEquals("замечаний", declension.decline(10, info));
    }

    @Test
    public void decline11() throws Exception {
       assertEquals("замечаний", declension.decline(11, info));
    }

    @Test
    public void decline12() throws Exception {
       assertEquals("замечаний", declension.decline(12, info));
    }

    @Test
    public void decline15() throws Exception {
       assertEquals("замечаний", declension.decline(15, info));
    }

    @Test
    public void decline0() throws Exception {
        assertEquals("замечаний", declension.decline(0, info));
    }

    @Test(expected = IllegalArgumentException.class)
    public void declineNull() throws Exception {
        declension.decline(0, null);
    }
}