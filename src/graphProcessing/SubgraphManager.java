package graphProcessing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import tweets.HashTag;

public class SubgraphManager {

	/**
	 * Queue that can contain at max a specific number of elements. The queue is circular First-In, First-Out,
	 *  so when it is full and a new element is added, the oldest element in the queue will be removed.
	 */
	private CircularFifoQueue<SimpleWeightedGraph<HashTag, DefaultWeightedEdge>> densestSubgraphQueue;
	private SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempGraph;
	private SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempDensestSubgraph;
	private Map<Double, Set<HashTag>> degreeVerticesMap;
	

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

	public CircularFifoQueue<SimpleWeightedGraph<HashTag, DefaultWeightedEdge>> getDensestSubgraphQueue() {
		return densestSubgraphQueue;
	}

	
	public SimpleWeightedGraph<HashTag, DefaultWeightedEdge> getDensestSubgraph(SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph,
			DensityManager<HashTag, DefaultWeightedEdge> densitymanager){
		
		 initializeTempGraphs(inputGraph);
		
		 this.degreeVerticesMap = new HashMap<Double, Set<HashTag>>();
		 
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
			Set<HashTag> verticesOfSmallestDegree = degreeVerticesMap.get(tempMin);
			System.out.println("the set of vertices with this degree is: "+ verticesOfSmallestDegree.toString());
			HashTag vertex = (HashTag) verticesOfSmallestDegree.toArray()[0];
			System.out.println("the vertex with this degree is: " + vertex.toString());
			
			//get the neighbors of the node with the smallest degree 
			NeighborIndex<HashTag, DefaultWeightedEdge> index = new NeighborIndex<HashTag, DefaultWeightedEdge>(tempGraph);
			Set<HashTag> neighborsOfVertex = index.neighborsOf(vertex);
			//remove the neighbors from the map
			for(HashTag neighbor: neighborsOfVertex){
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
			for(HashTag neighbor: neighborsOfVertex){
				double degreeOfNeighbor = densitymanager.getDegreeOfVertex(neighbor, tempGraph);
				if(degreeVerticesMap.containsKey(degreeOfNeighbor)){					
					degreeVerticesMap.get(degreeOfNeighbor).add(neighbor);
				}else{
					Set<HashTag> setForThisDegree = new HashSet<HashTag>();
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
//
//	@Deprecated
//	public SimpleWeightedGraph<HashTag, DefaultWeightedEdge> getDensestSubgraph2(SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph,
//			DensityManager<HashTag, DefaultWeightedEdge> densitymanager){
//		
//		
//		initializeTempGraphs(inputGraph);
//		
//		//PREPROCESSING STEP
//		this.degreeVerticesMap = new TreeMap<Double, Set<HashTag>>();	
//		
//		initializeDegreeVerticesMapWithVertices(inputGraph, densitymanager);
//		
//		while(!degreeVerticesMap.isEmpty()){
//			System.out.println("#START OF WHILE#");
//			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
//			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
//			
//			//get the smallest degree
//			double tempMin = ((TreeMap<Double, Set<HashTag>>) degreeVerticesMap).firstKey();
//			
//			System.out.println("smallest degree is: " + tempMin);
//			
//			//get 1 node with the smallest degree
//			Set<HashTag> verticesOfSmallestDegree = degreeVerticesMap.get(tempMin);
//			System.out.println("de set of vertices with this degree is: "+ verticesOfSmallestDegree.toString());
//			HashTag vertex = (HashTag) verticesOfSmallestDegree.toArray()[0];
//			System.out.println("the vertexof this degree is: " + vertex.toString());
//			
//			
//			
//			
//			
//			
//			NeighborIndex<HashTag, DefaultWeightedEdge> index = new NeighborIndex<HashTag, DefaultWeightedEdge>(tempGraph);
//			Set<HashTag> neighborsOfVertex = index.neighborsOf(vertex);
//			for(HashTag neighbor: neighborsOfVertex){
//				double neighborDegree = densitymanager.getDegreeOfVertex(neighbor, tempGraph);
//				degreeVerticesMap.get(neighborDegree).remove(neighbor);
//			}
//			
//			
//			
//			
//			verticesOfSmallestDegree.remove(vertex);
//			System.out.println("this vertex is removed!");
//			if(verticesOfSmallestDegree.isEmpty()){
//				degreeVerticesMap.remove(tempMin);
//				System.out.println("map for this degree removed!");
//			}
//			//remove v and all its edges
//			tempGraph.removeVertex(vertex);
//			
//
//			for(HashTag neighbor: neighborsOfVertex){
//				double degreeOfNeighbor = densitymanager.getDegreeOfVertex(neighbor, inputGraph);;
//				if(degreeVerticesMap.containsKey(degreeOfNeighbor)){
////					System.out.println("the map contains the key/ vertices of this degree");	
////					System.out.println("a print of the current map: "+degreeVerticesMap.toString());
//					
//					degreeVerticesMap.get(degreeOfNeighbor).add(neighbor);
//				}else{
////					System.out.println("the map does not contain the key/ vertices of this degree");
//					Set<HashTag> setForThisDegree = new HashSet<HashTag>();
//					setForThisDegree.add(neighbor);
//					degreeVerticesMap.put(degreeOfNeighbor, setForThisDegree);
//				}
//			}	
//			
//			/*
//			 * if the density of tempGraph is larger than the density of tempDensestSubgraph
//			 * then store tempGraph as tempDensestSubgraph
//			 */
//			updateTempDensestSubgraph(densitymanager);
//	
//			System.out.println("#END OF WHILE#");
//			//check if the graph has at least 3  
//		}
//		
//		System.out.println("the method returns");
//		return tempDensestSubgraph;
//	}
//
//	@Deprecated
//	public SimpleWeightedGraph<HashTag, DefaultWeightedEdge> getDensestSubgraph3(SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph,
//			DensityManager<HashTag, DefaultWeightedEdge> densitymanager){
//		
//		
//		initializeTempGraphs(inputGraph);
//		
//		//PREPROCESSING STEP
//		degreeVerticesMap = new TreeMap<Double, Set<HashTag>>();	
//		
//		initializeDegreeVerticesMapWithVertices(inputGraph, densitymanager);
//		
//		while(!degreeVerticesMap.isEmpty()){
//			System.out.println("#START OF WHILE#");
//			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
//			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
//			
//			//get the smallest degree
//			double tempMin = ((TreeMap<Double, Set<HashTag>>) degreeVerticesMap).firstKey();
//			
//			System.out.println("smallest degree is: " + tempMin);
//			
//			//get 1 node with the smallest degree
//			Set<HashTag> verticesOfSmallestDegree = degreeVerticesMap.get(tempMin);
//			System.out.println("de set van vertices met deze degree is: "+ verticesOfSmallestDegree.toString());
//			
//			NeighborIndex<HashTag, DefaultWeightedEdge> index = new NeighborIndex<HashTag, DefaultWeightedEdge>(tempGraph);
//			Set<HashTag> neighborsOfVertex = new HashSet<HashTag>();
//			for(HashTag vertex: verticesOfSmallestDegree){
//				neighborsOfVertex.addAll(index.neighborsOf(vertex));				
//				System.out.println("de vertex met deze degree is: " + vertex.toString());
//				tempGraph.removeVertex(vertex);
//				System.out.println("this vertex is removed!");
//			}
//			degreeVerticesMap.remove(tempMin);
//			System.out.println("map for this degree removed!");
//			for(HashTag neighbor: neighborsOfVertex){
//				double neighborDegree = densitymanager.getDegreeOfVertex(neighbor, tempGraph);
//				degreeVerticesMap.get(neighborDegree).remove(neighbor);
//			}
//			
//			/*
//			 * if the density of tempGraph is larger than the density of tempDensestSubgraph
//			 * then store tempGraph as tempDensestSubgraph
//			 */
//			updateTempDensestSubgraph(densitymanager);
//			
//			System.out.println("#END OF WHILE#");
//			//check if the graph has at least 3  
//		}
//		
//		System.out.println("the method returns");
//		return tempDensestSubgraph;
//	}

	public CircularFifoQueue<SimpleWeightedGraph<HashTag, DefaultWeightedEdge>> getDensestSubgraphs(SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph,
			DensityManager<HashTag, DefaultWeightedEdge> densitymanager){
			
		initializeTempGraphs(inputGraph);
			
		//PREPROCESSING STEP
		degreeVerticesMap = new TreeMap<Double, Set<HashTag>>();	
		
		initializeDegreeVerticesMapWithVertices(inputGraph, densitymanager);
		
		while(!degreeVerticesMap.isEmpty()){
			System.out.println("#======== START OF WHILE ========#");
			System.out.println("current keyset: " + degreeVerticesMap.keySet().toString());
			System.out.println("Current valueset: " + degreeVerticesMap.values().toString());
			
			//get the smallest degree
			double tempMin = ((TreeMap<Double, Set<HashTag>>) degreeVerticesMap).firstKey();
			System.out.println("smallest degree is: " + tempMin);
			
			//get nodes with the smallest degree
			Set<HashTag> verticesOfSmallestDegree = degreeVerticesMap.get(tempMin);
			System.out.println("the set of vertices with the smallest degree is: "+ verticesOfSmallestDegree.toString());
			
			//get neighbors of the nodes with the smallest degree
			NeighborIndex<HashTag, DefaultWeightedEdge> index = new NeighborIndex<HashTag, DefaultWeightedEdge>(tempGraph);
			Set<HashTag> neighborsOfVerticesWithSmallestDegree = new HashSet<HashTag>();
			for(HashTag vertex: verticesOfSmallestDegree){
				System.out.println("looking for neighbors of: " + vertex.toString());
				neighborsOfVerticesWithSmallestDegree.addAll(index.neighborsOf(vertex));
			}
			System.out.println("the set of neighboring vertices  is:" + "\n" + neighborsOfVerticesWithSmallestDegree.toString());
			
			//remove the vertices with the smallest degree from the map
			degreeVerticesMap.remove(tempMin);
			

			//remove the neighbors from the map
			for(HashTag neighbor: neighborsOfVerticesWithSmallestDegree){
				double neighborDegree = densitymanager.getDegreeOfVertex(neighbor, tempGraph);
				if(neighborDegree != tempMin){
					degreeVerticesMap.get(neighborDegree).remove(neighbor);
					if(degreeVerticesMap.get(neighborDegree).isEmpty()){
						degreeVerticesMap.remove(neighborDegree);
					}
					System.out.println("removed the neighbor from map: " +  neighbor.toString()+ " with degree: "+ neighborDegree);
				}
			}
					
			//remove the vertices with the smallest degree from the tempGraph
			tempGraph.removeAllVertices(verticesOfSmallestDegree);
			
			//put the neighbors back in the map (if they aren't part of the removed vertices)
			for(HashTag neighbor: neighborsOfVerticesWithSmallestDegree){
				if(tempGraph.containsVertex(neighbor)){
					double degreeOfNeighbor = densitymanager.getDegreeOfVertex(neighbor, tempGraph);
					if(degreeVerticesMap.containsKey(degreeOfNeighbor)){
						degreeVerticesMap.get(degreeOfNeighbor).add(neighbor);
					}else{
						Set<HashTag> setForThisDegree = new HashSet<HashTag>();
						setForThisDegree.add(neighbor);
						degreeVerticesMap.put(degreeOfNeighbor, setForThisDegree);
					}
				}
			}	

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

	private void initializeTempGraphs(SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph) {
		tempGraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
		tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
	}
	

	private void initializeDegreeVerticesMapWithVertices(
			SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph,
			DensityManager<HashTag, DefaultWeightedEdge> densitymanager) {
		for (HashTag vertex : inputGraph.vertexSet()){
			double degreeOfVertex = densitymanager.getDegreeOfVertex(vertex, inputGraph);
			if(degreeVerticesMap.containsKey(degreeOfVertex)){
				degreeVerticesMap.get(degreeOfVertex).add(vertex);
			}else{
				Set<HashTag> setForThisDegree = new HashSet<HashTag>();
				setForThisDegree.add(vertex);
				degreeVerticesMap.put(degreeOfVertex, setForThisDegree);
			}
		}
	}

	private void updateTempDensestSubgraph(
			DensityManager<HashTag, DefaultWeightedEdge> densitymanager) {
		double densityOfTempGraph = densitymanager.getDensity(this.tempGraph);
		double densityOfTempDensestSubgraph = densitymanager.getDensity(this.tempDensestSubgraph);
		if (densityOfTempGraph > densityOfTempDensestSubgraph) {
			tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) tempGraph.clone();
		}
	}
	
	private void updateDensestSubgraphQueue(
			DensityManager<HashTag, DefaultWeightedEdge> densitymanager) {
		double densityOfTempGraph = densitymanager.getDensity(this.tempGraph);
		double densityOfTempDensestSubgraph = densitymanager.getDensity(this.tempDensestSubgraph);
		if (densityOfTempGraph > densityOfTempDensestSubgraph) {
			tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) tempGraph.clone();
			this.getDensestSubgraphQueue().add(tempDensestSubgraph);
		}
	}
	
	
	
}
