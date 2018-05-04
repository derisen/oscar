/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import ca.nines.wilde.doc.DocWriter;
import ca.nines.wilde.doc.Validator;
import ca.nines.wilde.doc.WildeDoc;
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
        if (args.length == 0) {
            System.err.println(getUsage());
            return;
        }
        DocWriter writer = new DocWriter();
        for (WildeDoc doc : getCorpus(args)) {
            String text = doc.getOriginalText();
            int count = text.trim().split("\\s+").length;
            doc.addMetadata("wr.wordcount", Integer.toString(count));
            writer.write(doc.getPath(), doc);
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
