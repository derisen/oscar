/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import ca.nines.wilde.Util.Text;
import ca.nines.wilde.doc.DocWriter;
import ca.nines.wilde.doc.WildeDoc;
import org.apache.commons.cli.CommandLine;

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
            String content = doc.getOriginalText();
            if(content == null) {
                System.out.println(doc.getPath() + " is missing div#original");
                continue;
            }
            String heading = doc.getOriginalHeading();
            if(heading == null) {
                System.out.println(doc.getPath() + " is missing a heading");
                heading = "";
            }

            int contentCount = Text.normalize(content).split("\\s+").length;
            int headingCount = Text.normalize(heading).split("\\+s").length;
            doc.setMetadata("wr.wordcount", Integer.toString(contentCount - headingCount));
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
