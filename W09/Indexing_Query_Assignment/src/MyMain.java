import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;




import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
public class MyMain {

	public static void main(String[] args) throws InvalidFormatException, IOException {
		String docs[] = {"Web mining is useful","Usage mining applications","Web structure mining studies the Web hyperlink structure"};
		
		String [] NguVung = getUniqueTokens(docs);
		
		ArrayList<Map<String, Integer >> frequent = getFrequent(docs, NguVung);
		
		ArrayList< ArrayList<Integer> > reverseIndex  = getReverseIndex(docs, NguVung);
		//In ket qua danh sach chi muc dao ra file
		//YOUR CODE HERE
		
		String query_string = "Web";
		printSearch(query_string,NguVung,frequent,reverseIndex);
		System.out.println("================");
		query_string = "Web Mining";
		printSearch(query_string,NguVung,frequent,reverseIndex);
		

	}

	public static void printSearch
	(String query_string,String[] NguVung,ArrayList<Map<String, Integer >> frequent,ArrayList< ArrayList<Integer> > reverseIndex) 
			throws InvalidFormatException, IOException{
		
		String[] query = tokenizeAndStemmingAndRemoveDefaultStopWords(query_string);
		ArrayList<SearchItem> searchResult = search(query,NguVung,frequent,reverseIndex);
		for(int i = 0; i < searchResult.size(); i++){
			SearchItem item = searchResult.get(i);
			System.out.println(Integer.toString(item.docIdx)+ ": " + Integer.toString(item.count));
		}
	}
	public static ArrayList<SearchItem> search1word(String query, String [] NguVung, ArrayList<Map<String, Integer >> frequent, ArrayList< ArrayList<Integer> > reverseIndex){
		int idx = getIndexNguVung(query,NguVung);
		ArrayList<SearchItem> result = new ArrayList<SearchItem>();
		ArrayList<Integer> reverseIndexItem = reverseIndex.get(idx);
		for(int i = 0; i < reverseIndexItem.size(); i++){
			int docIdx = reverseIndexItem.get(i);
			int wordFrequent = frequent.get(docIdx).get(NguVung[idx]);
			SearchItem searchitem = new SearchItem();
			searchitem.docIdx = docIdx;
			//YOUR CODE HERE
			searchitem.count = 0;
			result.add(searchitem);
		}
		return result;
	}
	public static ArrayList<SearchItem> search(String query[], String [] NguVung, ArrayList<Map<String, Integer >> frequent, ArrayList< ArrayList<Integer> > reverseIndex){
		ArrayList<SearchItem> result = search1word(query[0],NguVung,frequent, reverseIndex);
		for(int i = 1; i < query.length; i++){
			ArrayList<SearchItem> temp = search1word(query[i],NguVung,frequent, reverseIndex);
			result = combineResult(result,temp);
		}
		result.sort(new SearchItemComparator());
		return result;
	}
	private static ArrayList<SearchItem> combineResult(
			ArrayList<SearchItem> A, ArrayList<SearchItem> B) {
		ArrayList<SearchItem> result = new ArrayList<SearchItem>();
		for(int i = 0; i < A.size(); i++){
			SearchItem a = A.get(i);
			for(int j = 0; j < B.size();j++){
				SearchItem b = B.get(j);
				if(a.docIdx == b.docIdx){
					SearchItem newItem = new SearchItem();
					newItem.docIdx = a.docIdx;
					//YOUR CODE HERE
					newItem.count = 0;
					result.add(newItem);
				}
			}
			
		}
		return result;
	}
	
	private static int min(int a, int b) {
		//YOUR CODE HERE
		return 0;
	}

	public static ArrayList<Map<String, Integer >> getFrequent(String[] docs, String [] NguVung) throws InvalidFormatException, IOException{
		ArrayList<Map<String, Integer >> frequent = new ArrayList<Map<String,Integer>>();
		for (int i = 0; i < docs.length; i++){
			frequent.add(getFrequentVector(NguVung,docs[i]));
		}
		return frequent;
	}
	public static ArrayList< ArrayList<Integer> > getReverseIndex(String docs[], String NguVung[]) throws InvalidFormatException, IOException{
		ArrayList< ArrayList<Integer> > reverseIndex  = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < NguVung.length;i++)
			reverseIndex.add( new ArrayList<Integer>());
		
