/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author mjoyce
 */
public class DocReaderTest {

    /**
     * Test of read method, of class DocReader.
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("ReaderTest");
        
        ClassLoader cl = getClass().getClassLoader();
        File file = new File(cl.getResource("ValidXMLDoc.xml").getFile());
        DocReader reader = new DocReader();
        WildeDoc doc = reader.read(file.toPath());
        assertNotNull(doc);               
    }

}
