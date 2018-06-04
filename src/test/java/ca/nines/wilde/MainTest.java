/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde;

import org.junit.Test;
import org.apache.commons.cli.UnrecognizedOptionException;

/**
 * @author dogan.erisen
 */
public class MainTest {
    
    /**
     * Test of main method, of class Main.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testMainForInvalidCommand() throws Exception {
        System.out.println("InvalidCommandTest");
 
        String[] args = new String[1];
        args[0] = "noSuchCommand";
        Main.main(args);
    }
    
    /**
     * Another test of main method, of class Main.
     */
    @Test (expected = UnrecognizedOptionException.class)
    public void testMainForInvalidOption() throws Exception {
        System.out.println("InvalidOptionTest");
        
        String[] args = new String[2];
        args[0] = "wc";
        args[1] = "--noSuchOption";
        Main.main(args);
    }
    
    /**
     * Another test of main method, of class Main.
     */
    @Test
    public void testMainForValidCommandAndOption() throws Exception{
        System.out.println("ValidCommandAndOptionTest");
        
        String[] args = new String[2];
        args[0] = "wc";
        args[1] = "--help";
        Main.main(args);
    
    }
    
}
