/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

/**
 * This class contains a method that formats a WildeDoc object with 
 * appropriate specifications and XML tags.
 * @author michael
 */
public class DocWriter {

    private final Transformer transformer;

    public DocWriter() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    }
    
    
    /**
    * This method formats a given WildeDoc object using the 3rd party utility class 'Tidy'.
    * @param path Path expression of the file location.
    * @param doc WildeDoc object argument. A WildeDoc object points to a modified XML file.
    * @throws TransformerConfigurationException if Transformer is not configured correctly.
    * @throws TransformerException if Transformer does not execute correctly.
    * @throws IOException if input operation fails.
    */
    public void write(Path path, WildeDoc doc) throws TransformerConfigurationException, TransformerException, IOException {
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        Document dom = doc.getXmlDocument();
        dom.getDocumentElement().normalize();
        DOMSource source = new DOMSource(dom);
        transformer.transform(source, result);
        String data = writer.toString();

        Tidy tidy = new Tidy();
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setXmlOut(false);
        tidy.setSmartIndent(true);
        tidy.setTidyMark(false);
        tidy.setErrout(new PrintWriter(new NullOutputStream()));
        tidy.setWraplen(Integer.MAX_VALUE);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        String cleaned = outputStream.toString("UTF-8");

        FileUtils.writeStringToFile(path.toFile(), cleaned, "UTF-8", false);
    }

}
