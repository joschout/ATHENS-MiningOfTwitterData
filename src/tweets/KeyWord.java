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
		return (this.text.equals(((KeyWord)other).text));
	}
	
	
	public int compareTo(KeyWord other){
		return this.text.compareTo(other.text);
		
	}
	
	@Override
	public int hashCode(){
		return this.text.hashCode();
	}
	
}
