package graphProcessing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import tweets.HashTag;

public class SubgraphManager {

	/**
	 * Constructor using as a default a CircularFifoQueue of size 10
	 */
	public SubgraphManager(){
		this.densestSubgraphQueue = new CircularFifoQueue<SimpleWeightedGraph<HashTag,DefaultWeightedEdge>>(10);
	}
	
	/**
	 * 
	 * @param numberOfDensestSubgraphsToBeSaved the number of densest subgraphs that are possible to be saved in a CircularFifoQueue
	 */
	public SubgraphManager(int numberOfDensestSubgraphsToBeSaved){
		this.densestSubgraphQueue = new CircularFifoQueue<SimpleWeightedGraph<HashTag,DefaultWeightedEdge>>(numberOfDensestSubgraphsToBeSaved);
	}

	/**
	 * Queue that can contain at max a specific number of elements. The queue is circular First-In, First-Out,
	 *  so when it is full and a new element is added, the oldest element in the queue will be removed.
	 */
	private CircularFifoQueue<SimpleWeightedGraph<HashTag, DefaultWeightedEdge>> densestSubgraphQueue;
	
	public CircularFifoQueue<SimpleWeightedGraph<HashTag, DefaultWeightedEdge>> getDensestSubgraphQueue() {
		return densestSubgraphQueue;
	}

	public SimpleWeightedGraph<HashTag, DefaultWeightedEdge> getDensestSubgraph(SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph,
			DensityManager<HashTag, DefaultWeightedEdge> densitymanager){
		
		
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempGraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
		
		//PREPROCESSING STEP
		Map<Double, Set<HashTag>> degreeVerticesMap = new HashMap<Double, Set<HashTag>>();	
		
		for (HashTag vertex : inputGraph.vertexSet()){
			System.out.println("//================//");
//			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
//			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			
//			System.out.println("vertex to be put in the map: "+ vertex .toString());
			double degreeOfVertex = densitymanager.getDegreeOfVertex(vertex, inputGraph);
//			System.out.println("degree of the vertex: " + degreeOfVertex);
			if(degreeVerticesMap.containsKey(degreeOfVertex)){
//				System.out.println("the map contains the key/ vertices of this degree");	
//				System.out.println("a print of the current map: "+degreeVerticesMap.toString());
				
				degreeVerticesMap.get(degreeOfVertex).add(vertex);
			}else{
//				System.out.println("the map does not contain the key/ vertices of this degree");
				Set<HashTag> setForThisDegree = new HashSet<HashTag>();
				setForThisDegree.add(vertex);
				degreeVerticesMap.put(degreeOfVertex, setForThisDegree);
			}
//			System.out.println("//================//");
		}
		
		/*
		 * STOPCONDITIE KAN FOUT ZIJN AAAAAAAAARRRGGGHHH
		 */
		
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
			Set<HashTag> verticesOfSmallestDegree = degreeVerticesMap.get(tempMin);
			System.out.println("de set van vertices met deze degree is: "+ verticesOfSmallestDegree.toString());
			HashTag vertex = (HashTag) verticesOfSmallestDegree.toArray()[0];
			System.out.println("de vertex met deze degree is: " + vertex.toString());
			verticesOfSmallestDegree.remove(vertex);
			System.out.println("this vertex is removed!");
			if(verticesOfSmallestDegree.isEmpty()){
				degreeVerticesMap.remove(tempMin);
				System.out.println("map for this degree removed!");
			}
			
			//remove v and all its edges
			tempGraph.removeVertex(vertex);
			
			/*
			 * if the density of tempGraph is larger than the density of tempDensestSubgraph
			 * then store tempGraph as tempDensestSubgraph
			 */
			double densityOfTempGraph = densitymanager.getDensity(tempGraph);
			double densityOfTempDensestSubgraph = densitymanager.getDensity(tempDensestSubgraph);
			if (densityOfTempGraph > densityOfTempDensestSubgraph) {
				tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) tempGraph.clone();
			}
			
