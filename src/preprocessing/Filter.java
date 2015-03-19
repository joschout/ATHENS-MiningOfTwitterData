package preprocessing;

import java.util.ArrayList;
import java.util.List;

import tweets.Tweet;

public class Filter {
	
	private final List<Tweet> tweets;

	public Filter(List<Tweet> tweets){
		this.tweets = new ArrayList<Tweet>(tweets);
	}
		
}
