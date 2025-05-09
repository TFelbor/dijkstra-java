package mypackage;
import graph.AdjacencyListGraph;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class Main {

	public static void main(String[] args) {

		Graph g = new AdjacencyListGraph();

		String[] labels = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
		int[][] weights = { { -1, 6, -1, 4, -1, -1, -1, -1, 9, -1 }, { 6, -1, 3, 3, 1, -1, -1, -1, -1, -1 },
				{ -1, 3, -1, -1, 2, 2, -1, -1, -1, -1 }, { 4, 3, -1, -1, 4, -1, 6, -1, -1, -1 },
				{ -1, 1, 2, 4, -1, 8, 6, 7, -1, -1 }, { -1, -1, 2, -1, 8, -1, -1, 11, -1, -1 },
				{ -1, -1, -1, 6, 6, -1, -1, 3, 2, 2 }, { -1, -1, -1, -1, 7, 11, 3, -1, -1, 4 },
				{ 9, -1, -1, -1, -1, -1, 2, -1, -1, 1 }, { -1, -1, -1, -1, -1, -1, 2, 4, 1, -1 } };

		// build the graph
		Vertex[] verts = new Vertex[labels.length];
		for (int i = 0; i < labels.length; i++) {
			verts[i] = g.insertVertex(labels[i]);
		}

		for (int u = 0; u < labels.length; u++) {
			for (int v = u + 1; v < labels.length; v++) {
				if (weights[u][v] == -1) {
					continue;
				}
				g.insertEdge(verts[u], verts[v], weights[u][v]);
			}
		}

		// run Dijkstra's
		Dijkstra alg = new Dijkstra(g, e -> ((Integer) e.getObject()).doubleValue());
		alg.init(verts[0]);
		alg.run();

		// print distances and shortest paths
		for (Vertex v : g.vertices()) {
			System.out.println(verts[0].getObject() + " -> " + v.getObject() + ": " + alg.getDist(v));
			System.out.print("  path vertices:");
			for (Vertex u : alg.getPathVertices(v)) {
				System.out.print(" " + u.getObject());
			}
			System.out.println();
			System.out.print("  path edges:");
			for (Edge e : alg.getPathEdges(v)) {
				Vertex[] ends = g.endVertices(e);
				System.out.print(" " + ends[0].getObject() + ends[1].getObject() + "/" + e.getObject());
			}
			System.out.println();
		}
	}

}
