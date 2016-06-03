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

public class Recognition {

	public static void main(String[] args) {
        SimpleGraph<Integer, DefaultEdge>  g = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class); 
        for (int i=1;i<=10; i++) g.addVertex(i);
        
        for (int i=1;i<=10; i++) {
        	for (int j=i+1;j<=10; j++) {
        		g.addEdge(i,j);
        	}
        }
        
        System.out.println("isChordal(g) = " + isChordal(g));
        g.removeEdge(4,5);
        System.out.println("isChordal(g) = " + isChordal(g));
        g.removeEdge(7,8);
        System.out.println("isChordal(g) = " + isChordal(g));

	}

	public static <V, E> int[] degSeq(SimpleGraph<V,E> g) {
		// gives int array sorted by natural order of vertex order, if one exists.
		int n = g.vertexSet().size();
		int[] s = new int[n];
		int i=0;
		for (V v : g.vertexSet()) {
			s[i] = g.degreeOf(v);
			i++;
		}
		return s;
	}

	public static <V,E> boolean isSimplicial(SimpleGraph<V,E> g, V v) {
		ArrayList<V> nbrs = (ArrayList<V>) Graphs.neighborListOf(g, v);
				
		for (int a=0; a<nbrs.size(); a++) {
			for (int b=a+1; b<nbrs.size(); b++) {
				if (g.containsEdge(nbrs.get(a), nbrs.get(b)) == false) return false;
			}
		}
		return true;
	}
	
	public static <V,E> boolean hasSimplicial(SimpleGraph<V,E> g) {
		for (V v : g.vertexSet()) {
			if (isSimplicial(g,v)) return true;
		}
		return false;
	}
	
	public static <V,E> V getSimplicial(SimpleGraph<V,E> g) {
		// returns 
		for (V v : g.vertexSet()) {
			if (isSimplicial(g,v)) return v;
		}
		return null;
	}

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
	
	public static <V,E> SimpleGraph<V,E> copy (SimpleGraph<V,E> g) {
		return (SimpleGraph<V, E>) g.clone();
	}
	
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
	


	
}
