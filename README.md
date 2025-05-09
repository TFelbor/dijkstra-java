# CPSC327 : Data Structures & Algs. Project 2 - Dijkstra's Algorithm Implementation

This project implements Dijkstra's algorithm for finding the shortest paths between nodes in a graph. The implementation is written in Java and provides a comprehensive solution for computing single-source shortest paths in weighted graphs.

## Features
- Find shortest paths from a source vertex to all other vertices in a graph
- Retrieve path vertices and edges for any destination vertex
- Get the distance (shortest path length) to any vertex
- Support for custom edge weight functions
- Efficient priority queue implementation for vertex selection

## Implementation Details

### Classes
- `Dijkstra`: The main class implementing Dijkstra's algorithm
- `EdgeWeight`: A functional interface for providing edge weights
- `VertComparator`: A comparator for ordering vertices in the priority queue based on their distance from the source

### Algorithm
The implementation follows the standard Dijkstra's algorithm with the following steps:
1. Initialize distances to infinity for all vertices except the source
2. Use a priority queue to process vertices in order of increasing distance
3. For each vertex, update distances to adjacent vertices if a shorter path is found
4. Track the previous edge used to reach each vertex for path reconstruction

### Time Complexity
- Overall: O((V + E) log V) where V is the number of vertices and E is the number of edges
- The implementation includes an O(n) removal and re-addition to the priority queue as a workaround for the lack of a decreaseKey operation

## Usage Example
```java
// Create a graph
Graph g = new AdjacencyListGraph();

// Add vertices and edges
Vertex v1 = g.insertVertex("A");
Vertex v2 = g.insertVertex("B");
g.insertEdge(v1, v2, 6); // Edge with weight 6

// Initialize Dijkstra's algorithm
Dijkstra alg = new Dijkstra(g, e -> ((Integer) e.getObject()).doubleValue());
alg.init(v1);
alg.run();

// Get the shortest distance
double dist = alg.getDist(v2);

// Get the path vertices
Iterable<Vertex> pathVertices = alg.getPathVertices(v2);

// Get the path edges
Iterable<Edge> pathEdges = alg.getPathEdges(v2);
```

## Sample Output
Running the Main class creates a sample graph and outputs the shortest paths:
```
A -> A: 0.0
  path vertices: A
  path edges:
A -> B: 6.0
  path vertices: A B
  path edges: AB/6
...
```

## Notes
- The implementation assumes non-negative edge weights
- The algorithm terminates early if the destination vertex is reached or if remaining vertices are unreachable
- Edge objects store their weights, which can be accessed via a custom weight function

