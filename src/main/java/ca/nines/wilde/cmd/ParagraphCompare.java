package ca.nines.wilde.cmd;

import ca.nines.wilde.Util.Callback;
import static ca.nines.wilde.Util.Text.levenshtein;
import static ca.nines.wilde.Util.Text.normalize;
import ca.nines.wilde.doc.DocWriter;
import ca.nines.wilde.doc.WildeDoc;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.cli.CommandLine;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

        List<WildeDoc> corpus = getCorpus(args, new Callback() {
            @Override
            public boolean include(WildeDoc doc) throws XPathExpressionException {
                return !doc.areParagraphsIndexed();
            }
        });

        long comparisons = (corpus.size() * (corpus.size() - 1)) / 2;
        System.out.println("Expect " + NumberFormat.getNumberInstance(Locale.US).format(comparisons) + " total comparisons.");
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
                        Element parI = (Element) parasI.item(a);
                        if (parI.hasAttribute("class") && parI.getAttribute("class").contains("heading")) {
                            continue;
                        }
                        String texta = normalize(parI.getTextContent());
                        if (texta.length() < MIN_LENGTH) {
                            continue;
                        }
                        for (int b = 0; b < parasJ.getLength(); b++) {
                            Element parJ = (Element) parasJ.item(b);
                            if (parJ.hasAttribute("class") && parJ.getAttribute("class").contains("heading")) {
                                continue;
                            }
                            String textb = normalize(parJ.getTextContent());
                            if (textb.length() < MIN_LENGTH) {
                                continue;
                            }

                            double similarity = levenshtein(texta, textb);
                            if (similarity > LEVEN_THRESHOLD) {
                                documentI.addParagraphSimilarity(parI, documentJ, parJ, similarity, "levenshtein");
                                documentJ.addParagraphSimilarity(parJ, documentI, parI, similarity, "levenshtein");
                            }
                        }
                    }
                }
                if (n % 1000 == 0) {
                    System.out.println("\r" + NumberFormat.getNumberInstance(Locale.US).format(n));
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

}
