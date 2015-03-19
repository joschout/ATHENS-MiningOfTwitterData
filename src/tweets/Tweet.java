package tweets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tweet {

	public final List<HashTag> hashtags;
	
	public Tweet(List<HashTag> hashtags){
		List<HashTag> sortedHashtags = new ArrayList<HashTag>(hashtags);
		Collections.sort(sortedHashtags);
		this.hashtags = sortedHashtags;
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
	
}
