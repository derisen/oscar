/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.*;

/**
 * @author derisen
 */
public class ValidatorTest {

            
    /**
     * Test of validate method, of class Validator.
     */
    @Test
    public void testValidateForInvalidFile() throws Exception {
        System.out.println("InvalidFileTest");
        
        ClassLoader cl = getClass().getClassLoader();
        File file = new File(cl.getResource("ValidXMLDoc.xml").getFile());
        DocReader reader = new DocReader();
        WildeDoc doc = reader.read(file.toPath());
        assertNotNull(doc);
        
        Validator instance = new Validator();
        List<String> expResult = Collections.<String>emptyList();       
        List<String> result = instance.validate(doc);
        assertEquals(expResult, result);   
    }
    
    /**
     * Test of validate method, of class Validator.
     */
    @Test
    public void testValidateForValidFile() throws Exception {
        System.out.println("ValidFile");
        
        ClassLoader cl = getClass().getClassLoader();
        File file = new File(cl.getResource("InvalidXMLDoc.xml").getFile());
        DocReader reader = new DocReader();
        WildeDoc doc = reader.read(file.toPath());
        assertNotNull(doc);
        
        Validator instance = new Validator();
        
        List<String> expResult = new ArrayList<String>();    
        String errorMessage = "Missing metadata: dc.date";

        expResult.add(errorMessage);
        
        List<String> result = instance.validate(doc);
        assertEquals(expResult, result);   
    }
    
    
}
