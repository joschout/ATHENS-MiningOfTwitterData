package graphProcessing;

import java.util.Set;

import org.jgrapht.Graph;

/**
 * 
 * 
 * @author Jonas
 *
 * @param <V>
 * @param <E>
 */
public class WeightedDensityManager<V, E> implements DensityManager<V, E> {

	/**
	 * Returns the density of the graph
	 */
	@Override
	public double getDensity(Graph<V, E> graph) {
		
		
	}

	/**
	 * Returns the degree of a vertex.
	 * 
	 *  weighted degree of a node =
	 *  = the sum of the weights of the edges incident to the node.
	 * 
	 */
	@Override
	public double getDegreeOfVertex(V vertex, Graph<V, E> graph) {
		Set<E> edgesOfvertex = graph.edgesOf(vertex);
		double degree = 0;
		for(E edge: edgesOfvertex){
			degree += graph.getEdgeWeight(edge);
		}
		return degree;
		
	}

}
