/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opennlp_1461390;

import static com.sun.org.apache.xerces.internal.util.FeatureState.is;
import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public static String[] SentenceDetect(String paragraph) throws InvalidFormatException, IOException {
        InputStream is = new FileInputStream("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W06\\OpenNLP_1461390\\en-sent.bin");
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME sdetector = new SentenceDetectorME(model);
        String sentences[] = sdetector.sentDetect(paragraph);
        is.close();
        return sentences;
    }

    //Xác định loại từ của các câu trong đoạn văn:
    //Ref; https://quyetdo289.wordpress.com/2013/01/05/xoa-file-trong-java/
    //http://ngockhuong.com/java/cach-ghi-them-noi-dung-vao-file-trong-java.html
    //http://giasutinhoc.vn/lap-trinh-java-co-ban/doc-va-ghi-file-trong-java-bai-5/
    public static void POSTag(String pragraph, String nameFile) throws IOException {
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
        String pathFile = String.format("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W06\\data\\out\\pos_tagger\\%s", nameFile);

        //Xử lý trường hợp file đã được tạo - ghi trùng dữ liệu:
        File d = new File(pathFile);
        if (d.exists()) {
            d.delete();
        }
        //->

        while ((line = lineStream.read()) != null) {
            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);
            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);

            //Ghi File
            File f = new File(pathFile);
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sample.toString());
            bw.write("\n");
            bw.close();
            //fw.close();
            //->

            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
    }

    //Xây dựng cây cú pháp - ghi ra file:
    public static void ParseFile(String pragraph, String nameFile) throws InvalidFormatException, IOException {
        InputStream is = new FileInputStream("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W06\\OpenNLP_1461390\\en-parser-chunking.bin");
        ParserModel model = new ParserModel(is);
        Parser parser = ParserFactory.create(model);

        //Tách đoạn văn thành từng câu:
        String[] arrSent = SentenceDetect(pragraph);

        String pathFile = String.format("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W06\\data\\out\\parse\\%s", nameFile);

        //Xử lý trường hợp file đã được tạo - ghi trùng dữ liệu:
        File d = new File(pathFile);
        if (d.exists()) {
            d.delete();
        }
        //->

        for (int i = 0; i < arrSent.length; i++) {

            Parse topParses[] = ParserTool.parseLine(arrSent[i], parser, 1);

            for (Parse p : topParses) {
                //Lấy kết quả cây cú pháp:
                StringBuffer contentBuffer = new StringBuffer();
                p.show(contentBuffer);
                String result = contentBuffer.toString();

                //Ghi file:
                File f = new File(pathFile);
                if (!f.exists()) {
                    f.createNewFile();
                }
                FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(result);
                bw.write("\n");
                bw.close();
                //fw.close();
                //->
            }
        }
        is.close();
    }

    //Xây dựng cây cú pháp - in ra màn hình console:
    public static void ParseConsole(String sentence) throws InvalidFormatException, IOException {
        InputStream is = new FileInputStream("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W06\\OpenNLP_1461390\\en-parser-chunking.bin");
        ParserModel model = new ParserModel(is);
        Parser parser = ParserFactory.create(model);
        Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);
        for (Parse p : topParses) {
            p.show();
        }
        is.close();
    }

    //Đọc toàn bộ nội dung file:
    //Ref: https://viblo.asia/khiemnd5/posts/l0rvmxKaGyqA
    public static String readContentFile(String pathFile) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(pathFile)), StandardCharsets.UTF_8);
        return content;
    }

    //Đọc file và xử lý:
    public static void handleFile(String source, String key) throws IOException {
        File file = new File(source);
        String content = "";
        if (file.exists()) {
            if (file.isFile()) {
                if (file.getName().endsWith(key)) {
                    content = readContentFile(file.getAbsolutePath());
                    POSTag(content, file.getName());
                    ParseFile(content, file.getName());

                    //Xuất cây thư mục ra màn hình Console:
                    String[] arrSent = SentenceDetect(content);
                    for (int i = 0; i < arrSent.length; i++) {
                        ParseConsole(arrSent[i]);
                    }
                }
            }
            File[] listFile = file.listFiles();
            if (listFile != null) {
                for (File f : listFile) {
                    handleFile(f.getAbsolutePath(), key);
                }
            }
        } else {
            System.out.println("source không tồn tại");
        }
    }

    //Đọc file và xử lý:
    public static void printTreeDirectory(String source) throws IOException {
       File file = new File(source);
        if (file.exists()) {            
            System.out.println(file.getName());
            File[] listFile = file.listFiles();
            if (listFile != null) {
                for (File f : listFile) {
                    printTreeDirectory(f.getAbsolutePath());
                }
            }
        } else {
            System.out.println("source không tồn tại");
        }
    }


    //Main:
    public static void main(String[] args) {
        try {
            String pathFile = "D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W06\\data";
            String key = ".txt";
            handleFile(pathFile, key);
            printTreeDirectory(pathFile);

        } catch (IOException ex) {
            Logger.getLogger(OpenNLP_1461390.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
