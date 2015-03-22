package graphProcessing;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import tweets.KeyWord;
import tweets.Tweet;

public class SubgraphManager {

	/**
	 * Queue that can contain at max a specific number of elements. The queue is circular First-In, First-Out,
	 *  so when it is full and a new element is added, the oldest element in the queue will be removed.
	 */
	private CircularFifoQueue<SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>> densestSubgraphQueue;
	private SimpleWeightedGraph<KeyWord, DefaultWeightedEdge> tempGraph;
	private SimpleWeightedGraph<KeyWord, DefaultWeightedEdge> tempDensestSubgraph;
	private Map<Double, Set<KeyWord>> degreeVerticesMap;
	

	/**
	 * Constructor using as a default a CircularFifoQueue of size 10
	 */
	public SubgraphManager(){
		this.densestSubgraphQueue = new CircularFifoQueue<SimpleWeightedGraph<KeyWord,DefaultWeightedEdge>>(10);
	}
	
	/**
	 * 
	 * @param numberOfDensestSubgraphsToBeSaved the number of densest subgraphs that are possible to be saved in a CircularFifoQueue
	 */
	public SubgraphManager(int numberOfDensestSubgraphsToBeSaved){
		this.densestSubgraphQueue = new CircularFifoQueue<SimpleWeightedGraph<KeyWord,DefaultWeightedEdge>>(numberOfDensestSubgraphsToBeSaved);
	}

	public CircularFifoQueue<SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>> getDensestSubgraphQueue() {
		return densestSubgraphQueue;
	}

	
	public SimpleWeightedGraph<KeyWord, DefaultWeightedEdge> getDensestSubgraph(SimpleWeightedGraph<KeyWord, DefaultWeightedEdge> inputGraph,
			DensityManager<KeyWord, DefaultWeightedEdge> densitymanager){
		
		 initializeTempGraphs(inputGraph);
		
		 this.degreeVerticesMap = new HashMap<Double, Set<KeyWord>>();
		 
		initializeDegreeVerticesMapWithVertices(inputGraph, densitymanager);
		
		while(!degreeVerticesMap.isEmpty()){
			System.out.println("#START OF WHILE#");
			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			
			//get the smallest degree
			double tempMin = Double.MAX_VALUE;
			for(double degree: degreeVerticesMap.keySet()){
				if (degree < tempMin) {
					tempMin = degree;
				}
			}
			
			System.out.println("smallest degree is: " + tempMin);
			
			//get 1 node with the smallest degree
			Set<KeyWord> verticesOfSmallestDegree = degreeVerticesMap.get(tempMin);
			System.out.println("the set of vertices with this degree is: "+ verticesOfSmallestDegree.toString());
			KeyWord vertex = (KeyWord) verticesOfSmallestDegree.toArray()[0];
			System.out.println("the vertex with this degree is: " + vertex.toString());
			
			//get the neighbors of the node with the smallest degree 
			NeighborIndex<KeyWord, DefaultWeightedEdge> index = new NeighborIndex<KeyWord, DefaultWeightedEdge>(tempGraph);
			Set<KeyWord> neighborsOfVertex = index.neighborsOf(vertex);
			//remove the neighbors from the map
			for(KeyWord neighbor: neighborsOfVertex){
				double neighborDegree = densitymanager.getDegreeOfVertex(neighbor, tempGraph);
				degreeVerticesMap.get(neighborDegree).remove(neighbor);
			}	
			
			//remove the node with the smallest degree from the map
			verticesOfSmallestDegree.remove(vertex);
			System.out.println("this vertex is removed!");
			if(verticesOfSmallestDegree.isEmpty()){
				degreeVerticesMap.remove(tempMin);
				System.out.println("map for this degree removed!");
			}
			//remove the node with the smallest degree from the graph
			tempGraph.removeVertex(vertex);
			
			//put the neighbors back in the map
			for(KeyWord neighbor: neighborsOfVertex){
				double degreeOfNeighbor = densitymanager.getDegreeOfVertex(neighbor, tempGraph);
				if(degreeVerticesMap.containsKey(degreeOfNeighbor)){					
					degreeVerticesMap.get(degreeOfNeighbor).add(neighbor);
				}else{
					Set<KeyWord> setForThisDegree = new HashSet<KeyWord>();
					setForThisDegree.add(neighbor);
					degreeVerticesMap.put(degreeOfNeighbor, setForThisDegree);
				}
			}			
			
			/*
			 * if the density of tempGraph is larger than the density of tempDensestSubgraph
			 * then store tempGraph as tempDensestSubgraph
			 */
			updateTempDensestSubgraph(densitymanager);
			System.out.println("#END OF WHILE#"); 
		}
		
		System.out.println("the method returns");
		return tempDensestSubgraph;
	}


