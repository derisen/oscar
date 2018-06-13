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
    
        ClassLoader cl = null;
        File file = null;
        DocReader reader = null;
        WildeDoc doc = null;
    
    public ValidatorTest() throws Exception {
    
        cl = getClass().getClassLoader();
        file = new File(cl.getResource("ValidXMLDoc.xml").getFile());
        reader = new DocReader();
        doc = reader.read(file.toPath());
    }
            
    @Test
    public void testValidateForInvalidFile() throws Exception {
        System.out.println("InvalidFileTest");
        
        Validator instance = new Validator();
        List<String> expResult = Collections.<String>emptyList();       
        List<String> result = instance.validate(doc);
        assertEquals(expResult, result);   
    }
    
    @Test
    public void testValidateForValidFile() throws Exception {
        System.out.println("ValidFile");
        
        Validator instance = new Validator();
        
        doc.removeMetadata("dc.date");
        
        List<String> expResult = new ArrayList<String>();    
        String errorMessage = "Missing metadata: dc.date";

        expResult.add(errorMessage);
        
        List<String> result = instance.validate(doc);
        assertEquals(expResult, result);   
    }
    
    
}
