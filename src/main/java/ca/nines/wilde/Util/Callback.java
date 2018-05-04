/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.Util;

import ca.nines.wilde.doc.WildeDoc;

/**
 *
 * @author mjoyce
 */
public interface Callback {

    public boolean include(WildeDoc doc) throws Exception;

}
