/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author michael
 */
public class WildeDoc {

    private final Document document;

    private final Path path;

    private final XPath xpath;

    public WildeDoc(Path path, Document document) {
        this.path = path;
        this.document = document;
        this.xpath = XPathFactory.newInstance().newXPath();
    }

    public Document getXmlDocument() {
        return document;
    }

    public List<String> listMetadataNames() throws XPathExpressionException {
        List<String> names = new ArrayList<>();
        NodeList nodelist = (NodeList)this.xpath.evaluate("//meta/@name", document.getDocumentElement(), XPathConstants.NODESET);
        for(int i = 0; i < nodelist.getLength(); i++) {
            names.add(nodelist.item(i).getNodeValue());
        }
        return names;
    }

    public String getMetadata(String name) throws XPathExpressionException {
        Node node = (Node)this.xpath.evaluate("//meta[@name='" + name + "']", document.getDocumentElement(), XPathConstants.NODE);
        if(node == null) {
            return null;
        }
        return node.getAttributes().getNamedItem("content").getNodeValue();
    }

    public void removeMetadata(String name) throws XPathExpressionException {
        Node node = (Node)this.xpath.evaluate("//meta[@name='" + name + "']", document.getDocumentElement(), XPathConstants.NODE);
        if(node == null) {
            return;
        }
        Node parent = node.getParentNode();
        parent.removeChild(node);
    }

    public String getOriginalText() throws XPathExpressionException {
        Node node = (Node)this.xpath.evaluate("//div[@id='original']", document.getDocumentElement(), XPathConstants.NODE);
        if(node == null) {
            return null;
        }
        return node.getTextContent();
    }

    public List<String> getTranslations() throws XPathExpressionException {
        NodeList nodelist = (NodeList)this.xpath.evaluate("//div/@lang", document.getDocumentElement(), XPathConstants.NODESET);
        List<String> translations = new ArrayList<>();
        for(int i = 0; i < nodelist.getLength(); i++) {
            translations.add(nodelist.item(i).getNodeValue());
        }
        return translations;
    }

    public String getTranslatedText(String lang) throws XPathExpressionException {
        Node node = (Node)this.xpath.evaluate("//div[@lang='" + lang + "']", document.getDocumentElement(), XPathConstants.NODE);
        if(node == null) {
            return null;
        }
        return node.getTextContent();
    }


}
