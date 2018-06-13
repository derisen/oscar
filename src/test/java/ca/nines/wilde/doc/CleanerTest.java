/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.util.ArrayList;
import java.io.File;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author dogan.erisen
 */
public class CleanerTest {   

    ClassLoader cl = null;
    File file = null;
    DocReader reader = null;
    WildeDoc doc = null;
            
    public CleanerTest() throws Exception {
    
        cl = getClass().getClassLoader();
        file = new File(cl.getResource("ValidXMLDoc.xml").getFile());
        reader = new DocReader();
        doc = reader.read(file.toPath());
        assertNotNull(doc);
    }
    
    
    @Test
    public void testClean() throws Exception {
        System.out.println("TestCleaner");
        
        Cleaner instance = new Cleaner();
        
        List<String> expResult = new ArrayList<>();
        expResult.add("wr.wordcount");
        expResult.add("index.document");
        expResult.add("index.paragraph");
        
        List<String> result = instance.clean(doc);
        assertEquals(expResult, result);
        
    }
    

    @Test
    public void testAddIdentifiers() throws Exception {
        System.out.println("addIdentifiers");
      
        String docId = "XYZ";
        int n = 0;
        Cleaner instance = new Cleaner();
        instance.addIdentifiers(doc, docId, n);
       
        Document xml = doc.getXmlDocument();
        Element root = xml.getDocumentElement();
        
        if (root.hasAttribute("id"))
            System.out.println("This document has an identifier");
        else 
            System.out.println("This document has no identifier");
    }
    
}
