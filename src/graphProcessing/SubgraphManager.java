package graphProcessing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import tweets.HashTag;

public class SubgraphManager {

	
	public SubgraphManager(){
	}
	
	public SimpleWeightedGraph<HashTag, DefaultWeightedEdge> getDensestSubgraph(SimpleWeightedGraph<HashTag, DefaultWeightedEdge> inputGraph,
			DensityManager<HashTag, DefaultWeightedEdge> densitymanager){
		
		
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempGraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
		SimpleWeightedGraph<HashTag, DefaultWeightedEdge> tempDensestSubgraph = (SimpleWeightedGraph<HashTag, DefaultWeightedEdge>) inputGraph.clone();
		
		//PREPROCESSING STEP
		Map<Double, Set<HashTag>> degreeVerticesMap = new HashMap<Double, Set<HashTag>>();	
		for (HashTag vertex : inputGraph.vertexSet()){
			double degreeOfVertex = densitymanager.getDegreeOfVertex(vertex, inputGraph);
			if(degreeVerticesMap.containsKey(degreeOfVertex)){
				degreeVerticesMap.get(degreeVerticesMap).add(vertex);
			}else{
				Set<HashTag> setForThisDegree = new HashSet<HashTag>();
				setForThisDegree.add(vertex);
				degreeVerticesMap.put(degreeOfVertex, setForThisDegree);
			}	
		}
		
		while(!degreeVerticesMap.isEmpty()){
			
			//get the smallest degree
			double tempMin = Double.MAX_VALUE;
			for(double degree: degreeVerticesMap.keySet()){
				if (degree < tempMin) {
					tempMin = degree;
				}
			}
			
			//get 1 node with the smallest degree
			Set<HashTag> verticesOfSmallestDegree =degreeVerticesMap.get(tempMin);
			HashTag vertex = (HashTag) verticesOfSmallestDegree.toArray()[0];
			verticesOfSmallestDegree.remove(vertex);
			if(verticesOfSmallestDegree.isEmpty()){
				degreeVerticesMap.remove(tempMin);
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
		}
		
		return tempDensestSubgraph;
	}
}
