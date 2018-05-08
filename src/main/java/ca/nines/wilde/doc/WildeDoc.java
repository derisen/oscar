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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author michael
 */
public class WildeDoc {

    private final Document document;

    private final XPath xpath;

    private final Path path;

    public WildeDoc(Path path, Document document) {
        this.document = document;
        this.xpath = XPathFactory.newInstance().newXPath();
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public Document getXmlDocument() {
        return document;
    }

    public String getDocId() throws XPathExpressionException {
        String id = (String)this.xpath.evaluate("/html/@id", document.getDocumentElement(), XPathConstants.STRING);
        return id;
    }

    public void addDocSimilarity(WildeDoc other, double similarity, String measure) throws XPathExpressionException {
        Element link = document.createElement("link");
        link.setAttribute("class", "similarity");
        link.setAttribute("data-similarity", Double.toString(similarity));
        link.setAttribute("href", other.getDocId());
        link.setAttribute("rel", "similarity");
        document.getElementsByTagName("head").item(0).appendChild(link);
    }

    public void addParagraphSimilarity(Node paragraph, WildeDoc otherDoc, Node otherParagraph, double similarity, String measure) throws XPathExpressionException {
        Element a = document.createElement("a");
        a.setAttribute("class", "similarity");
        a.setAttribute("data-document", otherDoc.getDocId());
        a.setAttribute("data-paragraph", ((Element)otherParagraph).getAttribute("id"));
        a.setAttribute("data-similarity", Double.toString(similarity));
        a.setAttribute("data-type", measure);
        paragraph.appendChild(a);
    }

    public void removeParagraphSimilarities() throws XPathExpressionException {
        NodeList nodelist = (NodeList)this.xpath.evaluate("//a[@class='similarity']", document.getDocumentElement(), XPathConstants.NODESET);
        for(int i = 0; i < nodelist.getLength(); i++) {
            Node node = nodelist.item(i);
            node.getParentNode().removeChild(node);
        }
    }

    public void removeDocumentSimilarities() throws XPathExpressionException {
        NodeList nodelist = (NodeList)this.xpath.evaluate("//link[@class='similarity']", document.getDocumentElement(), XPathConstants.NODESET);
        for(int i = 0; i < nodelist.getLength(); i++) {
            Node node = nodelist.item(i);
            node.getParentNode().removeChild(node);
        }
    }

    public void setDocumentIndexed() {
        Element meta = document.createElement("meta");
        meta.setAttribute("name", "index.document");
        meta.setAttribute("content", "yes");
        document.getElementsByTagName("head").item(0).appendChild(meta);
    }

    public boolean isDocumentIndexed() throws XPathExpressionException {
        String indexed = (String)this.xpath.evaluate("//meta[@name='index.document']/@content", document.getDocumentElement(), XPathConstants.STRING);
        if(indexed != null && indexed.equals("yes")) {
            return true;
        }
        return false;
    }

    public void setParagraphsIndexed() {
        Element meta = document.createElement("meta");
        meta.setAttribute("name", "index.paragraph");
        meta.setAttribute("content", "yes");
        document.getElementsByTagName("head").item(0).appendChild(meta);
    }

    public boolean areParagraphsIndexed() throws XPathExpressionException {
        String indexed = (String)this.xpath.evaluate("//meta[@name='index.paragraph']/@content", document.getDocumentElement(), XPathConstants.STRING);
        if(indexed != null && indexed.equals("yes")) {
            return true;
        }
        return false;
    }

    public NodeList getParagraphs() throws XPathExpressionException {
        NodeList paragraphs = (NodeList)this.xpath.evaluate("//p", document.getDocumentElement(), XPathConstants.NODESET);
        return paragraphs;
    }

    public NodeList getParagraphs(String lang) throws XPathExpressionException {
        NodeList paragraphs = (NodeList)this.xpath.evaluate("//div[@lang='" + lang + "']/p", document.getDocumentElement(), XPathConstants.NODESET);
        return paragraphs;
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

    public void setMetadata(String name, String value) throws XPathExpressionException, Exception {
        Node node = (Node)this.xpath.evaluate("//meta[@name='" + name + "']", document.getDocumentElement(), XPathConstants.NODE);
        if(node == null) {
            addMetadata(name, value);
            return;
        }
        Element element = (Element)node;
        element.setAttribute("content", value);
    }

    public void setTitle(String title) throws XPathExpressionException {
        NodeList nodelist = document.getElementsByTagName("title");
        Element element;
        if(nodelist.getLength() >= 1) {
            element = (Element)nodelist.item(0);
        } else {
            element = document.createElement("title");
            Node head = (Node)this.xpath.evaluate("/html/head", document.getDocumentElement(), XPathConstants.NODE);
            head.appendChild(element);
        }
        element.setTextContent(title);
    }

    public void addMetadata(String name, String value) throws XPathExpressionException, Exception {
        Node head = (Node)this.xpath.evaluate("/html/head", document.getDocumentElement(), XPathConstants.NODE);
        if(head == null) {
            throw new Exception("Cannot find head element in document.");
        }
        Element meta = document.createElement("meta");
        meta.setAttribute("name", name);
        meta.setAttribute("content", value);
        head.appendChild(meta);
    }

    public String getOriginalText() throws XPathExpressionException {
        Node node = (Node)this.xpath.evaluate("//div[@id='original']", document.getDocumentElement(), XPathConstants.NODE);
        if(node == null) {
            return null;
        }
        return node.getTextContent();
    }

    public String getOriginalHeading() throws XPathExpressionException {
        Node node = (Node)this.xpath.evaluate("//div[@id='original']/p[@class='heading']", document.getDocumentElement(), XPathConstants.NODE);
        if(node == null) {
            return null;
        }
        return node.getTextContent();
    }

    public List<String> listTranslations() throws XPathExpressionException {
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
