/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.atteo.classindex.ClassIndex;
import org.atteo.classindex.IndexSubclasses;

/**
 *
 * @author michael
 *
 */
@IndexSubclasses
abstract public class Command {
    private static final Map<String, Command> commandList = new TreeMap<>();

    abstract public String getDescription();

    abstract public void execute(CommandLine cmd) throws Exception;

    abstract public String getCommandName();

    abstract public String getUsage();

    public static Map<String, Command> getCommandList() throws InstantiationException, IllegalAccessException {
        if(commandList.isEmpty()) {
            for(Class<?> cls : ClassIndex.getSubclasses(Command.class)) {
                Command cmd = (Command) cls.newInstance();
                commandList.put(cmd.getCommandName(), cmd);
            }
        }
        return commandList;
    }

    public Options getOptions() {
        Options opts = new Options();
        opts.addOption("h", "help", false, "Command description.");
        return opts;
    }

    public CommandLine getCommandLine(Options opts, String[] args) throws ParseException {
        CommandLine cmd;
        CommandLineParser parser = new DefaultParser();
        cmd = parser.parse(opts, args);
        return cmd;
    }

    public String[] getArgList(CommandLine commandLine) {
        List<String> argList = commandLine.getArgList();
        argList = argList.subList(1, argList.size());
        String[] args = argList.toArray(new String[argList.size()]);
        return args;
    }

    public void help() {
        HelpFormatter formatter = new HelpFormatter();
        Options opts = getOptions();
        System.out.println(getDescription());
        if (opts.getOptions().size() > 0) {
            formatter.printHelp(this.getClass().getSimpleName().toLowerCase() + " " + getUsage(), opts);
        } else {
            System.out.println(this.getClass().getSimpleName().toLowerCase() + " " + getUsage());
        }
    }

}
