package main;

import java.io.IOException;

import org.jgrapht.graph.SimpleWeightedGraph;

import preprocessing.Filter;
import preprocessing.JsonParser;
import tweets.Tweet;

public class Main {
	
	public static void main(String[] args) throws IOException{

		JsonParser parser = new JsonParser("data");
		Filter filter = new Filter(parser.parseFile("NewYork-2015-2-23"));
		SimpleWeightedGraph graph = filter.createWeightedGraph();
		
		for(Tweet tweet : filter.tweets){
			//System.out.print(tweet + "\n-----------------------------\n");
		}
		
	}

}