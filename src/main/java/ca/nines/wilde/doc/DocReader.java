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
 *
 * @author michael
 */
public class DocReader {

    private final DocumentBuilder db;

    public DocReader() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        db = factory.newDocumentBuilder();
    }

    public WildeDoc read(Path path) throws SAXException, IOException {
        Document doc = db.parse(path.toFile());
        WildeDoc wilde = new WildeDoc(path, doc);
        return wilde;
    }

}
