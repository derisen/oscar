/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.Util;

import static ca.nines.wilde.cmd.DocCompare.LEVEN_THRESHOLD;
import java.text.Normalizer;
import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;

/**
 * This class contains methods to calculate the similarity distance of two strings,
 * as well as utility methods like text normalizer.
 * @author mjoyce
 */
public class Text {

    /** 
    * This method implements the Levenshtein Distance algorithm to find
    * similarities between two strings.
    * @param a string argument
    * @param b string argument
    * @return double value
    */
    public static double levenshtein(String a, String b) {
        if (a.equals(b)) {
            return 1.0;
        }
        int maxLength = Math.max(a.length(), b.length());
        int limit = (int) Math.ceil(maxLength * (1.0 - LEVEN_THRESHOLD));
        LevenshteinDistance ld = new LevenshteinDistance(limit);
        int distance = ld.apply(a, b);
        if (distance < 0) {
            return 0;
        }
        double similarity = 1.0 - (distance / ((double) maxLength));
        return similarity;
        
    }
    
    /** This method implements the Cosine Distance algorithm.
     * @param a string argument
     * @param b string argument
     * @return double value
     */

    public static double cosine(String a, String b) {
        CosineDistance cd = new CosineDistance();
        double distance = cd.apply(a, b);
        return 1.0 - distance;
    }
    
    /** This method implements a text normalizer
     * @param text string argument
     * @return string value
     */
    public static String normalize(String text) {
        return Normalizer
                .normalize(text, Normalizer.Form.NFD)
                .toLowerCase()
                .replaceAll("(\\p{Graph})\\p{Punct}+(\\p{Graph})", "$1$2")
                .replaceAll("\\p{Punct}+", " ")
                .replaceAll("\\s+", " ")
                .replaceAll("[^a-z0-9 -]", "")
                .trim();
    }

}
