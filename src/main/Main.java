package main;

import graphProcessing.CircularFifoQueue;
import graphProcessing.DensityManager;
import graphProcessing.SubgraphManager;
import graphProcessing.WeightedDensityManager;

import java.io.IOException;
import java.util.Iterator;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import preprocessing.Filter;
import preprocessing.JsonParser;
import tweets.KeyWord;
import tweets.Text;
import tweets.Tweet;
import utils.Timer;

public class Main {
	
	public static void main(String[] args) throws IOException{
		//testGraph();
		//testStanfordLib();
		testGraphMultiplePrints();
	}
	
	private static void testStanfordLib() throws IOException{
		JsonParser parser = new JsonParser("data");
		Timer parseTimer = new Timer("parsing the files");
		parseTimer.start();
		Filter filter = new Filter(parser.parseFile("NewYork-2015-2-23"));
		parseTimer.stop();
	}

//	private static void testGraph() throws IOException{
//		JsonParser parser = new JsonParser("data");
//		Timer parseTimer = new Timer("parsing the files");
//		parseTimer.start();
//		Filter filter = new Filter(parser.parseFile("NewYork-2015-2-23"));
//		parseTimer.stop();
//		SimpleWeightedGraph<HashTag, DefaultWeightedEdge>  graph = filter.createWeightedGraph();
////		
////		//Filter filter = new Filter(parser.parseAllFiles());
////		parseTimer.stop();
//
//		
////		for(Tweet tweet : filter.tweets){
////			//System.out.print(tweet + "\n-----------------------------\n");
////		}
//		
//		DensityManager<HashTag, DefaultWeightedEdge> densityManager = new WeightedDensityManager<HashTag, DefaultWeightedEdge>();
//		SubgraphManager subgraphManager = new SubgraphManager();
//		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> densestSubgraph = subgraphManager.getDensestSubgraph3(graph, densityManager);
//		
//		System.out.println("vertexset: "+densestSubgraph.vertexSet().toString());
//		
//		for(HashTag vertex: densestSubgraph.vertexSet()){
//			System.out.println(vertex.toString() +": " + densityManager.getDegreeOfVertex(vertex, densestSubgraph));
//		}
//		for(DefaultWeightedEdge edge: densestSubgraph.edgeSet()){
//			System.out.println("edges: " +edge.toString());
//		}	
//	}
	
	private static void testGraphMultiplePrints() throws IOException{
		JsonParser parser = new JsonParser("data");
		Timer parseTimer = new Timer("parsing the files");
		parseTimer.start();
		Filter filter = new Filter(parser.parseFile("NewYork-2015-2-23"));
		parseTimer.stop();
		SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>  graph = filter.createWeightedGraph();
		
		WeightedDensityManager<KeyWord, DefaultWeightedEdge> densityManager = new WeightedDensityManager<KeyWord, DefaultWeightedEdge>();
		SubgraphManager subgraphManager = new SubgraphManager();
		CircularFifoQueue<SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>> queue = subgraphManager.getDensestSubgraphs(graph, densityManager);
		

		for (Iterator<SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>> iter = queue.iterator(); iter.hasNext(); ) {
			SimpleWeightedGraph<KeyWord, DefaultWeightedEdge> tempDensestSubgraph = iter.next();
			System.out.println("\n" + "//=== Start of new temp densest subgraph ===//");
			System.out.println("vertexset: "+tempDensestSubgraph.vertexSet().toString());
			
			for(KeyWord vertex: tempDensestSubgraph.vertexSet()){
				System.out.println(vertex.toString() +": " + densityManager.getDegreeOfVertex(vertex, tempDensestSubgraph));
			}
			for(DefaultWeightedEdge edge: tempDensestSubgraph.edgeSet()){
				System.out.println("edges: " +edge.toString());
			}
			System.out.println("//=== End of new temp densest subgraph ===//" +"\n");
		}
		
		
		
	}
	
}