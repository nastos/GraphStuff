import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
//import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * @author nastos
 *
 */
public class Recognition {

	public static void main(String[] args) {
        SimpleGraph<Integer, DefaultEdge>  g = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class); 
        for (int i=1;i<=10; i++) g.addVertex(i);

//        for (int i=1;i<=10; i+=2) {
//        	for (int j=i+1;j<=10; j++) {
//        		g.addEdge(i,j);
//        	}
//        }
        

		g.addEdge(1,2);
		g.addEdge(2,4);
		g.addEdge(4,8);
		g.addEdge(8,3);
		g.addEdge(3,7);
		g.addEdge(1,7);
		g.addEdge(1,9);
		g.addEdge(2,9);
		g.addEdge(3,9);
		g.addEdge(4,9);
		g.addEdge(5,9);
		g.addEdge(6,9);
		g.addEdge(7,9);
		g.addEdge(8,9);

		
        System.out.println(g);
        System.out.println(getComplement(g));
        System.out.println(isWeaklyChordal(g));
        degSeq(g);
        
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
	 * Determines if a graph is chordal
	 * @param g
	 * @return true if g is chordal. false otherwise
	 */
	
	public static <V,E> boolean isChordal (SimpleGraph<V,E> g) {
		// Take each vertex. If it is not in a large cycle, remove and go on
		SimpleGraph<V,E> g2 = (SimpleGraph<V, E>) g.clone();
		while (g2.vertexSet().size() > 0) {
			V v = getSimplicial(g2);
			if (v==null) return false;
			g2.removeVertex(v);
			System.out.println(g2);
		}
		return true;		
	}
	
	/**
	 * Returns a copy of g. For now, a shallow copy, using .clone(). Might change to deep copy if needed.
	 * @param g
	 * @return a copy of g
	 */
	
	public static <V,E> SimpleGraph<V,E> copy (SimpleGraph<V,E> g) {
		return (SimpleGraph<V, E>) g.clone();
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
	 * Determines if a graph is weakly chordal, using algorithm that repeatedly adds an edge between a two-pair. Performs algorithm of complement if it
	 * is more efficient.
	 * @param g
	 * @return boolean
	 */
	public static <V,E> boolean isWeaklyChordal(SimpleGraph<V,E> g) {
		int N = g.vertexSet().size();
		int allPairs = (N%2==0)?(N/2)*(N-1):N*((N-1)/2);
		int M = g.edgeSet().size();
		SimpleGraph<V,E> g2;
		if (2*M +5 < allPairs) g2=getComplement(g);
		else g2 = copy(g);
		ArrayList<V> vertices = new ArrayList<V>(g2.vertexSet());
		boolean isWC = true;
		M = g2.edgeSet().size();
		while (isWC==true) {
			isWC=false;
			for (int i=0; i<N; i++) {
				for (int j=i+1; j<N; j++) {
					if (isTwoPair(g2,vertices.get(i),vertices.get(j))) {
						isWC=true;
						g2.addEdge(vertices.get(i), vertices.get(j));
						M+=1;
						System.out.println(M + " " + allPairs + " Added edge in two-pair " + vertices.get(i) + " " + vertices.get(j));
						if (M +4 >= allPairs) return true;
					}
				}
			}
		}
		return false;		
		
	}
	

	
}
