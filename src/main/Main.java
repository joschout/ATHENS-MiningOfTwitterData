package main;

import graphProcessing.DensityManager;
import graphProcessing.SubgraphManager;
import graphProcessing.WeightedDensityManager;
import gui.GUIApplet;

import java.io.IOException;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import preprocessing.Filter;
import preprocessing.JsonParser;
import tweets.HashTag;
import tweets.Tweet;
import utils.Timer;

public class Main {
	
	public static void main(String[] args) throws IOException{

		JsonParser parser = new JsonParser("data//oscars");
		Timer parseTimer = new Timer("parsing the files");
		parseTimer.start();
		Filter filter = new Filter(parser.parseFile("Oscars-2015-2-23"));
		parseTimer.stop();
		SimpleWeightedGraph graph = filter.createWeightedGraph();
//		
//		//Filter filter = new Filter(parser.parseAllFiles());
//		parseTimer.stop();

		
//		for(Tweet tweet : filter.tweets){
//			//System.out.print(tweet + "\n-----------------------------\n");
//		}
		
		WeightedDensityManager<HashTag, DefaultWeightedEdge> densityManager = new WeightedDensityManager<HashTag, DefaultWeightedEdge>();
		SubgraphManager subgraphManager = new SubgraphManager();
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> densestSubgraph = subgraphManager.getDensestSubgraph3(graph, densityManager);
		
		System.out.println("vertexset: "+densestSubgraph.vertexSet().toString());
		
		for(HashTag vertex: densestSubgraph.vertexSet()){
			System.out.println(vertex.toString() +": " + densityManager.getDegreeOfVertex(vertex, densestSubgraph));
		}
		for(DefaultWeightedEdge edge: densestSubgraph.edgeSet()){
			System.out.println("edges: " +edge.toString());
		}
	}

}