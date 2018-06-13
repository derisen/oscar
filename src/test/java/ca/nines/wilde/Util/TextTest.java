/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.Util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author mjoyce
 */
public class TextTest {


    @Test
    public void testLevenshtein() {
        String a = "abcde";
        String b = "abde";
        double expResult = 0.8;
        double result = Text.levenshtein(a, b);
        assertEquals(expResult, result, 0.0);
        
        String c = "abde";
        double expResult2 = 1.0;
        double result2 = Text.levenshtein(b,c);
        assertEquals(expResult2, result2, 0.0);
        
    }


    @Test
    public void testCosine() {
        String a = "a a b c";
        String b = "a b b c";
        double expResult = 0.83;
        double result = Text.cosine(a, b);
        assertEquals(expResult, result, 0.02);
    }


    @Test
    public void testNormalize() {
        String text = " a test isn't fun. But this is. ";
        String expResult = "a test isnt fun but this is";
        String result = Text.normalize(text);
        assertEquals(expResult, result);
    }

}
