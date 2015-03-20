package preprocessing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import tweets.HashTag;
import tweets.Tweet;

public class Filter {
	
	public List<Tweet> tweets;

	public Filter(List<Tweet> tweets){
		this.tweets = new ArrayList<Tweet>(tweets);
	}
	
	private void removeDuplicates(){
		Set<Tweet> distinctTweets = new HashSet<Tweet>(this.tweets);
		this.tweets = new ArrayList<Tweet>(distinctTweets);
	}
	
	public SimpleWeightedGraph<HashTag,DefaultWeightedEdge> createWeightedGraph(){
		removeDuplicates();
		SimpleWeightedGraph<HashTag,DefaultWeightedEdge> graph = new SimpleWeightedGraph<HashTag,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		for(Tweet tweet : this.tweets){
			for(HashTag hashtag : tweet.hashtags){
				graph.addVertex(hashtag);
			}
		}
		for(Tweet tweet : this.tweets){
			for(HashTag hashtag : tweet.hashtags){
				for(int i = (tweet.hashtags.indexOf(hashtag) + 1); i < tweet.hashtags.size(); ++i){
					if(!graph.containsEdge(hashtag, tweet.hashtags.get(i))){
						//System.out.println(hashtag + " <-----> " + tweet.hashtags.get(i));
						graph.addEdge(hashtag, tweet.hashtags.get(i));
						DefaultWeightedEdge edge = graph.getEdge(hashtag, tweet.hashtags.get(i));
						graph.setEdgeWeight(edge, 1);
					}
					else{
						DefaultWeightedEdge edge = graph.getEdge(hashtag, tweet.hashtags.get(i));
						graph.setEdgeWeight(edge, (graph.getEdgeWeight(edge) + 1));
						//System.out.println(hashtag + " <-----> " + tweet.hashtags.get(i) + "                   X " + (graph.getEdgeWeight(edge) + 1));
					}
				}
			}
		}
		return graph;
	}
	
}
