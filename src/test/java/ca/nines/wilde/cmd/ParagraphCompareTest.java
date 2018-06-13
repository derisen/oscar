/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import static ca.nines.wilde.Util.Text.levenshtein;
import static ca.nines.wilde.Util.Text.normalize;
import ca.nines.wilde.doc.DocReader;
import ca.nines.wilde.doc.WildeDoc;
import java.io.File;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author derisen
 */
public class ParagraphCompareTest {
    
    
    ClassLoader cl = null;
        
    File file = null;
    File file2 = null;
    
    DocReader reader = null;
        
    WildeDoc doc = null;
    WildeDoc doc2 = null;
        
    public ParagraphCompareTest() throws Exception {
    
        cl = getClass().getClassLoader();
        
        file = new File("src/test/resources/SimilarDoc.xml");
        file2 = new File("src/test/resources/SimilarDoc2.xml");
        
        reader = new DocReader();
        
        doc = reader.read(file.toPath());
        doc2 = reader.read(file2.toPath());
    
    }

    @Test
    public void testExecute() throws Exception {
        System.out.println("TestParagraphCompare");
        
        
        String[] args = new String[3];
        args[0] = "java -jar oscar.jar pc "; 
        args[1] = file.getAbsolutePath();
        args[2] = file2.getAbsolutePath();
        
        
        CommandLineParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("h", "help", false, "Command description.");
        
        CommandLine cmd = parser.parse(opts, args);
        ParagraphCompare comparer = new ParagraphCompare();
        comparer.execute(cmd);

    }
    
    @Test
    public void testExecuteResults() throws Exception {
    
        
        NodeList parasI = doc.getParagraphs();
        NodeList parasJ = doc2.getParagraphs();
        
        String texta = null;
        String textb = null;

        for (int a = 0; a < parasI.getLength(); a++) {
            Element parI = (Element) parasI.item(a);
            texta = normalize(parI.getTextContent());
        
            for (int b = 0; b < parasJ.getLength(); b++) {
                Element parJ = (Element) parasJ.item(b);
                textb = normalize(parJ.getTextContent());
                }
        }
        
        double similarity = levenshtein(texta, textb);
    
        Document xml = doc.getXmlDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();
        double expResult = Double.parseDouble((String)xpath.evaluate("//a[@class='similarity']/@data-similarity", xml.getDocumentElement(), XPathConstants.STRING));
        assertEquals(similarity, expResult, 0.0f);
        
        Document xml2 = doc2.getXmlDocument();
        XPath xpath2 = XPathFactory.newInstance().newXPath();
        double expResult2 = Double.parseDouble((String)xpath2.evaluate("//a[@class='similarity']/@data-similarity", xml2.getDocumentElement(), XPathConstants.STRING));
        assertEquals(similarity, expResult2, 0.0f);
        
        assertEquals(expResult, expResult2, 0.0f);
        
        
    }
    
}
