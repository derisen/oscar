/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.doc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;

/**
 * This class contains a method that validates a given WildeDoc object.
 * @author michael
 */
public class Validator {
    
    /**
    * List of XML tags that are required.
    */
    private static final String[] REQUIRED = {
        "dc.date",
        "dc.publisher",
        "dc.region",
        "dc.language",
    };
    
    /**
    * This method checks whether the tags in string List REQUIRED above is present
    * in the provided XML file. 
    * @param doc a WildeDoc object.
    * @return a list of strings.
    * @throws XPathExpressionException if path expression provided is not correct.
    */
    public List<String> validate(WildeDoc doc) throws XPathExpressionException {
        List<String> errors = new ArrayList<>();

        for(String name : REQUIRED) {
            if(doc.getMetadata(name) == null) {
                errors.add("Missing metadata: " + name);
            }
        }

        return errors;
    }

}
