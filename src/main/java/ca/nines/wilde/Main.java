/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde;

import ca.nines.wilde.cmd.Command;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

/**
 * This class handles the main command prompt interface
 * of the application. It accepts a set of commands, and,
 * if input does not match to any element in the set, throws an exception and 
 * redirects to correct usage.
 * @author mjoyce
 */
public class Main {
    
    /** 
    * Main method.
    * @param args string argument as command.
    * @throws IllegalArgumentException if input does not match commands set.
    * @throws InstantiationException if an application tries to create an instance of a class but cannot do so.
    * @throws IllegalAccessException if an application tries to reflectively create an instance but cannot do so.
    * @throws ParseException if input contains invalid characters.
    * @throws UnrecognizedOptionException if input does not match options set.
    * @throws Exception if anything else.
    */
    public static void main(String[] args) throws IllegalArgumentException, InstantiationException, IllegalAccessException, ParseException, Exception {
        Map<String, Command> commandList = Command.getCommandList();

        String commandName = "help";
        if (args.length > 0) {
            commandName = args[0];
        }
        
        if(!commandList.containsKey(commandName)) {
                commandName = "help";
                throw new IllegalArgumentException("Unknown command: " + commandName);
        }
       

        Command cmd = commandList.get(commandName);
        Options opts = cmd.getOptions();
        CommandLine commandLine = null;
        try {
            commandLine = cmd.getCommandLine(opts, args);
        } catch (UnrecognizedOptionException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        
        if (commandLine.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar oscar.jar " + cmd.getCommandName() + " [options]", opts);
            System.out.println(cmd.getDescription());
        } else {
            cmd.execute(commandLine);
        }
    }

}
