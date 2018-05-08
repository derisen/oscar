/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.Util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
public class TextTest {

    public TextTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of levenshtein method, of class Text.
     */
    @Test
    public void testLevenshtein() {
        String a = "abcde";
        String b = "abde";
        double expResult = 0.8;
        double result = Text.levenshtein(a, b);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of cosine method, of class Text.
     */
    @Test
    public void testCosine() {
        String a = "a a b c";
        String b = "a b b c";
        double expResult = 0.83;
        double result = Text.cosine(a, b);
        assertEquals(expResult, result, 0.02);
    }

    /**
     * Test of normalize method, of class Text.
     */
    @Test
    public void testNormalize() {
        String text = " a test isn't fun. But this is. ";
        String expResult = "a test isnt fun but this is";
        String result = Text.normalize(text);
        assertEquals(expResult, result);
    }

}