			System.out.println("#END OF WHILE#");
			//check if the graph has at least 3  
		}
		
		System.out.println("the method returns");
		return tempDensestSubgraph;
	}
	
	
	
	
	public SimpleWeightedGraph<HashTag, DefaultWeightedEdge> getDensestSubgraph2(SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph,
			DensityManager<HashTag, DefaultWeightedEdge> densitymanager){
		
		
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempGraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
		
		//PREPROCESSING STEP
		TreeMap<Double, Set<HashTag>> degreeVerticesMap = new TreeMap<Double, Set<HashTag>>();	
		
		for (HashTag vertex : inputGraph.vertexSet()){
			System.out.println("//================//");
//			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
//			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			
//			System.out.println("vertex to be put in the map: "+ vertex .toString());
			double degreeOfVertex = densitymanager.getDegreeOfVertex(vertex, inputGraph);
//			System.out.println("degree of the vertex: " + degreeOfVertex);
			if(degreeVerticesMap.containsKey(degreeOfVertex)){
//				System.out.println("the map contains the key/ vertices of this degree");	
//				System.out.println("a print of the current map: "+degreeVerticesMap.toString());
				
				degreeVerticesMap.get(degreeOfVertex).add(vertex);
			}else{
//				System.out.println("the map does not contain the key/ vertices of this degree");
				Set<HashTag> setForThisDegree = new HashSet<HashTag>();
				setForThisDegree.add(vertex);
				degreeVerticesMap.put(degreeOfVertex, setForThisDegree);
			}
//			System.out.println("//================//");
		}
		
		/*
		 * STOPCONDITIE KAN FOUT ZIJN AAAAAAAAARRRGGGHHH
		 */
		
		while(!degreeVerticesMap.isEmpty()){
			System.out.println("#START OF WHILE#");
			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			
			//get the smallest degree
			double tempMin = degreeVerticesMap.firstKey();
			
			System.out.println("smallest degree is: " + tempMin);
			
			//get 1 node with the smallest degree
			Set<HashTag> verticesOfSmallestDegree = degreeVerticesMap.get(tempMin);
			System.out.println("de set van vertices met deze degree is: "+ verticesOfSmallestDegree.toString());
			HashTag vertex = (HashTag) verticesOfSmallestDegree.toArray()[0];
			System.out.println("de vertex met deze degree is: " + vertex.toString());
			verticesOfSmallestDegree.remove(vertex);
			System.out.println("this vertex is removed!");
			if(verticesOfSmallestDegree.isEmpty()){
				degreeVerticesMap.remove(tempMin);
				System.out.println("map for this degree removed!");
			}
			
			//remove v and all its edges
			tempGraph.removeVertex(vertex);
			
			/*
			 * if the density of tempGraph is larger than the density of tempDensestSubgraph
			 * then store tempGraph as tempDensestSubgraph
			 */
			double densityOfTempGraph = densitymanager.getDensity(tempGraph);
			double densityOfTempDensestSubgraph = densitymanager.getDensity(tempDensestSubgraph);
			if (densityOfTempGraph > densityOfTempDensestSubgraph) {
				tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) tempGraph.clone();
			}
			
			System.out.println("#END OF WHILE#");
			//check if the graph has at least 3  
		}
		
		System.out.println("the method returns");
		return tempDensestSubgraph;
	}
	
	public SimpleWeightedGraph<HashTag, DefaultWeightedEdge> getDensestSubgraph3(SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph,
			DensityManager<HashTag, DefaultWeightedEdge> densitymanager){
		
		
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempGraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
		
		//PREPROCESSING STEP
		TreeMap<Double, Set<HashTag>> degreeVerticesMap = new TreeMap<Double, Set<HashTag>>();	
		
		for (HashTag vertex : inputGraph.vertexSet()){
//			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
//			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			
//			System.out.println("vertex to be put in the map: "+ vertex .toString());
			double degreeOfVertex = densitymanager.getDegreeOfVertex(vertex, inputGraph);
//			System.out.println("degree of the vertex: " + degreeOfVertex);
			if(degreeVerticesMap.containsKey(degreeOfVertex)){
	//				System.out.println("the map contains the key/ vertices of this degree");	
	//				System.out.println("a print of the current map: "+degreeVerticesMap.toString());
					
					degreeVerticesMap.get(degreeOfVertex).add(vertex);
			}else{
	//				System.out.println("the map does not contain the key/ vertices of this degree");
					Set<HashTag> setForThisDegree = new HashSet<HashTag>();
					setForThisDegree.add(vertex);
					degreeVerticesMap.put(degreeOfVertex, setForThisDegree);
			}
			
//			System.out.println("//================//");
		}
		
		/*
		 * STOPCONDITIE KAN FOUT ZIJN AAAAAAAAARRRGGGHHH
		 */
		
		while(!degreeVerticesMap.isEmpty()){
			System.out.println("#START OF WHILE#");
			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			
			//get the smallest degree
			double tempMin = degreeVerticesMap.firstKey();
			
			System.out.println("smallest degree is: " + tempMin);
			
			//get 1 node with the smallest degree
			Set<HashTag> verticesOfSmallestDegree = degreeVerticesMap.get(tempMin);
			System.out.println("de set van vertices met deze degree is: "+ verticesOfSmallestDegree.toString());
			for(HashTag vertex: verticesOfSmallestDegree){
				System.out.println("de vertex met deze degree is: " + vertex.toString());
				tempGraph.removeVertex(vertex);
				System.out.println("this vertex is removed!");
			}
			
			degreeVerticesMap.remove(tempMin);
			System.out.println("map for this degree removed!");
			
			
			/*
			 * if the density of tempGraph is larger than the density of tempDensestSubgraph
			 * then store tempGraph as tempDensestSubgraph
			 */
			double densityOfTempGraph = densitymanager.getDensity(tempGraph);
			double densityOfTempDensestSubgraph = densitymanager.getDensity(tempDensestSubgraph);
			if (densityOfTempGraph > densityOfTempDensestSubgraph) {
				tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) tempGraph.clone();
			}
			
			System.out.println("#END OF WHILE#");
			//check if the graph has at least 3  
		}
		
		System.out.println("the method returns");
		return tempDensestSubgraph;
	}
	
	public CircularFifoQueue<SimpleWeightedGraph<HashTag, DefaultWeightedEdge>> getDensestSubgraph4(SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph,
			DensityManager<HashTag, DefaultWeightedEdge> densitymanager){
		
		
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempGraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
			
		//PREPROCESSING STEP
		TreeMap<Double, Set<HashTag>> degreeVerticesMap = new TreeMap<Double, Set<HashTag>>();	
		
		for (HashTag vertex : inputGraph.vertexSet()){
//			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
//			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			
//			System.out.println("vertex to be put in the map: "+ vertex .toString());
			double degreeOfVertex = densitymanager.getDegreeOfVertex(vertex, inputGraph);
//			System.out.println("degree of the vertex: " + degreeOfVertex);
			if(degreeVerticesMap.containsKey(degreeOfVertex)){
	//				System.out.println("the map contains the key/ vertices of this degree");	
	//				System.out.println("a print of the current map: "+degreeVerticesMap.toString());
					
					degreeVerticesMap.get(degreeOfVertex).add(vertex);
			}else{
	//				System.out.println("the map does not contain the key/ vertices of this degree");
					Set<HashTag> setForThisDegree = new HashSet<HashTag>();
					setForThisDegree.add(vertex);
					degreeVerticesMap.put(degreeOfVertex, setForThisDegree);
			}
			
//			System.out.println("//================//");
		}
		
		/*
		 * STOPCONDITIE KAN FOUT ZIJN AAAAAAAAARRRGGGHHH
		 */
		
		while(!degreeVerticesMap.isEmpty()){
			System.out.println("#START OF WHILE#");
			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			
			//get the smallest degree
			double tempMin = degreeVerticesMap.firstKey();
			
			System.out.println("smallest degree is: " + tempMin);
			
			//get 1 node with the smallest degree
			Set<HashTag> verticesOfSmallestDegree = degreeVerticesMap.get(tempMin);
			System.out.println("de set van vertices met deze degree is: "+ verticesOfSmallestDegree.toString());
			for(HashTag vertex: verticesOfSmallestDegree){
				System.out.println("de vertex met deze degree is: " + vertex.toString());
				tempGraph.removeVertex(vertex);
				System.out.println("this vertex is removed!");
			}
			
			degreeVerticesMap.remove(tempMin);
			System.out.println("map for this degree removed!");
			
			
			/*
			 * if the density of tempGraph is larger than the density of tempDensestSubgraph
			 * then store tempGraph as tempDensestSubgraph
			 */
			double densityOfTempGraph = densitymanager.getDensity(tempGraph);
			double densityOfTempDensestSubgraph = densitymanager.getDensity(tempDensestSubgraph);
			if (densityOfTempGraph > densityOfTempDensestSubgraph) {
				tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) tempGraph.clone();
				getDensestSubgraphQueue().add(tempDensestSubgraph);
			}
			
			System.out.println("#END OF WHILE#");
			//check if the graph has at least 3  
		}
		
		System.out.println("the method returns");
		return getDensestSubgraphQueue();
	}
}
