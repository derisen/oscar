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
 *
 * @author michael
 */
public class Validator {

    private static final String[] REQUIRED = {
        "dc.date",
        "dc.publisher",
        "dc.region",
        "dc.language",
    };

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
