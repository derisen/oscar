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

/**
 * @author derisen
 */
public class DocCompareTest {
    
    /**
     * Test of execute method, of class DocCompare.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("TestDocumentCompare");
        
        ClassLoader cl = getClass().getClassLoader();
        
        File file = new File("src/test/resources/SimilarDoc.xml");
        File file2 = new File("src/test/resources/SimilarDoc2.xml");
        
        DocReader reader = new DocReader();
        
        WildeDoc doc = reader.read(file.toPath());
        WildeDoc doc2 = reader.read(file2.toPath());
        
        String textI = normalize(doc.getOriginalText());
        String textJ = normalize(doc2.getOriginalText());
        
        double similarity = levenshtein(textI, textJ);
        System.out.println(String.valueOf(similarity));
        
        String[] args = new String[3];
        args[0] = "java -jar oscar.jar dc ";
        args[1] = file.getAbsolutePath();
        args[2] = file2.getAbsolutePath();
        
        System.out.println(args[0]+args[1]+" "+args[2]);
        
        CommandLineParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("h", "help", false, "Command description.");
        
        CommandLine cmd = parser.parse(opts, args);

        DocCompare comparer = new DocCompare();
        comparer.execute(cmd);
                      
        Document xml = doc.getXmlDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();
        double expResult = Double.parseDouble((String)xpath.evaluate("//link[@class='similarity']/@data-similarity", xml.getDocumentElement(), XPathConstants.STRING));
        System.out.println(String.valueOf(expResult));

        assertEquals(similarity, expResult, 0.0f);
       
        Document xml2 = doc2.getXmlDocument();
        XPath xpath2 = XPathFactory.newInstance().newXPath();
        double expResult2 = Double.parseDouble((String)xpath2.evaluate("//link[@class='similarity']/@data-similarity", xml2.getDocumentElement(), XPathConstants.STRING));
        System.out.println(String.valueOf(expResult2));
        
        assertEquals(similarity, expResult2, 0.0f);
        
        

    }

    
}
