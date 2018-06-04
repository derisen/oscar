/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.io.IOException;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This class contains a method that loads an XML resource 
 * into the project as a WildeDoc object.
 * @author mjoyce
 */

public class DocReader {

    private final DocumentBuilder db;

    /** 
    * Class constructor: Configures a SAX parser to load the XML file into application memory.
    * @throws ParserConfigurationException if not setup correctly.
    */
    public DocReader() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        factory.setFeature("http://xml.org/sax/features/namespaces", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        db = factory.newDocumentBuilder();
    }
    
    /** 
    * @param path Path expression of the file location. 
    * @return a WildeDoc object.
    * @throws SAXException if SAX parser is not correctly configured.
    * @throws IOException if input operation fails.
    */
    public WildeDoc read(Path path) throws SAXException, IOException {
        Document doc = db.parse(path.toFile());
        WildeDoc wilde = new WildeDoc(path, doc);
        return wilde;
    }

}
