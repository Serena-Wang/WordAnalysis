import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;;

public class Analysis {
	private HashMap<String, Integer>  wordsMap = new HashMap<>();
	public static void main (String[] args) throws IOException{
		
	    FileReader common = new FileReader("res/1-1000.txt");
	    BufferedReader commonReader = new BufferedReader(common);
	    String commonWords="";
	    String commonLine = commonReader.readLine();
	    int i=0;
	    // top 100 commonly used words
		while(commonLine!=null && i<100) {
			commonWords+=" "+ commonLine;
			commonLine = commonReader.readLine();
			i++;
		}
	    
		
		FileReader file = new FileReader("res/209.txt");
		BufferedReader reader = new BufferedReader(file);
		String novel = "";
		String line = reader.readLine();
		// novel
		while(line!=null) {
			novel+=" "+ line;
			line = reader.readLine();
		}
		new Analysis(novel, commonWords);
		
		
	}
	
	public Analysis(String novel, String commonWords) {
		int wordCount = getTotalNumerOfWords(novel);
		System.out.println(wordCount);
		int uniqueCount = getTotalUniqueWords(novel);
		System.out.println(uniqueCount);
		
		List frequentList= get20MostFrequentWords();
		System.out.println(frequentList);
		
		List filteredtList= get20MostInterestingFrequentWords(commonWords);
		System.out.println(filteredtList);
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
	private PriorityQueue sortWords() {
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
		
		return sortedHeap;
	
	}
    
	private ArrayList<List> get20MostFrequentWords(){
		PriorityQueue <String> sortedHeap = sortWords();
		return get20Words(sortedHeap,new ArrayList<String>());
	}
	
	private ArrayList<List> get20Words(PriorityQueue <String> sortedHeap, List<String> filter){
		
		ArrayList<List> res = new ArrayList<List>();
		int i=0;
		while(i<20) {
			String word = sortedHeap.poll();
			if (!filter.contains(word)) {
				Integer val = wordsMap.get(word);
				List<String> pair = new ArrayList<>();
				pair.add(word);
				pair.add(val.toString());
				res.add(pair);
				i++;
			} else {
				word = sortedHeap.poll();
			}
		}
		return res;
	}
	
	
	
	private ArrayList<List> get20MostInterestingFrequentWords(String commonWords) {
		List<String> commonList = new ArrayList<String>(Arrays.asList(commonWords.split(" ")));
		PriorityQueue <String> sortedHeap = sortWords();
		return get20Words(sortedHeap,commonList);
	
	}
	
}
