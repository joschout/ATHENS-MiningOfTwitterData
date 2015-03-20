package tweets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Tweet {

	public final long tweetId;
	public final long userId;
	public final Text text;
	public final List<HashTag> hashtags;
	
	public Tweet(long tweetId, long userId, Text text, List<HashTag> hashtags){
		this.tweetId = tweetId;
		this.userId = userId;
		this.text = text;
		Set<HashTag> sortedHashtags = new TreeSet<HashTag>(hashtags);
		this.hashtags = new ArrayList<HashTag>(sortedHashtags);
	}
	
	@Override
	public String toString(){
		String result = "Tweet " + this.tweetId + " by user " + this.userId + ":\n";
		result += this.text + "\n";
		for(HashTag hashtag : this.hashtags){
			result += hashtag + " ";
		}
		return result;
	}
	
	@Override
	public boolean equals(Object other){
		if(this.getClass() != other.getClass()){
			throw new UnsupportedOperationException("Tweets cannot be compared to other objects through \"equals()\" method");
		}
		return (this.hashtags.equals(((Tweet)other).hashtags));
	}
	
	@Override
	public int hashCode(){
		return this.hashtags.hashCode();
	}
	
}
