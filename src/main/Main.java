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

public class Main {
	
	public static void main(String[] args) throws IOException{

		JsonParser parser = new JsonParser("data");
		Filter filter = new Filter(parser.parseFile("test"));
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> graph = filter.createWeightedGraph();
		
//		for(Tweet tweet : filter.tweets){
//			//System.out.print(tweet + "\n-----------------------------\n");
//		}
		
		WeightedDensityManager<HashTag, DefaultWeightedEdge> densityManager = new WeightedDensityManager<HashTag, DefaultWeightedEdge>();
		SubgraphManager subgraphManager = new SubgraphManager();
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> densestSubgraph = subgraphManager.getDensestSubgraph(graph, densityManager);
		
		System.out.println("vertexset: "+densestSubgraph.vertexSet().toString());
		
		for(HashTag vertex: densestSubgraph.vertexSet()){
			System.out.println(vertex.toString() +": " + densityManager.getDegreeOfVertex(vertex, densestSubgraph));
		}
		for(DefaultWeightedEdge edge: densestSubgraph.edgeSet()){
			System.out.println("edges: " +edge.toString());
		}
	}

}