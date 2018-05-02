/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import ca.nines.wilde.doc.DocReader;
import ca.nines.wilde.doc.Validator;
import ca.nines.wilde.doc.WildeDoc;
import java.nio.file.Path;
import org.apache.commons.cli.CommandLine;

/**
 *
 * @author michael
 */
public class Validate extends Command {

    @Override
    public String getDescription() {
        return "Check that the required metadata elements are present.";
    }

    @Override
    public void execute(CommandLine cmd) throws Exception {
        String[] args = this.getArgList(cmd);
        if(args.length == 0) {
            System.err.println(getUsage());
            return;
        }
        DocReader reader = new DocReader();
        Validator validator = new Validator();
        for (String arg : args) {
            for (Path p : this.findFiles(arg)) {
                WildeDoc doc = reader.read(p);
                for(String s : validator.validate(doc)) {
                    System.out.println(p);
                    System.out.println("  ERROR: " + s);
                }
            }
        }
    }

    @Override
    public String getCommandName() {
        return "validate";
    }

    @Override
    public String getUsage() {
        return "java -jar wilde.jar validate <path>...";
    }

}
