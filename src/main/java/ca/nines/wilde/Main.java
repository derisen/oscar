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
 *
 * @author michael
 */
public class Main {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ParseException, Exception {
        Map<String, Command> commandList = Command.getCommandList();

        String commandName = "help";
        if (args.length > 0) {
            commandName = args[0];
        }
        if (!commandList.containsKey(commandName)) {
            System.err.println("Unknown command: " + commandName);
            commandName = "help";
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
            formatter.printHelp("java -jar wip.jar " + cmd.getCommandName() + " [options]", opts);
            System.out.println(cmd.getDescription());
        } else {
            cmd.execute(commandLine);
        }
    }

}
