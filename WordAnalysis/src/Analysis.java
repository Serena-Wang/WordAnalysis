/**
 * This class analyzes the novel: The Turn of the Screw
 * @author Serena Wang
 * 2018. July
 */
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;;

public class Analysis {
	private HashMap<String, Integer>  wordsMap = new HashMap<>();
	private String[] byChapter;
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
		
		List leastList = get20LeastFrequentWords();
		System.out.println(leastList);
		
		int[] wordCountByChapter = getFrequencyOfWord(novel,"see");
		System.out.println(Arrays.toString(wordCountByChapter));
		
		int num = getChapterQuoteAppears("We were alone with the quiet day, and his little heart, dispossessed, had stopped.");
		System.out.println(num);
		
		String sentence = generateSentence(novel);
		System.out.println(sentence);
		
		
		
	}
	// get the total number of words in the novel
	private int getTotalNumerOfWords(String novel) {
		return novel.split("\\s+").length;
	}
	
	// get the total number of unique words in the novel
	private int getTotalUniqueWords(String novel) {
		// read word and put it in the hashmap
		Scanner scan = new Scanner(novel);
		while (scan.hasNext()) {
			String word = scan.next();
			word = word.replaceAll("\\p{Punct}", "");
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
	
	//helper method for finding top 20 words
	//With specified option String, it will return a max/min Heap
	private PriorityQueue sortWords(String option) {
		PriorityQueue <String> sortedHeap = new PriorityQueue<>(wordsMap.size(), new Comparator<String>() {
			public int compare (String s1, String s2) {
				int countDiff=0;
				if (option.equals("min")) {
					countDiff = wordsMap.get(s1)-wordsMap.get(s2);
				} else if (option.equals("max")){
					countDiff = wordsMap.get(s2)-wordsMap.get(s1);
				}
				 
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
    
	// get top 20 most frequently used words
	private ArrayList<List> get20MostFrequentWords(){
		PriorityQueue <String> sortedHeap = sortWords("max");
		return get20Words(sortedHeap,new ArrayList<String>());
	}
	
	// helper method to return a list of top 20 items in the heap and their number of occurrence 
	// if there's no filter, filter can be an empty list
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
	
	
	// get 20 most interesting words after the filter
	// filter: top 100 most frequently used words
	private ArrayList<List> get20MostInterestingFrequentWords(String commonWords) {
		List<String> commonList = new ArrayList<String>(Arrays.asList(commonWords.split(" ")));
		PriorityQueue <String> sortedHeap = sortWords("max");
		return get20Words(sortedHeap,commonList);
	}
	
	// get 20 least frequently used words
	private ArrayList<List> get20LeastFrequentWords(){
		PriorityQueue <String> sortedHeap = sortWords("min");
		return get20Words(sortedHeap,new ArrayList<String>());
	}
	
	// get the frequency of a specific word in each chapter
	private int[] getFrequencyOfWord(String novel, String target) {
		byChapter = novel.split("Chapter[0-9]");
		int[] res = new int[byChapter.length];
		int currCount=0;
		for (int i=0; i<byChapter.length; i++) {
			currCount = 0;
			Scanner scan = new Scanner(byChapter[i]);
			while (scan.hasNext()) {
				String word = scan.next();
				word = word.replaceAll("\\p{Punct}", "");
				if(word.equals(target)) {
					currCount++;
				}
			}
			res[i]=currCount;
		}
		return res;	
	}
	
	// find the chapter that the quote is from
	// return -1 if cannot find the quote
	private int 	getChapterQuoteAppears(String quote) {
		for (int i=0; i<byChapter.length; i++) {
			if (byChapter[i].contains(quote)) {
				return i;
			}
		}
		
		return -1;
	}
	
	// generate a sentence with the words in the novel
	// current algorithm: randomly picks the word that came after a word
	// Code commented out uses the algorithm that picks the words with highest frequency in the novel. It does not perform very well in this novel due to a weird word loop.
	// The sentence that I got from this algorithm is "The little of the him the him the him the him the him the him the him the him the."
	private String generateSentence(String novel) {
		StringBuffer sentence = new StringBuffer();
		int i=0;
		String wordWithMaxFreq = "The";
		sentence.append(wordWithMaxFreq);
		while (i<19) {
			String pattern = wordWithMaxFreq+"\\W+(\\w+)";
			List<String> options = new ArrayList<String>();
			Pattern curr = Pattern.compile(pattern);
			Matcher matcher = curr.matcher(novel);
			int maxFreq =0;
			while(matcher.find()) {
				String word = matcher.group(1);
//				if (wordsMap.containsKey(word) && maxFreq< wordsMap.get(word)) {
//					maxFreq = wordsMap.get(word);
//					wordWithMaxFreq = word; 
//				}
				options.add(word);
			}
			i++;
			int random = (int) (Math.random()*options.size());
			//sentence.append(" "+wordWithMaxFreq);
			sentence.append(" "+options.get(random));
		}
		
		System.out.println(sentence.toString());
		return sentence.toString();
	}
}
