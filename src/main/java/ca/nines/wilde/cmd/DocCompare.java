/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.wilde.cmd;

import ca.nines.wilde.doc.DocReader;
import ca.nines.wilde.doc.DocWriter;
import ca.nines.wilde.doc.WildeDoc;
import java.io.IOException;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.xml.sax.SAXException;

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
        List<WildeDoc> corpus = getCorpus(args);

        long comparisons = (corpus.size() * (corpus.size()-1)) / 2;
        System.out.println("Expect " + comparisons + " total comparisons.");
        long n = 0;
        DocWriter writer = new DocWriter();

        for (int i = 0; i < corpus.size(); i++) {
            WildeDoc documentI = corpus.get(i);
            String textI = normalize(documentI.getOriginalText());
            String langI = documentI.getMetadata("dc.language");
            for (int j = 0; j < i; j++) {
                WildeDoc documentJ = corpus.get(j);
                if (!documentJ.getMetadata("dc.language").equals(langI)) {
                    continue;
                }
                String textJ = normalize(documentJ.getOriginalText());
                double dc = cosine(textI, textJ);
                double dl = levenshtein(textI, textJ);
                n++;
                if(n % 25 == 0) {
                    System.out.print('.');
                }
                if(n % (25 * 70) == 0) {
                    System.out.println(" " + n);
                }
                if(dl > LEVEN_THRESHOLD) {
                    documentI.addDocSimilarity(documentJ, dl, "levenshtein");
                    documentJ.addDocSimilarity(documentI, dl, "levenshtein");
                }
                if(dc > COSINE_THRESHOLD) {
                    documentI.addDocSimilarity(documentJ, dc, "cosine");
                    documentJ.addDocSimilarity(documentI, dc, "cosine");
                }
            }
            documentI.setDocumentIndexed();
            writer.write(documentI.getPath(), documentI);
        }

    }

    @Override
    public String getCommandName() {
        return "dc";
    }

    @Override
    public String getUsage() {
        return "jar -jar wilde.jar dc <path>...";
    }

    protected double levenshtein(String a, String b) {
        if (a.equals(b)) {
            return 1.0;
        }
        int maxLength = Math.max(a.length(), b.length());
        int limit = (int) Math.ceil(maxLength * (1.0 - LEVEN_THRESHOLD));
        LevenshteinDistance ld = new LevenshteinDistance(limit);
        int distance = ld.apply(a, b);
        if (distance < 0) {
            return 0;
        }
        double similarity = 1.0 - (distance / ((double) maxLength));
        return similarity;
    }

    protected double cosine(String a, String b) {
        CosineDistance cd = new CosineDistance();
        double distance = cd.apply(a, b);
        return 1.0 - distance;
    }

    protected String normalize(String text) {
        return Normalizer
                .normalize(text, Normalizer.Form.NFD)
                .toLowerCase()
                .replaceAll("[\\p{Punct}]", " ")
                .replaceAll("\\s+", " ")
                .replaceAll("[^a-z0-9 -]", "")
                .trim();
    }

    protected List<WildeDoc> getCorpus(String[] args) throws ParserConfigurationException, IOException, IOException, SAXException {
        List<WildeDoc> corpus = new ArrayList<>(1000);
        DocReader reader = new DocReader();
        for (String arg : args) {
            for (Path p : this.findFiles(arg)) {
                corpus.add(reader.read(p));
            }
        }
        return corpus;
    }

}
