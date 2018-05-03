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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
        for (String name : doc.listMetadataNames()) {
            if (!KEPT.contains(name)) {
                doc.removeMetadata(name);
                removed.add(name);
            }
        }
        return removed;
    }

    public void addIdentifiers(WildeDoc doc, String docId, int n) throws XPathExpressionException {
        Document xml = doc.getXmlDocument();
        Element root = xml.getDocumentElement();
        if (!root.hasAttribute("id")) {
            root.setAttribute("id", docId + "_" + n);
        }
        NodeList paragraphs = doc.getParagraphs();
        for (int i = 0; i < paragraphs.getLength(); i++) {
            Element p = (Element) paragraphs.item(i);
            if (!p.hasAttribute("id")) {
                p.setAttribute("id", docId + "_" + n + "_" + i);
            }
        }
        // adds Id attributes to the document root and to every paragraph.
    }

}
