/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import ca.nines.wilde.Util.Callback;
import ca.nines.wilde.doc.DocReader;
import ca.nines.wilde.doc.WildeDoc;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.atteo.classindex.ClassIndex;
import org.atteo.classindex.IndexSubclasses;
import org.xml.sax.SAXException;

/**
 *
 * @author michael
 */
@IndexSubclasses
abstract public class Command {

    private static final Map<String, Command> commandList = new TreeMap<>();

    abstract public String getDescription();

    abstract public void execute(CommandLine cmd) throws Exception;

    abstract public String getCommandName();

    abstract public String getUsage();

    public static Map<String, Command> getCommandList() throws InstantiationException, IllegalAccessException {
        if (commandList.isEmpty()) {
            for (Class<?> cls : ClassIndex.getSubclasses(Command.class)) {
                Command cmd = (Command) cls.newInstance();
                commandList.put(cmd.getCommandName(), cmd);
            }
        }
        return commandList;
    }

    public List<Path> findFiles(String root) throws IOException {
        return findFiles(Paths.get(root));
    }

    public List<Path> findFiles(Path root) throws IOException {
        Stream<Path> filePathStream = Files.find(root, 4, (path, attr) -> String.valueOf(path).endsWith(".xml"));
        ArrayList<Path> pathList = filePathStream.collect(Collectors.toCollection(ArrayList::new));
        return pathList;
    }

    protected List<WildeDoc> getCorpus(String[] args) throws ParserConfigurationException, IOException, IOException, SAXException, XPathExpressionException {
        return getCorpus(args, new Callback() {
            @Override
            public boolean include(WildeDoc doc) {
                return true;
            }
        });
    }

    protected List<WildeDoc> getCorpus(String[] args, Callback filter) throws ParserConfigurationException, IOException, IOException, SAXException, XPathExpressionException {
        List<WildeDoc> corpus = new ArrayList<>(1000);
        DocReader reader = new DocReader();
        for (String arg : args) {
            for (Path p : this.findFiles(arg)) {
                WildeDoc doc = reader.read(p);
                if(filter.include(doc)) {
                    corpus.add(doc);
                }
            }
        }
        return corpus;
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
