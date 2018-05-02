/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import ca.nines.wilde.doc.Cleaner;
import ca.nines.wilde.doc.DocReader;
import ca.nines.wilde.doc.DocWriter;
import ca.nines.wilde.doc.WildeDoc;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.cli.CommandLine;

/**
 *
 * @author michael
 */
public class Clean extends Command {

    @Override
    public String getDescription() {
        return "Remove unneccessary metadata from documents.";
    }

    @Override
    public void execute(CommandLine cmd) throws Exception {
        String[] args = this.getArgList(cmd);
        if(args.length == 0) {
            System.err.println(getUsage());
            return;
        }
        DocReader reader = new DocReader();
        DocWriter writer = new DocWriter();
        Cleaner cleaner = new Cleaner();
        for (String arg : args) {
            for (Path input : this.findFiles(arg)) {
                WildeDoc doc = reader.read(input);
                List<String> removed = cleaner.clean(doc);
                if(removed.size() > 0) {
                    System.out.println(input);
                    for(String s : removed) {
                        System.out.println("  removed: " + s);
                    }
                }
                writer.write(input, doc);
            }
        }
    }

    @Override
    public String getCommandName() {
        return "clean";
    }

    @Override
    public String getUsage() {
        return "java -jar wilde.jar clean <path>...";
    }

}
