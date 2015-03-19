package main;

import java.io.IOException;

import org.jgrapht.graph.SimpleWeightedGraph;

import preprocessing.Filter;
import preprocessing.JsonParser;
import tweets.Tweet;
import utils.Timer;

public class Main {
	
	public static void main(String[] args) throws IOException{

		JsonParser parser = new JsonParser("data");
		//Filter filter = new Filter(parser.parseFile("Paris-2015-2-27"));
		//SimpleWeightedGraph graph = filter.createWeightedGraph();
		Timer parseTimer = new Timer("parsing the files");
		parseTimer.start();
		Filter filter = new Filter(parser.parseAllFiles());
		parseTimer.stop();
		
		for(Tweet tweet : filter.tweets){
			//System.out.print(tweet + "\n-----------------------------\n");
		}
		
	}

}