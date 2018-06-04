/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author derisen
 */
public class WildeDocTest {
    
    WildeDoc wildeDoc = null;
    Path path = null;
    XPath xpath = null;
    Document xml = null;

    public WildeDocTest() throws Exception {
    
        ClassLoader cl = getClass().getClassLoader();
        File file = new File(cl.getResource("ValidXMLDoc.xml").getFile());
        path = file.toPath(); 
        
        xpath = XPathFactory.newInstance().newXPath();
        String spath = file.toPath().toString();
        xpath.compile(spath);
        
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
        
        DocReader reader = new DocReader();
        wildeDoc = reader.read(path);
        assertNotNull(doc);
        
        xml = wildeDoc.getXmlDocument();
    }
  

//    /**
//     * Test of removeParagraphSimilarities method, of class WildeDoc.
//     */
//    @Test
//    public void testRemoveParagraphSimilarities() throws Exception {
//        System.out.println("removeParagraphSimilarities");
//        wildeDoc.removeParagraphSimilarities();
//        
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of removeDocumentSimilarities method, of class WildeDoc.
//     */
//    @Test
//    public void testRemoveDocumentSimilarities() throws Exception {
//        System.out.println("removeDocumentSimilarities");
//        wildeDoc.removeDocumentSimilarities();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of listMetadataNames method, of class WildeDoc.
//     */
//    @Test
//    public void testListMetadataNames() throws Exception {
//        System.out.println("listMetadataNames");
//        WildeDoc instance = null;
//        List<String> expResult = null;
//        List<String> result = instance.listMetadataNames();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getMetadata method, of class WildeDoc.
//     */
//    @Test
//    public void testGetMetadata() throws Exception {
//        System.out.println("getMetadata");
//        String name = "";
//        WildeDoc instance = null;
//        String expResult = "";
//        String result = instance.getMetadata(name);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of removeMetadata method, of class WildeDoc.
//     */
//    @Test
//    public void testRemoveMetadata() throws Exception {
//        System.out.println("removeMetadata");
//        String name = "";
//        WildeDoc instance = null;
//        instance.removeMetadata(name);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setMetadata method, of class WildeDoc.
//     */
//    @Test
//    public void testSetMetadata() throws Exception {
//        System.out.println("setMetadata");
//        String name = "";
//        String value = "";
//        WildeDoc instance = null;
//        instance.setMetadata(name, value);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setTitle method, of class WildeDoc.
//     */
//    @Test
//    public void testSetTitle() throws Exception {
//        System.out.println("setTitle");
//        String title = "";
//        WildeDoc instance = null;
//        instance.setTitle(title);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addMetadata method, of class WildeDoc.
//     */
//    @Test
//    public void testAddMetadata() throws Exception {
//        System.out.println("addMetadata");
//        String name = "";
//        String value = "";
//        WildeDoc instance = null;
//        instance.addMetadata(name, value);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//
//    /**
//     * Test of listTranslations method, of class WildeDoc.
//     */
//    @Test
//    public void testListTranslations() throws Exception {
//        System.out.println("listTranslations");
//        WildeDoc instance = null;
//        List<String> expResult = null;
//        List<String> result = instance.listTranslations();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getTranslatedText method, of class WildeDoc.
//     */
//    @Test
//    public void testGetTranslatedText() throws Exception {
//        System.out.println("getTranslatedText");
//        String lang = "";
//        WildeDoc instance = null;
//        String expResult = "";
//        String result = instance.getTranslatedText(lang);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    
}