	public CircularFifoQueue<SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>> getDensestSubgraphs(SimpleWeightedGraph<KeyWord, DefaultWeightedEdge> inputGraph,
			DensityManager<KeyWord, DefaultWeightedEdge> densitymanager){
			
		initializeTempGraphs(inputGraph);
			
		//PREPROCESSING STEP
		degreeVerticesMap = new TreeMap<Double, Set<KeyWord>>();	
		
		initializeDegreeVerticesMapWithVertices(inputGraph, densitymanager);
		
		while(!degreeVerticesMap.isEmpty()){
			System.out.println("#======== START OF WHILE ========#");
			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			
			//get the smallest degree
			double tempMin = ((TreeMap<Double, Set<KeyWord>>) degreeVerticesMap).firstKey();
			System.out.println("smallest degree is: " + tempMin);
			
			//get nodes with the smallest degree
			Set<KeyWord> verticesOfSmallestDegree = degreeVerticesMap.get(tempMin);
			System.out.println("the set of vertices with the smallest degree is: "+ verticesOfSmallestDegree.toString());
			
			//get neighbors of the nodes with the smallest degree
			NeighborIndex<KeyWord, DefaultWeightedEdge> index = new NeighborIndex<KeyWord, DefaultWeightedEdge>(tempGraph);
			Set<KeyWord> neighborsOfVerticesWithSmallestDegree = new HashSet<KeyWord>();
			for(KeyWord vertex: verticesOfSmallestDegree){
//				System.out.println("looking for neighbors of: " + vertex.toString());
				neighborsOfVerticesWithSmallestDegree.addAll(index.neighborsOf(vertex));
			}
			System.out.println("the set of neighboring vertices  is:" + "\n    " + neighborsOfVerticesWithSmallestDegree.toString());
			
			//remove the vertices with the smallest degree from the map
			degreeVerticesMap.remove(tempMin);
			

			//remove the neighbors from the map
			for(KeyWord neighbor: neighborsOfVerticesWithSmallestDegree){
				double neighborDegree = densitymanager.getDegreeOfVertex(neighbor, tempGraph);
				if(neighborDegree != tempMin){
					degreeVerticesMap.get(neighborDegree).remove(neighbor);
					if(degreeVerticesMap.get(neighborDegree).isEmpty()){
						degreeVerticesMap.remove(neighborDegree);
					}
//					System.out.println("removed neighbor from map: " +  neighbor.toString()+ " with degree: "+ neighborDegree);
				}
			}
					
			//remove the vertices with the smallest degree from the tempGraph
			tempGraph.removeAllVertices(verticesOfSmallestDegree);
			
			//put the neighbors back in the map (if they aren't part of the removed vertices)
			for(KeyWord neighbor: neighborsOfVerticesWithSmallestDegree){
				if(tempGraph.containsVertex(neighbor)){
					double degreeOfNeighbor = densitymanager.getDegreeOfVertex(neighbor, tempGraph);
					if(degreeVerticesMap.containsKey(degreeOfNeighbor)){
						degreeVerticesMap.get(degreeOfNeighbor).add(neighbor);
					}else{
						Set<KeyWord> setForThisDegree = new HashSet<KeyWord>();
						setForThisDegree.add(neighbor);
						degreeVerticesMap.put(degreeOfNeighbor, setForThisDegree);
					}
//					System.out.println("added neighbor to map: " +  neighbor.toString()+ " with degree: "+ degreeOfNeighbor);	
				}
			}	
			
			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			/*
			 * if the density of tempGraph is larger than the density of tempDensestSubgraph
			 * then store tempGraph as tempDensestSubgraph
			 */
			updateDensestSubgraphQueue(densitymanager);
			System.out.println("#======== END OF WHILE ========#");
		}
		
		System.out.println("the method returns");
		return getDensestSubgraphQueue();
	}

	private void initializeTempGraphs(SimpleWeightedGraph<KeyWord, DefaultWeightedEdge> inputGraph) {
		tempGraph = (SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>) inputGraph.clone();
		tempDensestSubgraph = (SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>) inputGraph.clone();
	}
	

