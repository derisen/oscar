/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import ca.nines.wilde.doc.Cleaner;
import ca.nines.wilde.doc.DocWriter;
import ca.nines.wilde.doc.WildeDoc;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.commons.cli.CommandLine;

/**
 * This class contains methods that handle command prompt for the
 * the document cleaning process implemented elsewhere.
 * @author mjoyce
 */
public class Clean extends Command {

    @Override
    public String getDescription() {
        return "Remove unneccessary metadata from documents.";
    }

    /**
    * This method executes the clean command provided in the command prompt. 
    * @param cmd CommandLine variable that passes a string as an argument.
    */
    @Override
    public void execute(CommandLine cmd) throws Exception {
        String[] args = this.getArgList(cmd);
        if (args.length == 0) {
            System.err.println(getUsage());
            return;
        }
        DocWriter writer = new DocWriter();
        Cleaner cleaner = new Cleaner();
        List<WildeDoc> corpus = getCorpus(args);
        int i = 1;
        for (WildeDoc doc : corpus) {
            String id = doc.getPath().getParent().toString().replaceAll("[^A-Z]", "").toLowerCase();
            List<String> removed = cleaner.clean(doc);
            if (removed.size() > 0) {
                System.out.println(doc.getPath());
                for (String s : removed) {
                    System.out.println("  removed: " + s);
                }
            }
            LocalDate date = LocalDate.parse(doc.getMetadata("dc.date"), DateTimeFormatter.ofPattern("yyyy-MM-d"));
            String title = doc.getMetadata("dc.publisher") + " - " + date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, y"));
            doc.setTitle(title);
            cleaner.addIdentifiers(doc, id, i);
            doc.setMetadata("wr.path", doc.getPath().toString());
            doc.removeDocumentSimilarities();
            doc.removeParagraphSimilarities();

            writer.write(doc.getPath(), doc);
            i++;
        }
    }

    @Override
    public String getCommandName() {
        return "clean";
    }

    @Override
    public String getUsage() {
        return "java -jar oscar.jar clean <path>...";
    }

}