		for(int i = 0; i < docs.length;i++){
			Map<String, Integer> frequentVector = getFrequentVector(NguVung, docs[i]);
			for(int j = 0; j < NguVung.length; j++){
				if(frequentVector.get(NguVung[j]) > 0){
					ArrayList <Integer> reverseIndexItem = reverseIndex.get(j);
					reverseIndexItem.add(i);
					reverseIndex.set(j, reverseIndexItem);
				}
			}
		}
		return reverseIndex;
	}
	public static int getIndexNguVung(String word, String [] NguVung){
		int res = -1;
		for(int i = 0;i < NguVung.length;i++)
			if (word.equalsIgnoreCase(NguVung[i]))
				return i;
		return res;
	}
	public static Map<String, Integer> getFrequentVector(String[] NguVung, String doc) throws InvalidFormatException, IOException{
		Map<String, Integer> frequentVector = new HashMap<String, Integer>();
		for(int i = 0; i < NguVung.length;i++)
			frequentVector.put(NguVung[i], 0);
		String doc_token[] = tokenizeAndStemmingAndRemoveDefaultStopWords(doc);
		for(int i = 0; i < doc_token.length; i++){
			int currentCount = frequentVector.get(doc_token[i]);
			frequentVector.put(doc_token[i], currentCount + 1);
		}
		
		return frequentVector;
	}
	public static String[] getUniqueTokens(String[] docs) throws InvalidFormatException, IOException{
		PorterStemmer porterstemmer = new PorterStemmer();
		Set<String> uniquetokens = new HashSet<String>();
		for (int k = 0; k < docs.length; k++){
			String [] tokens =  tokenizeAndRemoveDefaultStopWords(docs[k].toLowerCase()) ;
			for(int i = 0; i < tokens.length; i++)
				uniquetokens.add(porterstemmer.stem(tokens[i]));
		}
		return uniquetokens.toArray(new String [uniquetokens.size()]);
	}
	public static String[] tokenizeAndStemmingAndRemoveDefaultStopWords(String input) throws InvalidFormatException, IOException{
		String tokens [] = tokenizeAndRemoveDefaultStopWords(input.toLowerCase()) ;
		String stemmed_tokens [] = new String [tokens.length];
		PorterStemmer porterstemmer = new PorterStemmer();
		for(int i =0; i < tokens.length; i++)
			stemmed_tokens[i] = porterstemmer.stem(tokens[i]);
		return stemmed_tokens;
	}
	public static String[] tokenizeAndRemoveDefaultStopWords(String input) throws InvalidFormatException, IOException{
		//YOUR CODE HERE
		String[] stopWords = getStopWord("E:\\F\\Teaching\\KhaiThacDuLieuWeb_CD_2017\\indexing\\stopWord.txt");
		
		String [] tokens =  tokenize(input) ;
		tokens = removeStopWords(tokens, stopWords);
		return tokens;
	}
	private static String[] tokenize(String input) throws InvalidFormatException, IOException {
		//YOUR CODE HERE
		InputStream is = new FileInputStream("E:/F/Teaching/KhaiThacDuLieuWeb_CD_2017/preprocessing/model/en-token.bin");
		TokenizerModel model = new TokenizerModel(is);
		Tokenizer tokenizer = new TokenizerME(model);
		String tokens[] = tokenizer.tokenize(input);
		
		is.close();
		
		return tokens;
	}
	public static String[] getStopWord(String stopWordFilePath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(stopWordFilePath));
		String line;
		ArrayList<String> array_stopWord = new ArrayList<String>();
		while((line = br.readLine()) != null){
			array_stopWord.add(line);
		}
		return array_stopWord.toArray(new String [array_stopWord.size()]);
	}
	public static String[] removeStopWords(String [] tokens, String [] stopWords){
		ArrayList<String> tokensList = new ArrayList<String>();
		for(int i = 0; i < tokens.length; i++){
			tokensList.add(tokens[i]);
		}
		for(int i = tokensList.size()-1; i > 0; i--){
			if(isContains(stopWords,tokens[i]))
				tokensList.remove(i);
		}
		return tokensList.toArray(new String [tokensList.size()]);
	}
	private static boolean isContains(String [] set, String item){
		boolean res = false;
		for(int i = 0; i < set.length; i++)
			if (item.equalsIgnoreCase(set[i]))
				res = true;
		return res;
	}
}