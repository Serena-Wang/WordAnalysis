import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;;

public class Analysis {
	private HashMap<String, Integer>  wordsMap = new HashMap<>();
	
	public static void main (String[] args) throws IOException{
		String fileName = "209.txt";
		FileReader file = new FileReader("res/209.txt");
		BufferedReader reader = new BufferedReader(file);
		String novel = "";
		String line = reader.readLine();
		while(line!=null) {
			novel+=" "+ line;
			line = reader.readLine();
		}
		new Analysis(novel);
	}
	
	public Analysis(String novel) {
		int wordCount = getTotalNumerOfWords(novel);
		System.out.println(wordCount);
		int uniqueCount = getTotalUniqueWords(novel);
		System.out.println(uniqueCount);
		
	}
	
	private int getTotalNumerOfWords(String novel) {
		return novel.split("\\s+").length;
	}
	
	private int getTotalUniqueWords(String novel) {
		Scanner scan = new Scanner(novel);
		while (scan.hasNext()) {
			String word = scan.next();
			if (wordsMap.containsKey(word)) {
				int val = wordsMap.get(word);
				val+=1;
				wordsMap.put(word, val);
			} else {
				wordsMap.put(word, 1);
			}
		}
		
		return wordsMap.size();
	}
    
	private ArrayList<List> get20MostFrequentWords(String novel){
		return null;
	}
	
}
