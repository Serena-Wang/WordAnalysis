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
		
		
	}
	
	private int getTotalNumerOfWords(String novel) {
		return novel.split("\\s+").length;
	}
	
	private int getTotalUniqueWords(String novel) {
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
	private PriorityQueue sortWords(String option) {
		PriorityQueue <String> sortedHeap = new PriorityQueue<>(wordsMap.size(), new Comparator<String>() {
			public int compare (String s1, String s2) {
				int countDiff=0;
				if (option.equals("min")) {
					countDiff = wordsMap.get(s1)-wordsMap.get(s2);
				} else {
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
    
	private ArrayList<List> get20MostFrequentWords(){
		PriorityQueue <String> sortedHeap = sortWords("max");
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
		PriorityQueue <String> sortedHeap = sortWords("max");
		return get20Words(sortedHeap,commonList);
	}
	
	private ArrayList<List> get20LeastFrequentWords(){
		PriorityQueue <String> sortedHeap = sortWords("min");
		return get20Words(sortedHeap,new ArrayList<String>());
	}
	
	private int[] getFrequencyOfWord(String novel, String target) {
		byChapter = novel.split("Chapter[0-9]");
		int[] res = new int[byChapter.length];
		//chapter num, count
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
	private int 	getChapterQuoteAppears(String quote) {
		for (int i=0; i<byChapter.length; i++) {
			if (byChapter[i].contains(quote)) {
				return i;
			}
		}
		
		return -1;
	}
	
	private String generateSentence(String novel) {
		StringBuffer sentence = new StringBuffer();
		int i=0;
		String wordWithMaxFreq = "The";
		sentence.append(wordWithMaxFreq);
		while (i<20) {
			String pattern = wordWithMaxFreq+"\\W+(\\w+)";
			List<String> options = new ArrayList<String>();
			Pattern curr = Pattern.compile(pattern);
			Matcher matcher = curr.matcher(novel);
			int maxFreq =0;
			while(matcher.find()) {
				String word = matcher.group(1);
				if (wordsMap.containsKey(word) && maxFreq< wordsMap.get(word)) {
					maxFreq = wordsMap.get(word);
					wordWithMaxFreq = word; 
				}
			}
			i++;
			sentence.append(" "+wordWithMaxFreq);
			System.out.println("maxFreq"+ wordWithMaxFreq +" "+maxFreq);
		}
		
		System.out.println(sentence.toString());
		return sentence.toString();
		
		
		
//		
//		int index = novel.indexOf("The");
//		while (index!=-1) {
//			int end = novel.indexOf(" ", index+"The".length()+1);
//			String word = novel.substring(index+4, end);
//			options.add(word);
//			index = novel.indexOf("The",index+1);
//		}
		
//		System.out.println("The"+ options);
//		System.out.println("maxFreq"+ wordWithMaxFreq +" "+maxFreq);
//		
//		
//		return null;
		
	}
}
