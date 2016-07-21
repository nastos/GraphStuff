import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
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
			if (vertices.contains(v)) continue;

			count=0;
			partials = new HashSet<V>();
	
			for (V u : vertices) {
				if (g.containsEdge(u,v)) count++;
			}
			// v is partial on the set if count is not 0 and not the size of the set
			partials.add(v);
		}
		
		if (count > 0) {
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
	 * Max Cardinality Search (MCS) of a graph g starting at vertex v. Not linear time. Needs a sorted list data structure to extract max quickly.
	 * 
	 * @param g
	 * @param v
	 * @return a list of vertices in MCS order
	 */
	public static <V,E> List<V> MCS (SimpleGraph<V,E> g, V v){

		if (g.vertexSet().contains(v) == false) {
			System.err.println("Cannot perform MCS starting at " + v + " because it is not in the graph. Returned null.");
			return null;
		}
		
		mySortedList<V> values = new mySortedList<V>();
		for (V u : g.vertexSet()) {
			if (u==v) continue;
			values.add(u, 0);
		}
		
		List<V> output = new ArrayList<V>();
		output.add(v);
		
		for (V u : Graphs.neighborListOf(g, v)) {
			values.increment(u);
		}

		System.out.println(values);
		while (output.size() < g.vertexSet().size()) {
			V u = values.pop().getKey();
			output.add(u);
			for (V x : Graphs.neighborListOf(g, u)) {
				if (values.contains(x)) values.increment(x);
			}			
			//System.out.println(values);
		}
		
		return output;
	}
	
	
	/** Private method to increment an entry in this list and maintain sorted order
	 * @param <V>
	 * @param <V> vertex type
	 * @param sortedList list of Map Entries
	 * @param u
	 */
	private static <V> void increment(List<Entry<V, Integer>> sortedList, Map<V, Integer> values, V u) {
		int oldVal = values.get(u);
		values.put(u,oldVal+1);
		int uPos = -1;
		
		int positionToInsert = 0;
		for (int position=0; position<sortedList.size(); position++) {
			if (sortedList.get(position).getValue() <= (oldVal+1)) {
				 
			}
		}
			
		
	}


	/**
	 * LexBFS of a graph g starting at vertex v
	 * 
	 * @param g
	 * @param v
	 * @return a list of vertices in LexBFS order
	 */
	public static <V,E> List<V> lexBFS (SimpleGraph<V,E> g, V v){

		if (g.vertexSet().contains(v) == false) {
			System.err.println("Cannot perform lexbfs starting at " + v + " because it is not in the graph. Returned null.");
			return null;
		}
		
		List<List<V>> listOfLists = new ArrayList<List<V>>();
		listOfLists.add(new ArrayList<V>(g.vertexSet()));

		ArrayList<V> lexorder = new ArrayList<V>();
		listOfLists.get(0).remove(v);
		lexorder.add(v);
		Tools.pivot(listOfLists,g,lexorder.get(0));	
		
		for (int i=1; i<g.vertexSet().size(); i++) {
			lexorder.add(extract(listOfLists));
			Tools.pivot(listOfLists,g,lexorder.get(i));
		}

		return lexorder;
	}
	
	/**
	 * Private method to split vertex partitions within a lexBFS search.
	 * @param listoflists
	 * @param g
	 * @param v
	 */
	private static <V,E> void pivot(List<List<V>> listoflists, SimpleGraph<V,E> g, V v) {
		// v is assumed to NOT be in the list. i.e. it is extracted first, then we pivot on it, then the vertex is added in this process.
		int index = 0;
		if (listoflists == null) return;
		while (index < listoflists.size()) {
			if (listoflists.get(index).size() == 0) {
				listoflists.remove(index);
				continue;
			}
			List<V> outside = new LinkedList<V>();
			for (V u : listoflists.get(index)) {
				if (g.containsEdge(u, v) == false) outside.add(u);
			}
			if (outside.size() > 0) {
				listoflists.get(index).removeAll(outside);
				listoflists.add(index+1, outside);				
			}
			//LinkedList<V> single = new LinkedList<V>();
			//single.add(v);
			//listoflists.add(index, single);
			//index += 3;
			index += 2;
		}
	}

	/**
	 * private method to find the first non-null, non-empty item in a list of lists
	 * @param listOfLists
	 * @return
	 */
	private static <V> V extract(List<List<V>> listOfLists) {
		if (listOfLists.size() == 0) return null;
		for (List<V> ll : listOfLists) {
			if (ll.size() == 0) continue;
			return ll.remove(0);
		}
		return null;
	}
	
	/**
	 * Turns a list of list elements into a single list of elements. Used in partition-based implementation of LexBFS
	 * 
	 * @param listoflists
	 * @return
	 */
	private static <V> List<V> listize(List<List<V>> listoflists) {
		List<V> output = new ArrayList<V>();
		for (List<V> l : listoflists) {
			for (V v : l) {
				output.add(v);
			}
		}
		return output;
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

	/**
	 * Checks if v is the top of a house or bigger structure in graph g. Used to recognize house,hole-free graphs
	 * @param g
	 * @param v
	 * @return
	 */

	public static <V,E> boolean isTopOfBuilding(SimpleGraph<V,E> g, V v) {
		List<V> nbrs = new ArrayList<V>(Graphs.neighborListOf(g, v));
		for (int i=0; i<nbrs.size(); i++) {
			V x = nbrs.get(i);
			for (int j=i+1; j<nbrs.size(); j++) {
				V y = nbrs.get(j);
				SimpleGraph<V,E> h = copy(g);
				ArrayList<V> commonNbrs = (ArrayList<V>) Graphs.neighborListOf(g, x);
				commonNbrs.retainAll(Graphs.neighborListOf(g, y));
				h.removeAllVertices(commonNbrs); // removes v and all other commoners

				if (g.containsEdge(x, y)) {
					h.removeEdge(x, y);
				}
				
				ConnectivityInspector<V,E> ci = new ConnectivityInspector(h);
				
				if (ci.pathExists(x, y)) return true;
			}
		}
		return false;
	}
	
	/**
	 * Greedily finds a large clique in g. Serves as lower bound to Max Clique.
	 * @param g a simple graph
	 * @return A set of vertices inducing a clique
	 */
	
	public static <V,E> Set<V> greedyClique(SimpleGraph<V,E> g) {
		
		Map<V,Integer> deg = new HashMap<V,Integer>();
		for (V v: g.vertexSet()) deg.put(v, g.degreeOf(v));
		List<Map.Entry<V,Integer>> sortedDeg = MapSortByVal.getMapSortedByValue(deg);
		
		Set<V> clique = new HashSet<V>();
		
		int i=sortedDeg.size();
		while (i > 0) {
			i--;
			boolean addIt = true;
			for (V v : clique) {
				if (g.containsEdge(v, sortedDeg.get(i).getKey()) == false) {
					addIt=false;
					break;
				}
			}
			if (addIt) clique.add(sortedDeg.get(i).getKey());
		}
		
		return clique;
	}

	/**
	 * Do not use unless this message is removed
	 *
	 * @param <V>
	 */
	private class lexComparator <V> implements Comparator<Map.Entry<V, ArrayList<Integer>>>{

		// UNUSED METHOD. DO NOT USE. KEPT FOR HISTORICAL REASONS
		
		@Override
		public int compare(Entry<V, ArrayList<Integer>> arg0, Entry<V, ArrayList<Integer>> arg1) {
			ArrayList<Integer> list0 = arg0.getValue();
			ArrayList<Integer> list1 = arg0.getValue();
			
			while (true) {
				if (list0.isEmpty()) return Integer.MAX_VALUE;
				if (list1.isEmpty()) return Integer.MIN_VALUE;
				int a = list0.remove(0);
				int b = list1.remove(0);
				if (a != b) return b-a;
				// otherwise, iterate through while loop again with smaller lists
			}
		}
		
	}	
}

