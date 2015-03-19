package preprocessing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tweets.Tweet;

public class Filter {
	
	private final List<Tweet> tweets;

	public Filter(List<Tweet> tweets){
		this.tweets = new ArrayList<Tweet>(tweets);
	}
	
	public List<Tweet> removeDuplicates(){
		Set<Tweet> distinctTweets = new HashSet<Tweet>(this.tweets);
		return new ArrayList<Tweet>(distinctTweets);
	}
	
}
