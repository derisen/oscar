/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import ca.nines.wilde.Util.Text;
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

public class WordCountTest {
    
    ClassLoader cl = null;
    File file = null;
    WildeDoc doc = null;
    DocReader reader = null;
    
    public WordCountTest() throws Exception {
        cl = getClass().getClassLoader();
        file = new File(cl.getResource("ValidXMLDoc.xml").getFile());
        reader = new DocReader();
        doc = reader.read(file.toPath());
        
        assertNotNull(doc);
        
    }

    @Test
    public void testExecute() throws Exception {
        System.out.println("WordCountTest");
        
        String[] args = new String[2];
        args[0] = "java -jar oscar.jar wc ";
        args[1] = file.getAbsolutePath();
        
        CommandLineParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("h", "help", false, "Command description.");
        
        CommandLine cmd = parser.parse(opts, args);
        
        WordCount counter = new WordCount();
        counter.execute(cmd);

    }
    
    @Test
    public void testExecuteResults() throws Exception {
        
        Document xml = doc.getXmlDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        int expResult = Integer.parseInt((String)xpath.evaluate("//meta[@name='wr.wordcount']/@content", xml.getDocumentElement(), XPathConstants.STRING));       
        System.out.println(expResult);
        
        String content = doc.getOriginalText();
        String heading = doc.getOriginalHeading();
            
        int contentCount = Text.normalize(content).split("\\s+").length;
        int headingCount = Text.normalize(heading).split("\\+s").length;                
                
        int actResult = contentCount - headingCount;       
        
        System.out.println(actResult);
        
        assertEquals(expResult, actResult);
        
    
    }

    
}
