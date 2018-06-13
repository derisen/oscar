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
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * @author derisen
 */
public class DocWriterTest {
    
        ClassLoader cl = null;
        File file = null;
        Path path = null;
        
        DocumentBuilder builder = null;
        Document doc =  null;
        
        DocReader reader = null;
        WildeDoc wDoc = null;
        
        DocWriter writer = null;
    
    
    public DocWriterTest() throws Exception {
    
        cl = getClass().getClassLoader();
        file = new File(cl.getResource("WriterTestDoc.xml").getFile());
        path = file.toPath();
        
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = builder.newDocument();
        
        reader = new DocReader();
        wDoc = reader.read(path);
        
        writer = new DocWriter();
        
    }
    
    @Test
    public void testWrite() throws Exception {
        System.out.println("WriterTest");
        
        writer.write(path, wDoc);
           
        Validator instance = new Validator();
        List<String> expResult = Collections.<String>emptyList();       
        List<String> result = instance.validate(wDoc);
        assertEquals(expResult, result); 
    }
    
    
}
