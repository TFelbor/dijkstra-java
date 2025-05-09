// Author: Tytus Felbor

package mypackage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

/**
 * Dijkstra's algorithm.
 */
public class Dijkstra {

	private Graph graph_; // the graph
	private EdgeWeight w_; // accessor for edge weights
	
	//Maps each vertex to its shortest distance from source
	private Map<Vertex, Double> distance;
	
	//Maps each vertex to the edge used to reach it on shortest path
	private Map<Vertex, Edge> previous;
	
	//Starting vertex (helps with path reconstruction)
	private Vertex start;

	/**
	 * Functional interface for edge weights.
	 */
	public interface EdgeWeight {

		/**
		 * Get the weight for edge e.
		 *
		 * @param e
		 *          edge to get the weight for
		 * @return weight for edge e
		 */

		double weight ( Edge e );
	}

	/**
	 * Ordering of vertices within the priority queue.
	 */
	private class VertComparator implements Comparator<Vertex> {

		@Override
		public int compare ( Vertex u, Vertex v ) {

			double distU = distance.get(u);
			double distV = distance.get(v);

			// Compare vertices based on their distance from the source
			if (distU < distV) return -1;
			else if (distU > distV) return 1;
			else return 0;

		}
	}

	/**
	 * Create an instance of Dijkstra's algorithm to compute shortest paths from s
	 * to all other vertices. Assumes that the weight for edge e is
	 * (Double)e.getObject().
	 *
	 * @param g
	 *          the graph
	 */
	public Dijkstra ( Graph g ) {

		this(g,e -> ((Double) e.getObject()).doubleValue());

	}

	/**
	 * Create an instance of Dijkstra's algorithm to compute shortest paths from s
	 * to all other vertices.
	 *
	 * @param g
	 *          the graph
	 * @param w
	 *          function taking an Edge and returning the weight for the edge (as
	 *          a double)
	 */
	public Dijkstra ( Graph g, EdgeWeight w ) {

		graph_ = g;
		w_ = w;

	}

	/**
	 * Initialize the algorithm.
	 *
	 * @param s
	 *          starting vertex
	 */

	public void init ( Vertex s ) {

		// Initialize distance and previous edge maps (helpers)
		start = s;
		distance = new HashMap<>();
		previous = new HashMap<>();

		// Initialize distances to infinity and previous edges to null
		for (Vertex v : graph_.vertices()) {
			
			distance.put(v, Double.MAX_VALUE);
			previous.put(v, null);
			
		}

		// Distance to start vertex is 0
		distance.put(s, 0.0);
	}

	/**
	 * Compute shortest paths from s to all vertices. Requires init(s) to have
	 * been called first.
	 */
	public void run () {
		
		run(null);
		
	}

	/**
	 * Compute shortest path from vertex s to vertex f. Requires init(s) to have
	 * been called first.
	 *
	 * @param f
	 *          finish vertex; if null, compute the shortest path to all vertices
	 */
	public void run ( Vertex f ) {

		// Create priority queue ordered by distance from source
    // Vertices with smaller distances will be processed first
		PriorityQueue<Vertex> pq = new PriorityQueue<>(new VertComparator());

		// Add all vertices to the priority queue
		for (Vertex v : graph_.vertices()) {
			
			pq.add(v);
			
		}

		// Main loop of Dijkstra's algorithm
		while (!pq.isEmpty()) {
			
			// Get the vertex with minimum distance
			Vertex v = pq.poll();

			// If we've reached the finish vertex => break (done)
			if (v.equals(f)) break;

			/* If we've pulled a vertex with infinite distance,
			 * all remaining vertices are unreachable */
			if (distance.get(v) == Double.MAX_VALUE) break;

			// Process all edges incident to v
			for (Edge e : graph_.incidentEdges(v)) {

				Vertex u = graph_.opposite(v, e);

				// Calculate potential new distance through v
				double weight = w_.weight(e);
				double newDist = distance.get(v) + weight;

				// If a shorter path to u is found
				if (newDist < distance.get(u)) {

					// Update the distance and the previous edge
					distance.put(u, newDist);
					
          /* Store the edge instead of vertex in prev, 
					*  a modification from standard Dijkstra that makes path retrieval easier */
					previous.put(u, e);

					/* The priority queue doesn't implement decreaseKey,
					 * instead we remove and re-add.
					 * O(n) but works */
					pq.remove(u);
					pq.add(u);
					
				}
			}
		}
	}

	/**
	 * Get the vertices on the shortest path from vertex s to vertex f. Requires
	 * run() or run(f) to have been called first.
	 *
	 * @param f
	 *          finish vertex
	 * @return the vertices on the shortest path from vertex s to vertex f
	 */
	public Iterable<Vertex> getPathVertices ( Vertex f ) {

		// Data structure to help building path f -> s, then reversing to s -> f
		LinkedList<Vertex> path = new LinkedList<>();

		// If there's no path to f, return an empty list
		if (distance.get(f) == Double.MAX_VALUE) {

			return path;

		}

		// Add the finish vertex
		path.addFirst(f);

		// Trace back f -> s
		Vertex current = f;

		// Trace edges to the source
		while (!current.equals(start)) {

			// Reverse the path
			Edge e = previous.get(current);
			current = graph_.opposite(current, e);
			path.addFirst(current);

		}

		return path;
	}

	/**
	 * Get the edges on the shortest path from vertex s to vertex f. Requires
	 * run() or run(f) to have been called first.
	 *
	 * @param f
	 *          finish vertex
	 * @return the edges on the shortest path from vertex s to vertex f
	 */
	public Iterable<Edge> getPathEdges ( Vertex f ) {

		LinkedList<Edge> path = new LinkedList<>();

		// If there's no path to f, return an empty list
		if (distance.get(f) == Double.MAX_VALUE) {

			return path;

		}

		// Trace back f -> s
		Vertex current = f;
		
		// Trace edges to the source
		while (!current.equals(start)) {

			// Reverse the path again, to get the correct order 
			Edge e = previous.get(current);
			path.addFirst(e);
			current = graph_.opposite(current, e);

		}

		return path;
	}

	/**
	 * Get the length of the shortest path from vertex s to vertex f. Requires
	 * run() or run(f) to have been called first.
	 *
	 * @param f
	 *          finish vertex
	 * @return the length of the shortest path from vertex s to vertex f
	 */
	public double getDist ( Vertex f ) {
 
		return distance.get(f);

	}
}