/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * @author derisen
 */
public class DocWriterTest {
    
    /**
     * Test of write method, of class DocWriter.
     */
    @Test
    public void testWrite() throws Exception {
        System.out.println("WriterTest");
        
        ClassLoader cl = getClass().getClassLoader();
        File file = new File(cl.getResource("WriterTestDoc.xml").getFile());
        Path path = file.toPath();
        
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
        
        DocReader reader = new DocReader();
        WildeDoc wDoc = reader.read(path);
        assertNotNull(doc);
        assertNotNull(wDoc);
        
        DocWriter writer = new DocWriter();
        writer.write(path, wDoc);
        
        Validator instance = new Validator();
        List<String> expResult = Collections.<String>emptyList();       
        List<String> result = instance.validate(wDoc);
        assertEquals(expResult, result); 
        
        
    }
    
}
