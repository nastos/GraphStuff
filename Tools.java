import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleGraph;

public class Tools {

	/**
	 * Returns a copy of g. For now, a shallow copy, using .clone(). Might change to deep copy if needed.
	 * @param g
	 * @return a copy of g
	 */
	
	public static <V,E> SimpleGraph<V,E> copy (SimpleGraph<V,E> g) {
		return (SimpleGraph<V, E>) g.clone();
	}

	/**
	 * Finds Partial closure of a subset of vertices
	 * @param g
	 * @return
	 */
	public static <V,E> Collection<V> partialClosure(SimpleGraph<V,E> g, Collection<V> vertices){
		// (P4,C4,2K2)-free . AKA trivially perfect and co-triv perfect. AKA split cograph.
		HashSet<V> partials=new HashSet<V>();
		int count=0;
		
		for (V v : g.vertexSet()) {
			count=0;
			partials = new HashSet<V>();
	
			if (vertices.contains(v)) continue;
	
			for (V u : vertices) {
				if (g.containsEdge(u,v)) count++;
			}
			// v is partial on the set if count is not 0 and not the size of the set
			partials.add(v);
		}
		
		if (count > 0 && count < vertices.size()) {
			partials.addAll(vertices);
			return partialClosure(g, partials);
		}
		else return vertices;
	}

	/**
	 * Determines if a and b form a two-pair in g
	 * @param g
	 * @param a
	 * @param b
	 * @return boolean
	 */
	
	public static <V,E> boolean isTwoPair(SimpleGraph<V,E> g, V a, V b) {
		if (g.containsEdge(a,b) == true) return false;
		SimpleGraph<V,E> g2 = (SimpleGraph<V, E>) g.clone();
		HashSet<V> toDelete = new HashSet<V>(Graphs.neighborListOf(g2,a));
		toDelete.retainAll(Graphs.neighborListOf(g2, b));
		g2.removeAllVertices(toDelete);
		DijkstraShortestPath d = new DijkstraShortestPath(g2,a,b);
		if (d.getPathEdgeList() == null) return true;
		return false;
	}

	/**
	 * Computes the degree sequence of a graph
	 * @param g a Simple Graph
	 * @return int[] degree sequence
	 */
	
	public static <V, E> int[] degSeq(SimpleGraph<V,E> g) {
		int n = g.vertexSet().size();
		int[] s = new int[n];
		int i=0;
		for (V v : g.vertexSet()) {
			s[i] = g.degreeOf(v);
			i++;
		}
		return s;
	}

	/**
	 * Returns the graph complement of g
	 * @param g
	 * @return graph complement
	 */
	
	public static <V,E> SimpleGraph<V,E> getComplement(SimpleGraph<V,E> g) {
		SimpleGraph<V,E> comp = new SimpleGraph<V,E>(g.getEdgeFactory());
		for (V v : g.vertexSet()) {
			comp.addVertex(v);
			for (V u : comp.vertexSet()) {
				if (u==v) continue;
				if (g.containsEdge(u, v) == false) comp.addEdge(u, v);
			}
		}
		return comp;
	}

	/**
	 * Gets a simplicial vertex in a graph.
	 * @param g a simple graph
	 * @return V a simplicial vertex in g, or null if none.
	 */
	
	public static <V,E> V getSimplicial(SimpleGraph<V,E> g) {
		// returns 
		for (V v : g.vertexSet()) {
			if (isSimplicial(g,v)) return v;
		}
		return null;
	}

	/**
	 * returns a map of vertex colours from 1 to k found by a greedy colouring with unknown vertex order
	 * @param g
	 * @return Map with integer colours
	 */
	public static <V,E> Map<V,Integer> greedyColoring (SimpleGraph<V,E> g){
		return greedyColouring(g);
	}

	/**
	 * returns a map of vertex colours from 1 to k found by a greedy colouring processed in given vertex order
	 * @param g
	 * @return Map with integer colours
	 */
	public static <V,E> Map<V,Integer> greedyColoring (SimpleGraph<V,E> g, ArrayList<V> vertexOrder){
		return greedyColouring(g,vertexOrder);
	}

	/**
	 * returns a map of vertex colours from 1 to k found by a greedy colouring with unknown vertex order
	 * @param g
	 * @return Map with integer colours
	 */
	public static <V,E> Map<V,Integer> greedyColouring (SimpleGraph<V,E> g){
		Map<V,Integer> m = new HashMap<V,Integer>();
		ArrayList<V> vertexOrder = new ArrayList<V>(g.vertexSet());
		return greedyColouring(g,vertexOrder);
	}

	/**
	 * returns a map of vertex colours from 1 to k found by a greedy colouring processed in given vertex order
	 * @param g
	 * @return Map with integer colours
	 */
	public static <V,E> Map<V,Integer> greedyColouring (SimpleGraph<V,E> g, ArrayList<V> vertexOrder){
		Map<V,Integer> m = new HashMap<V,Integer>();
		for (int i=0; i<vertexOrder.size(); i++) {
			//find colours of neighbours
			//assign lowest colour available
			HashSet<Integer> usedColours = new HashSet<Integer>();
			for (int j=0; j<i; j++) {
				if (g.containsEdge(vertexOrder.get(i), vertexOrder.get(j)) && m.containsKey(vertexOrder.get(j))) usedColours.add(m.get(vertexOrder.get(j)));
			}
			Integer newCol = 1;
			while (usedColours.contains(newCol)) {
				newCol++;
			}
			m.put(vertexOrder.get(i), newCol);
		}
		return m;
	}

	/**
	 * Checks if a graph has a simplicial vertex
	 * @param g
	 * @return true if the graph has a simplicial vertex
	 */
	
	public static <V,E> boolean hasSimplicial(SimpleGraph<V,E> g) {
		for (V v : g.vertexSet()) {
			if (isSimplicial(g,v)) return true;
		}
		return false;
	}

	/**
	 * Determines if a vertex is simplicial in a graph
	 * @param g a simple graph
	 * @param v a vertex in the graph
	 * @return true if the vertices is simplicial in g. Otherwise, false.
	 */
	
	public static <V,E> boolean isSimplicial(SimpleGraph<V,E> g, V v) {
		ArrayList<V> nbrs = (ArrayList<V>) Graphs.neighborListOf(g, v);
				
		for (int a=0; a<nbrs.size(); a++) {
			for (int b=a+1; b<nbrs.size(); b++) {
				if (g.containsEdge(nbrs.get(a), nbrs.get(b)) == false) return false;
			}
		}
		return true;
	}

}
