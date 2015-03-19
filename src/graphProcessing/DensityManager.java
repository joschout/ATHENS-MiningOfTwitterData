package graphProcessing;

import org.jgrapht.Graph;

/**
 * Interface containing method for querying the degrees of vertices in a graph and the density of the graph.
 * @author Jonas
 *
 * @param <V>
 * @param <E>
 */
public interface DensityManager<V, E> {

	public double getDensity(Graph<V, E> graph);
	
	public  double  getDegreeOfVertex(V vertex, Graph<V, E> graph);
}
