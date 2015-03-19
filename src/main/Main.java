package main;

import java.io.IOException;
import java.util.List;

import preprocessing.Filter;
import preprocessing.JsonParser;
import tweets.Tweet;

public class Main {
	
	public static void main(String[] args) throws IOException{

		JsonParser parser = new JsonParser("data");
		List<Tweet> ny = (new Filter(parser.parseFile("NewYork-2015-2-23"))).removeDuplicates();
		
		for(Tweet tweet : ny){
			System.out.print(tweet + "\n-----------------------------\n");
		}
		
	}

}