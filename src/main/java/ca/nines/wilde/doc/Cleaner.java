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
 * This class contains methods that remove non-essential tags from an XML
 * document. Essential tags are defined as a static collection of strings. The class also 
 * contains a method to add identifiers to documents. 
 * @author mjoyce
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
        "wr.path",
    })));
    
    /**
    * This method removes all the tags in an XML document that are not marked as KEPT above.
    * @param doc WildeDoc object argument. A WildeDoc object points to a modified XML file. 
    * @return a string list that contains the names of the removed tags. 
    * @throws XPathExpressionException if the path information provided is not appropriate.
    */
    public List<String> clean(WildeDoc doc) throws XPathExpressionException {
        List<String> removed = new ArrayList<>();
        for (String name : doc.listMetadataNames()) {
            if (!KEPT.contains(name)) {
                doc.removeMetadata(name);
                removed.add(name);
            }
        }
 
        System.out.print("\nDone");
        return removed;
    }

    /** 
    * This method adds an identifier string to the head of an XML file.
    * @param doc WildeDoc object argument. A WildeDoc object points to a modified XML file.
    * @param docId string argument. Document identifier.
    * @param n integer argument. Further document identifier.
    * @throws XPathExpressionException if the path information provided is not appropriate.
    */
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
    }
    
}
