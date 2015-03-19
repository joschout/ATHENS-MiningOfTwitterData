package tweets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Tweet {

	public final List<HashTag> hashtags;
	
	public Tweet(List<HashTag> hashtags){
		Set<HashTag> sortedHashtags = new TreeSet<HashTag>(hashtags);
		this.hashtags = new ArrayList<HashTag>(sortedHashtags);
	}
	
	@Override
	public String toString(){
		String result = "Tweet:";
		for(HashTag hashtag : this.hashtags){
			result += " " + hashtag;
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
