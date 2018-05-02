/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.cli.CommandLine;

/**
 *
 * @author michael
 */
public class WordCount extends Command {

    @Override
    public String getDescription() {
        return "Count and report the words in one or more documents.";
    }

    @Override
    public void execute(CommandLine cmd) throws Exception {
        String[] args = this.getArgList(cmd);
        if(args.length == 0) {
            System.err.println(getUsage());
            return;
        }
        for (String arg : args) {
            System.out.println(arg);
            for (Path p : this.findFiles(arg)) {
                System.out.println("  " + p);
            }
        }
    }

    @Override
    public String getCommandName() {
        return "wc";
    }

    @Override
    public String getUsage() {
        return "java -jar wilde.jar wc <path>...";
    }

}
