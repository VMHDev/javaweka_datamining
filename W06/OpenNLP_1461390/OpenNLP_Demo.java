/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opennlp_1461390;

import static com.sun.org.apache.xerces.internal.util.FeatureState.is;
import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 *
 * @author 1461390
 */
public class OpenNLP_1461390 {

    //Phân tách đoạn văn thành từng câu:
    public static void SentenceDetect(String paragraph) throws InvalidFormatException, IOException {
        InputStream is = new FileInputStream("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W06\\OpenNLP_1461390\\en-sent.bin");
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME sdetector = new SentenceDetectorME(model);
        String sentences[] = sdetector.sentDetect(paragraph);
        System.out.println(sentences[0]);
        System.out.println(sentences[1]);
        is.close();
    }

    //Phân cách câu thành từng từ có nghĩa:
    public static void Tokenize(String sentences) throws InvalidFormatException, IOException {
        InputStream is = new FileInputStream("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W06\\OpenNLP_1461390\\en-token.bin");
        TokenizerModel model = new TokenizerModel(is);
        Tokenizer tokenizer = new TokenizerME(model);
        String tokens[] = tokenizer.tokenize(sentences);
        for (String a : tokens) {
            System.out.println(a);
        }
        is.close();
    }

    //Xác định loại từ của các câu trong đoạn văn:
    public static void POSTag(String pragraph) throws IOException {
        POSModel model = new POSModelLoader().load(new File("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W06\\OpenNLP_1461390\\en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        InputStreamFactory isf = new InputStreamFactory() {
            public InputStream createInputStream() throws IOException {
                return new ByteArrayInputStream(pragraph.getBytes());
            }
        };
        Charset charset = Charset.forName("UTF-8");
        ObjectStream<String> lineStream = new PlainTextByLineStream(isf, charset);

        perfMon.start();
        String line;
        while ((line = lineStream.read()) != null) {
            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);
            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());
            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
    }

    //Xây dựng cây cú pháp:
    public static void Parse(String sentence) throws InvalidFormatException, IOException {
        InputStream is = new FileInputStream("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W06\\OpenNLP_1461390\\en-parser-chunking.bin");
        ParserModel model = new ParserModel(is);
        Parser parser = ParserFactory.create(model);
        Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);
        for (Parse p : topParses) {
            p.show();
        }
        is.close();
    }

    public static void main(String[] args) {
        try {
            //Phân tách câu:
            String paragraph = "I’ve been to the city of St. Petersburg before. That place was very beautiful.";
            SentenceDetect(paragraph);

            //Phân tách từ:
            String sentences = "Hi. How are you? This is Mike.";
            Tokenize(sentences);

            //Xác định loại từ:
            String pragraph = "Hi. How are you? This is Mike.";
            POSTag(pragraph);
            
            //Xây dựng cây cú pháp:
            String sentence = "Programcreek is a very huge and useful website.";
            Parse(sentence);
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(OpenNLP_1461390.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