	private void initializeDegreeVerticesMapWithVertices(
			SimpleWeightedGraph<KeyWord, DefaultWeightedEdge> inputGraph,
			DensityManager<KeyWord, DefaultWeightedEdge> densitymanager) {
		for (KeyWord vertex : inputGraph.vertexSet()){
			double degreeOfVertex = densitymanager.getDegreeOfVertex(vertex, inputGraph);
			if(degreeVerticesMap.containsKey(degreeOfVertex)){
				degreeVerticesMap.get(degreeOfVertex).add(vertex);
			}else{
				Set<KeyWord> setForThisDegree = new HashSet<KeyWord>();
				setForThisDegree.add(vertex);
				degreeVerticesMap.put(degreeOfVertex, setForThisDegree);
			}
		}
	}

	private void updateTempDensestSubgraph(
			DensityManager<KeyWord, DefaultWeightedEdge> densitymanager) {
		double densityOfTempGraph = densitymanager.getDensity(this.tempGraph);
		double densityOfTempDensestSubgraph = densitymanager.getDensity(this.tempDensestSubgraph);
		if (densityOfTempGraph > densityOfTempDensestSubgraph) {
			tempDensestSubgraph = (SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>) tempGraph.clone();
		}
	}
	
	private void updateDensestSubgraphQueue(
			DensityManager<KeyWord, DefaultWeightedEdge> densitymanager) {
		double densityOfTempGraph = densitymanager.getDensity(this.tempGraph);
		System.out.println("density of the current temp graph: " + densityOfTempGraph);
		double densityOfTempDensestSubgraph = densitymanager.getDensity(this.tempDensestSubgraph);
		System.out.println("density of the densest subgraph up until now:" + densityOfTempDensestSubgraph);
		if (densityOfTempGraph > densityOfTempDensestSubgraph) {
			System.out.println("new densest subgraph added");
			tempDensestSubgraph = (SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>) tempGraph.clone();
			this.getDensestSubgraphQueue().add(tempDensestSubgraph);
		}
	}
	
	public void printSubgraphsToFile(DensityManager densityManager, String fileName) throws IOException{
		File file = new File(fileName);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		String text = "This file contains the results from a run of the Densest Subgraph algoritm. \n"
				+ "";
		int denseSubgraphNb = densestSubgraphQueue.size();
		
		for (Iterator<SimpleWeightedGraph<KeyWord, DefaultWeightedEdge>> iter = densestSubgraphQueue.iterator(); iter.hasNext(); ) {
			SimpleWeightedGraph<KeyWord, DefaultWeightedEdge> sub = iter.next();
			text = text + "#================ Dense subgraph number: "+ denseSubgraphNb + " ================# \n";
			text = text + "Density of the graph: " + densityManager.getDensity(sub) + " \n";
			
			
			int nrOfusers = 0;
			int nrOfTweets =0;
			Set<Long> userIds = new HashSet<Long>();
			Set<Long> tweetIds = new HashSet<Long>();
			
			for(KeyWord word: sub.vertexSet()){
				for(Tweet tweet: word.tweets){
					long uID = tweet.userId;
					if (!userIds.contains(uID)) {
						nrOfusers ++;
						userIds.add(uID);
					}
					long twID = tweet.tweetId;
					if (!tweetIds.contains(twID)) {
						nrOfTweets ++;
						tweetIds.add(twID);
					}					
				}
			}
			
			text = text + "Number of distinct users who contributed to the creation of the subgraph: " + nrOfusers+ " \n";
			text = text + "Number of distinct tweets from which the subgraph emerged: "+ nrOfTweets+ " \n";
			text = text + "The vertices in the subgraph with their degree: \n";
			for(KeyWord vertex: sub.vertexSet()){
				text = text + "    (" + vertex.toString() +", " + densityManager.getDegreeOfVertex(vertex, sub) + ") \n";
			}
			text = text + "The edges in the subgraph: \n";
			for(DefaultWeightedEdge edge: sub.edgeSet()){
				text = text + "     " +edge.toString()+ " \n";
			}
			text = text + "#=========================================================#" + " \n";
			denseSubgraphNb --;
		}
		
		writer.write(text);
		writer.close();
		
		System.out.println("File written to: " + file.getAbsolutePath().toString());
	}
	
	
}
