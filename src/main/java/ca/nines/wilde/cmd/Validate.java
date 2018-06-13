/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import ca.nines.wilde.doc.Validator;
import ca.nines.wilde.doc.WildeDoc;
import org.apache.commons.cli.CommandLine;

/**
 * This class contains a method that handles the validation process for an XML document passed as
 * a WildeDoc object.
 * @author mjoyce
 */
public class Validate extends Command {

    @Override
    public String getDescription() {
        return "Check that the required metadata elements are present.";
    }

    @Override
    public void execute(CommandLine cmd) throws Exception {
        String[] args = this.getArgList(cmd);
        if (args.length == 0) {
            System.err.println(getUsage());
            return;
        }
        Validator validator = new Validator();
        for (WildeDoc doc : getCorpus(args)) {
            for (String s : validator.validate(doc)) {
                System.out.println(doc.getPath());
                System.out.println("  ERROR: " + s);
            }
        }
    }

    @Override
    public String getCommandName() {
        return "validate";
    }
    

    @Override
    public String getUsage() {
        return "java -jar oscar.jar validate <path>...";
    }

}
