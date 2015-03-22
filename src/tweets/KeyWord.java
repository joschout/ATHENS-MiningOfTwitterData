package tweets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class KeyWord implements Comparable<KeyWord> {

	public final String text;
	public List<Tweet> tweets = new ArrayList<Tweet>();
	
	public KeyWord(String text){
		this.text = text.toLowerCase();
	}
	
	public void addTweet(Tweet tweet){
		this.tweets.add(tweet);
	}
	
	public Set<Tweet> getTweets(){
		return new HashSet<Tweet>(this.tweets);
	}
	
	@Override
	public abstract String toString();
	
	@Override
	public boolean equals(Object other){
		//return (this.text.equals(((KeyWord)other).text));
		return approximatelyEqual(this, (KeyWord)other);
	}
	
	
	public int compareTo(KeyWord other){
		return this.text.compareTo(other.text);
		
	}
	
	@Override
	public int hashCode(){
		return this.text.hashCode();
	}
	
	private boolean approximatelyEqual(KeyWord first, KeyWord second){
		//return (hammingDistance(first.text, second.text) <= 2);
		return (LevenshteinDistance(first.text, second.text) <= 2);
	}
	
	private int hammingDistance(String firstWord, String secondWord){
		int differenceInLength = firstWord.length() - secondWord.length();
		if(differenceInLength == 0){
			return hammingDistanceEqualLength(firstWord, secondWord);
		}
		else if(differenceInLength == -1){
			return Math.min(hammingDistanceEqualLength(" " + firstWord, secondWord), hammingDistanceEqualLength(firstWord + " ", secondWord));
		}
		else if(differenceInLength == 1){
			return Math.min(hammingDistanceEqualLength(firstWord, " " + secondWord), hammingDistanceEqualLength(firstWord, secondWord + " "));
		}
		else if(differenceInLength == -2){
			return Math.min(Math.min(hammingDistanceEqualLength("  " + firstWord, secondWord), hammingDistanceEqualLength(" " + firstWord + " ", secondWord)),hammingDistanceEqualLength(firstWord + "  ", secondWord));
		}
		else if(differenceInLength == 2){
			return Math.min(Math.min(hammingDistanceEqualLength(firstWord, "  " + secondWord), hammingDistanceEqualLength(firstWord, " " + secondWord + " ")),hammingDistanceEqualLength(firstWord, secondWord + "  "));
		}
		else return Integer.MAX_VALUE;
	}
	
	private int hammingDistanceEqualLength(String firstWord, String secondWord){
		if(firstWord.length() != secondWord.length())
			throw new UnsupportedOperationException("Words are not of equal length!");
		int counter = 0;
		for (int i = 0; i < firstWord.length(); i++) {
		    if (firstWord.charAt(i) != secondWord.charAt(i)) {
		        ++counter;
		    }
		}
		return counter;
	}
	
	//This method was copied from http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
	public int LevenshteinDistance (String s0, String s1) {                          
	    int len0 = s0.length() + 1;                                                     
	    int len1 = s1.length() + 1;                                                     
	 
	    // the array of distances                                                       
	    int[] cost = new int[len0];                                                     
	    int[] newcost = new int[len0];                                                  
	 
	    // initial cost of skipping prefix in String s0                                 
	    for (int i = 0; i < len0; i++) cost[i] = i;                                     
	 
	    // dynamically computing the array of distances                                  
	 
	    // transformation cost for each letter in s1                                    
	    for (int j = 1; j < len1; j++) {                                                
	        // initial cost of skipping prefix in String s1                             
	        newcost[0] = j;                                                             
	 
	        // transformation cost for each letter in s0                                
	        for(int i = 1; i < len0; i++) {                                             
	            // matching current letters in both strings                             
	            int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;             
	 
	            // computing cost for each transformation                               
	            int cost_replace = cost[i - 1] + match;                                 
	            int cost_insert  = cost[i] + 1;                                         
	            int cost_delete  = newcost[i - 1] + 1;                                  
	 
	            // keep minimum cost                                                    
	            newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
	        }                                                                           
	 
	        // swap cost/newcost arrays                                                 
	        int[] swap = cost; cost = newcost; newcost = swap;                          
	    }                                                                               
	 
	    // the distance is the cost for transforming all letters in both strings        
	    return cost[len0 - 1];                                                          
	}
	
}
