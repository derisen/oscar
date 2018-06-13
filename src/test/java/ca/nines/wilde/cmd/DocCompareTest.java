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
import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

/**
 * @author derisen
 */
public class DocCompareTest {
    
    ClassLoader cl = null;
    File file = null;
    File file2 = null;  
    DocReader reader = null; 
    WildeDoc doc = null;
    WildeDoc doc2 = null;

    public DocCompareTest() throws Exception {
    
        cl = getClass().getClassLoader();
        file = new File("src/test/resources/SimilarDoc.xml");
        file2 = new File("src/test/resources/SimilarDoc2.xml");     
        reader = new DocReader(); 
        doc = reader.read(file.toPath());
        doc2 = reader.read(file2.toPath());
    }
    
    @Test
    public void testExecute() throws Exception {
        System.out.println("TestDocumentCompare");
        
        String[] args = new String[3];
        args[0] = "java -jar oscar.jar dc ";
        args[1] = file.getAbsolutePath();
        args[2] = file2.getAbsolutePath();
        
        CommandLineParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("h", "help", false, "Command description.");
        
        CommandLine cmd = parser.parse(opts, args);

        DocCompare comparer = new DocCompare();
        comparer.execute(cmd);
        
    }
        
        @Test
        public void testExecuteResult() throws Exception {
        
        String textI = normalize(doc.getOriginalText());
        String textJ = normalize(doc2.getOriginalText());
        
        double similarity = levenshtein(textI, textJ);
            
        Document xml = doc.getXmlDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();
        double expResult = Double.parseDouble((String)xpath.evaluate("//link[@class='similarity']/@data-similarity", xml.getDocumentElement(), XPathConstants.STRING));
        assertEquals(similarity, expResult, 0.0f);
       
        Document xml2 = doc2.getXmlDocument();
        XPath xpath2 = XPathFactory.newInstance().newXPath();
        double expResult2 = Double.parseDouble((String)xpath2.evaluate("//link[@class='similarity']/@data-similarity", xml2.getDocumentElement(), XPathConstants.STRING));
        assertEquals(similarity, expResult2, 0.0f);
    }
        

    }
