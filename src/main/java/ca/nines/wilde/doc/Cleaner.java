/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.xpath.XPathExpressionException;

/**
 *
 * @author michael
 */
public class Cleaner {

    private static final Set<String> KEPT = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[]{
        "dc.date",
        "dc.publisher",
        "dc.region",
        "dc.language",
        "dc.region.city",
        "dc.source.database",
        "dc.source.institution",
        "dc.source.facsimile",
        "dc.publisher.edition",
        "dc.source.url",
    })));

    public List<String> clean(WildeDoc doc) throws XPathExpressionException {
        List<String> removed = new ArrayList<>();
        for(String name : doc.listMetadataNames()) {
            if( ! KEPT.contains(name)) {
                doc.removeMetadata(name);
                removed.add(name);
            }
        }
        return removed;
    }

}
