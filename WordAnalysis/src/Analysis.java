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
		
		List frequentList= get20MostFrequentWords(novel);
		System.out.println(frequentList);
	}
	
	private int getTotalNumerOfWords(String novel) {
		return novel.split("\\s+").length;
	}
	
	private int getTotalUniqueWords(String novel) {
		Scanner scan = new Scanner(novel);
		while (scan.hasNext()) {
			String word = scan.next();
			word = word.toLowerCase();
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
		ArrayList<List> res = new ArrayList<List>();
		PriorityQueue <String> sortedHeap = new PriorityQueue<>(wordsMap.size(), new Comparator<String>() {
				public int compare (String s1, String s2) {
					int countDiff = wordsMap.get(s2)-wordsMap.get(s1);
					if (countDiff ==0) {
						return s1.compareTo(s2);
					}
					return countDiff;	
					}
		});
		
		for (String key: wordsMap.keySet()) {
			sortedHeap.offer(key);
		}
		
		for (int i=0; i<20; i++) {
			String word = sortedHeap.poll();
			Integer val = wordsMap.get(word);
			List<String> pair = new ArrayList<>();
			pair.add(word);
			pair.add(val.toString());
			res.add(pair);
		}
		
		return res;
	}
	
}
