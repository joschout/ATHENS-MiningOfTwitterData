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
		return (hammingDistance(first.text, second.text) <= 2);
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
	
}
