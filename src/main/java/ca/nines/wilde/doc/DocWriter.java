/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

/**
 *
 * @author michael
 */
public class DocWriter {

    private final Transformer transformer;

    public DocWriter() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    }

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
        tidy.setXmlOut(true);
        tidy.setSmartIndent(true);
        tidy.setTidyMark(false);
        tidy.setWraplen(Integer.MAX_VALUE);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);

        FileUtils.writeStringToFile(path.toFile(), outputStream.toString("UTF-8"), "UTF-8", false);
    }

}
