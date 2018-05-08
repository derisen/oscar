/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import ca.nines.wilde.Util.Callback;
import static ca.nines.wilde.Util.Text.cosine;
import static ca.nines.wilde.Util.Text.levenshtein;
import static ca.nines.wilde.Util.Text.normalize;
import ca.nines.wilde.doc.DocWriter;
import ca.nines.wilde.doc.WildeDoc;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.cli.CommandLine;

/**
 *
 * @author michael
 */
public class DocCompare extends Command {

    public static final double LEVEN_THRESHOLD = 0.6;

    public static final double COSINE_THRESHOLD = 0.9;

    @Override
    public String getDescription() {
        return "Run the document-level comparison.";
    }

    @Override
    public void execute(CommandLine cmd) throws Exception {
        String[] args = this.getArgList(cmd);
        if (args.length == 0) {
            System.err.println(getUsage());
            return;
        }
        List<WildeDoc> corpus = getCorpus(args, new Callback() {
            @Override
            public boolean include(WildeDoc doc) throws XPathExpressionException {
                return !doc.isDocumentIndexed();
            }
        });

        long comparisons = (corpus.size() * (corpus.size()-1) ) / 2;
        System.out.println("Expect " + NumberFormat.getNumberInstance(Locale.US).format(comparisons) + " total comparisons.");
        long n = 0;
        DocWriter writer = new DocWriter();

        for (int i = 0; i < corpus.size(); i++) {
            WildeDoc documentI = corpus.get(i);
            String textI = normalize(documentI.getOriginalText());
            String langI = documentI.getMetadata("dc.language");
            for (int j = 0; j < i; j++) {
                n++;
                if (n % 1000 == 0) {
                    System.out.print("\r" + NumberFormat.getNumberInstance(Locale.US).format(n));
                }
                WildeDoc documentJ = corpus.get(j);
                if (!documentJ.getMetadata("dc.language").equals(langI)) {
                    continue;
                }
                String textJ = normalize(documentJ.getOriginalText());
                double similarity = levenshtein(textI, textJ);
                if(similarity > LEVEN_THRESHOLD) {
                    documentI.addDocSimilarity(documentJ, similarity, "levenshtein");
                    documentJ.addDocSimilarity(documentI, similarity, "levenshtein");
                }
            }
            documentI.setDocumentIndexed();
            writer.write(documentI.getPath(), documentI);
        }
        System.out.println("\nDone");

    }

    @Override
    public String getCommandName() {
        return "dc";
    }

    @Override
    public String getUsage() {
        return "jar -jar wilde.jar dc <path>...";
    }

}
