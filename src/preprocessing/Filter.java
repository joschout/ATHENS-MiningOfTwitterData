package preprocessing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import tweets.HashTag;
import tweets.KeyWord;
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
	
	public SimpleWeightedGraph<KeyWord,DefaultWeightedEdge> createWeightedGraph(){
		removeDuplicates();
		SimpleWeightedGraph<KeyWord,DefaultWeightedEdge> graph = new SimpleWeightedGraph<KeyWord,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		/*for(Tweet tweet : this.tweets){
			for(KeyWord keyword : tweet.keywords){
				if(graph.containsVertex(keyword)){
				}
				graph.addVertex(keyword);
			}
		}*/
		
		for(Tweet tweet : this.tweets){
			for(KeyWord keyword : tweet.keywords){
				for(int i = (tweet.keywords.indexOf(keyword) + 1); i < tweet.keywords.size(); ++i){
					if(!graph.containsEdge(keyword, tweet.keywords.get(i))){
						//System.out.println(hashtag + " <-----> " + tweet.hashtags.get(i));
						graph.addEdge(keyword, tweet.keywords.get(i));
						DefaultWeightedEdge edge = graph.getEdge(keyword, tweet.keywords.get(i));
						graph.setEdgeWeight(edge, 1);
					}
					else{
						DefaultWeightedEdge edge = graph.getEdge(keyword, tweet.keywords.get(i));
						graph.setEdgeWeight(edge, (graph.getEdgeWeight(edge) + 1));
						//System.out.println(hashtag + " <-----> " + tweet.hashtags.get(i) + "                   X " + (graph.getEdgeWeight(edge) + 1));
					}
				}
			}
		}
		return graph;
	}
	
}
