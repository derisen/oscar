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
 *
 * @author mjoyce
 */
public class Text {

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

    public static double cosine(String a, String b) {
        CosineDistance cd = new CosineDistance();
        double distance = cd.apply(a, b);
        return 1.0 - distance;
    }

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
