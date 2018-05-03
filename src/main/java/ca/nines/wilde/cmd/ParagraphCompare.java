package ca.nines.wilde.cmd;

import static ca.nines.wilde.cmd.DocCompare.LEVEN_THRESHOLD;
import ca.nines.wilde.doc.DocReader;
import ca.nines.wilde.doc.DocWriter;
import ca.nines.wilde.doc.WildeDoc;
import java.io.IOException;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author mjoyce
 */
public class ParagraphCompare extends Command {

    public static final int MIN_LENGTH = 25;

    public static final double LEVEN_THRESHOLD = 0.6;

    public static final double COSINE_THRESHOLD = 0.9;

    @Override
    public String getDescription() {
        return "Compare all paragraphs.";
    }

    @Override
    public void execute(CommandLine cmd) throws Exception {
        String[] args = this.getArgList(cmd);
        if (args.length == 0) {
            System.err.println(getUsage());
            return;
        }

        List<WildeDoc> corpus = getCorpus(args);
        long comparisons = (corpus.size() * (corpus.size() - 1)) / 2;
        System.out.println("Expect " + comparisons + " total comparisons.");
        long n = 0;
        DocWriter writer = new DocWriter();

        for (int i = 0; i < corpus.size(); i++) {
            WildeDoc documentI = corpus.get(i);
            String lang = documentI.getMetadata("dc.language");
            NodeList parasI = documentI.getParagraphs();
            for (int j = 0; j < i; j++) {
                n++;
                WildeDoc documentJ = corpus.get(j);
                if (documentJ.getMetadata("dc.language").equals(lang)) {
                    NodeList parasJ = documentJ.getParagraphs();
                    for (int a = 0; a < parasI.getLength(); a++) {
                        String texta = normalize(parasI.item(a).getTextContent());
                        if (texta.length() < MIN_LENGTH) {
                            continue;
                        }
                        for (int b = 0; b < parasJ.getLength(); b++) {
                            String textb = normalize(parasJ.item(b).getTextContent());
                            if (textb.length() < MIN_LENGTH) {
                                continue;
                            }

                            double dc = cosine(texta, textb);
                            double dl = levenshtein(texta, textb);
                            if (dl > LEVEN_THRESHOLD) {
                                documentI.addParagraphSimilarity(parasI.item(a), documentJ, parasJ.item(b), dl, "levenshtein");
                                documentJ.addParagraphSimilarity(parasJ.item(b), documentI, parasI.item(a), dl, "levenshtein");
                            }
                            if (dc > COSINE_THRESHOLD) {
                                documentI.addParagraphSimilarity(parasI.item(a), documentJ, parasJ.item(b), dc, "cosine");
                                documentJ.addParagraphSimilarity(parasJ.item(b), documentI, parasI.item(a), dc, "cosine");
                            }
                        }
                    }
                }
                if (n % 25 == 0) {
                    System.out.print('.');
                }
                if (n % (25 * 70) == 0) {
                    System.out.println(" " + n);
                }
            }
            documentI.setParagraphsIndexed();
            writer.write(documentI.getPath(), documentI);
        }

    }

    @Override
    public String getCommandName() {
        return "pc";
    }

    @Override
    public String getUsage() {
        return "java -jar pc <path>...";
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
                .normalize(text.replaceAll("[\\p{Punct}]", " "), Normalizer.Form.NFD)
                .toLowerCase()
                .replaceAll("\\s+", " ")
                .replaceAll("[^a-z0-9 -]", "")
                .trim();
    }

    protected List<WildeDoc> getCorpus(String[] args) throws ParserConfigurationException, IOException, IOException, SAXException, XPathExpressionException {
        List<WildeDoc> corpus = new ArrayList<>(1000);
        DocReader reader = new DocReader();
        for (String arg : args) {
            for (Path p : this.findFiles(arg)) {
                WildeDoc doc = reader.read(p);
                if (!doc.areParagraphsIndexed()) {
                    corpus.add(doc);
                }
            }
        }
        return corpus;
    }

}
