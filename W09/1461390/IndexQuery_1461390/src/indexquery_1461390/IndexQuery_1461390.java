/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexquery_1461390;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

/**
 *
 * @author 1461390
 */
public class IndexQuery_1461390 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String docs[] = {"Web mining is useful", "Usage mining applications", "Web structure mining studies the Web hyperlink structure"};

        String[] NguVung = getUniqueTokens(docs);

        ArrayList<Map<String, Integer>> frequent = getFrequent(docs, NguVung);

        ArrayList< ArrayList<Integer>> reverseIndex = getReverseIndex(docs, NguVung);


        //YOUR CODE HERE: ok
        String query_string;
        System.out.println("Nhập câu truy vấn: ");
        Scanner scan =new Scanner(System.in);
        query_string = scan.nextLine();
        printSearch(query_string, NguVung, frequent, reverseIndex);
        
        //In ket qua danh sach chi muc dao ra file
        //Ref: https://ngockhuong.com/java/cach-ghi-them-noi-dung-vao-file-trong-java.html
        String stringPath = "D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W09\\1461390\\output.txt";
        File file = new File(stringPath);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
    
        String[] query = tokenizeAndStemmingAndRemoveDefaultStopWords(query_string);
        ArrayList<SearchItem> searchResult = search(query, NguVung, frequent, reverseIndex);
        for (int i = 0; i < searchResult.size(); i++) {
            SearchItem item = searchResult.get(i);
            String data = Integer.toString(item.docIdx) + ": " + Integer.toString(item.count) + "\n";
            bw.write(data);
        }
        
        if (bw != null) {
            bw.close();
        }
        if (fw != null) {
            fw.close();
        }
        
        //Xuất ra màn hình:
        System.out.println("Output My Teacher");
        query_string = "Web";
        printSearch(query_string, NguVung, frequent, reverseIndex);
        System.out.println("================");
        query_string = "Web Mining";
        printSearch(query_string, NguVung, frequent, reverseIndex);
        System.out.println("================");
        query_string = "Usage mining";
        printSearch(query_string, NguVung, frequent, reverseIndex);
        System.out.println("================");
        query_string = "mining";
        printSearch(query_string, NguVung, frequent, reverseIndex);
    }

    public static String[] getUniqueTokens(String[] docs) throws InvalidFormatException, IOException {
        PorterStemmer porterstemmer = new PorterStemmer();
        Set<String> uniquetokens = new HashSet<String>();
        for (int k = 0; k < docs.length; k++) {
            String[] tokens = tokenizeAndRemoveDefaultStopWords(docs[k].toLowerCase());
            for (int i = 0; i < tokens.length; i++) {
                uniquetokens.add(porterstemmer.stem(tokens[i]));
            }
        }
        return uniquetokens.toArray(new String[uniquetokens.size()]);
    }

    public static String[] tokenizeAndRemoveDefaultStopWords(String input) throws InvalidFormatException, IOException {
        //YOUR CODE HERE: ok
        String[] stopWords = getStopWord("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W09\\1461390\\stopword.txt");

        String[] tokens = tokenize(input);
        tokens = removeStopWords(tokens, stopWords);
        return tokens;
    }

    public static String[] getStopWord(String stopWordFilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(stopWordFilePath));
        String line;
        ArrayList<String> array_stopWord = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            array_stopWord.add(line);
        }
        return array_stopWord.toArray(new String[array_stopWord.size()]);
    }

    private static String[] tokenize(String input) throws InvalidFormatException, IOException {
        //YOUR CODE HERE: ok
        InputStream is = new FileInputStream("D:\\Couses_VI\\KTDLWeb\\15CK3\\TH\\W09\\1461390\\en-token.bin");
        TokenizerModel model = new TokenizerModel(is);
        Tokenizer tokenizer = new TokenizerME(model);
        String tokens[] = tokenizer.tokenize(input);

        is.close();

        return tokens;
    }

    public static String[] removeStopWords(String[] tokens, String[] stopWords) {
        ArrayList<String> tokensList = new ArrayList<String>();
        for (int i = 0; i < tokens.length; i++) {
            tokensList.add(tokens[i]);
        }
        for (int i = tokensList.size() - 1; i > 0; i--) {
            if (isContains(stopWords, tokens[i])) {
                tokensList.remove(i);
            }
        }
        return tokensList.toArray(new String[tokensList.size()]);
    }

    private static boolean isContains(String[] set, String item) {
        boolean res = false;
        for (int i = 0; i < set.length; i++) {
            if (item.equalsIgnoreCase(set[i])) {
                res = true;
            }
        }
        return res;
    }

    public static ArrayList<Map<String, Integer>> getFrequent(String[] docs, String[] NguVung) throws InvalidFormatException, IOException {
        ArrayList<Map<String, Integer>> frequent = new ArrayList<Map<String, Integer>>();
        for (int i = 0; i < docs.length; i++) {
            frequent.add(getFrequentVector(NguVung, docs[i]));
        }
        return frequent;
    }

    public static Map<String, Integer> getFrequentVector(String[] NguVung, String doc) throws InvalidFormatException, IOException {
        Map<String, Integer> frequentVector = new HashMap<String, Integer>();
        for (int i = 0; i < NguVung.length; i++) {
            frequentVector.put(NguVung[i], 0);
        }
        String doc_token[] = tokenizeAndStemmingAndRemoveDefaultStopWords(doc);
        for (int i = 0; i < doc_token.length; i++) {
            int currentCount = frequentVector.get(doc_token[i]);
            frequentVector.put(doc_token[i], currentCount + 1);
        }

        return frequentVector;
    }

    public static String[] tokenizeAndStemmingAndRemoveDefaultStopWords(String input) throws InvalidFormatException, IOException {
        String tokens[] = tokenizeAndRemoveDefaultStopWords(input.toLowerCase());
        String stemmed_tokens[] = new String[tokens.length];
        PorterStemmer porterstemmer = new PorterStemmer();
        for (int i = 0; i < tokens.length; i++) {
            stemmed_tokens[i] = porterstemmer.stem(tokens[i]);
        }
        return stemmed_tokens;
    }

    public static ArrayList< ArrayList<Integer>> getReverseIndex(String docs[], String NguVung[]) throws InvalidFormatException, IOException {
        ArrayList< ArrayList<Integer>> reverseIndex = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < NguVung.length; i++) {
            reverseIndex.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < docs.length; i++) {
            Map<String, Integer> frequentVector = getFrequentVector(NguVung, docs[i]);
            for (int j = 0; j < NguVung.length; j++) {
                if (frequentVector.get(NguVung[j]) > 0) {
                    ArrayList<Integer> reverseIndexItem = reverseIndex.get(j);
                    reverseIndexItem.add(i);
                    reverseIndex.set(j, reverseIndexItem);
                }
            }
        }
        return reverseIndex;
    }

    public static void printSearch(String query_string, String[] NguVung, ArrayList<Map<String, Integer>> frequent, ArrayList< ArrayList<Integer>> reverseIndex)
            throws InvalidFormatException, IOException {

        String[] query = tokenizeAndStemmingAndRemoveDefaultStopWords(query_string);
        ArrayList<SearchItem> searchResult = search(query, NguVung, frequent, reverseIndex);
        for (int i = 0; i < searchResult.size(); i++) {
            SearchItem item = searchResult.get(i);
            System.out.println(Integer.toString(item.docIdx) + ": " + Integer.toString(item.count));
        }
    }

    public static ArrayList<SearchItem> search(String query[], String[] NguVung, ArrayList<Map<String, Integer>> frequent, ArrayList< ArrayList<Integer>> reverseIndex) {
        ArrayList<SearchItem> result = search1word(query[0], NguVung, frequent, reverseIndex);
        for (int i = 1; i < query.length; i++) {
            ArrayList<SearchItem> temp = search1word(query[i], NguVung, frequent, reverseIndex);
            result = combineResult(result, temp);
        }
        result.sort((Comparator<? super SearchItem>) new SearchItemComparator());
        return result;
    }

    public static ArrayList<SearchItem> search1word(String query, String[] NguVung, ArrayList<Map<String, Integer>> frequent, ArrayList< ArrayList<Integer>> reverseIndex) {
        int idx = getIndexNguVung(query, NguVung);
        ArrayList<SearchItem> result = new ArrayList<SearchItem>();
        ArrayList<Integer> reverseIndexItem = reverseIndex.get(idx);
        for (int i = 0; i < reverseIndexItem.size(); i++) {
            int docIdx = reverseIndexItem.get(i);
            int wordFrequent = frequent.get(docIdx).get(NguVung[idx]);
            SearchItem searchitem = new SearchItem();
            searchitem.docIdx = docIdx + 1;
            //YOUR CODE HERE
            searchitem.count = wordFrequent;
            result.add(searchitem);
        }
        return result;
    }

    public static int getIndexNguVung(String word, String[] NguVung) {
        int res = -1;
        for (int i = 0; i < NguVung.length; i++) {
            if (word.equalsIgnoreCase(NguVung[i])) {
                return i;
            }
        }
        return res;
    }

    private static ArrayList<SearchItem> combineResult(
            ArrayList<SearchItem> A, ArrayList<SearchItem> B) {
        ArrayList<SearchItem> result = new ArrayList<SearchItem>();
        for (int i = 0; i < A.size(); i++) {
            SearchItem a = A.get(i);
            for (int j = 0; j < B.size(); j++) {
                SearchItem b = B.get(j);
                if (a.docIdx == b.docIdx) {
                    SearchItem newItem = new SearchItem();
                    newItem.docIdx = a.docIdx;
                    //YOUR CODE HERE: ok
                    newItem.count = min(a.count, b.count);
                    result.add(newItem);
                }
            }

        }
        return result;
    }

    private static int min(int a, int b) {
        //YOUR CODE HERE: ok
        if (a < b) {
            return a;
        }
        if (a > b) {
            return b;
        }
        return a;
    }
}
