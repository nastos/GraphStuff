import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Generation{

	public static void main(String[] args) {

	}

	/**
	 * Builds an Erdos Renyi random graph with edge probability p
	 * @param n
	 * @param p
	 * @return
	 */
	public static SimpleGraph<Integer,DefaultEdge> ER(int n, double p){
		// Returns a graph with n nodes named 1 to n.
		SimpleGraph<Integer,DefaultEdge> g = new SimpleGraph<Integer,DefaultEdge>(DefaultEdge.class);
		Random r = new Random();


		for (int i=1; i<=n; i++) g.addVertex(i);
		for (int i=1; i<n; i++) {
			for (int j=1; j<=n; j++) {
				if (r.nextDouble() <= p) g.addEdge(i, j);
			}
		}
		return g;
	}

	/**
	 * Builds an ER-type of random bipartite graph with edge probability p
	 * @param n, m
	 * @param p
	 * @return
	 */
	public static SimpleGraph<Integer,DefaultEdge> BipartiteER(int n, int m, double p){
		// Returns a graph with bipartition 1 to n and (n+1) to (n+m).
		SimpleGraph<Integer,DefaultEdge> g = new SimpleGraph<Integer,DefaultEdge>(DefaultEdge.class);
		Random r = new Random();


		for (int i=1; i<=n; i++) g.addVertex(i);
		for (int i=n+1; i<=n+m; i++) g.addVertex(i);

		for (int i=1; i<n; i++) {
			for (int j=n+1; j<=n+m; j++) {
				if (r.nextDouble() <= p) g.addEdge(i, j);
			}
		}
		return g;
	}

	/**
	 * Builds an ER-type of random k-partite graph with edge probabilities ab, bc, ca for probability of an edge between each pair of parts
	 * @param A
	 * @param B
	 * @param C
	 * @param ab
	 * @param bc
	 * @param ca
	 * @return
	 */
	public static SimpleGraph<Integer,DefaultEdge> BipartiteER(int A, int B, int C, double ab, double bc, double ca){
		// Returns a graph with bipartition 1 to n and (n+1) to (n+m).
		SimpleGraph<Integer,DefaultEdge> g = new SimpleGraph<Integer,DefaultEdge>(DefaultEdge.class);
		Random r = new Random();


		for (int i=1; i<=A; i++) g.addVertex(i);
		for (int i=A+1; i<=A+B; i++) g.addVertex(i);
		for (int i=A+B+1; i<=A+B+C; i++) g.addVertex(i);

		for (int i=1; i<A; i++) {
			for (int j=A+1; j<=A+B; j++) {
				if (r.nextDouble() <= ab) g.addEdge(i, j);
			}
		}

		for (int i=1; i<A; i++) {
			for (int j=A+B+1; j<=A+C; j++) {
				if (r.nextDouble() <= ca) g.addEdge(i, j);
			}
		}

		for (int i=A+1; i<A+B; i++) {
			for (int j=A+B+1; j<=A+B+C; j++) {
				if (r.nextDouble() <= bc) g.addEdge(i, j);
			}
		}
		
		return g;
	}

	/**
	 * Building an Erdos Renyi random graph with exactly m edges 
	 * @param n
	 * @param m
	 * @return
	 */
	public static SimpleGraph<Integer,DefaultEdge> ER(int n, int m){
		// Returns a graph with n nodes named 1 to n and exactly m edges.
		SimpleGraph<Integer,DefaultEdge> g = new SimpleGraph<Integer,DefaultEdge>(DefaultEdge.class);
		Random r = new Random();

		for (int i=1; i<=n; i++) g.addVertex(i);
		int numEdges=0;
		while (numEdges < m) {
			int a = r.nextInt(n-1)+1; // nextInt generates between 0 and n-1 here. Nodes are 1 to n.
			int b = r.nextInt(n-1)+1;
			if (g.containsEdge(a,b)) continue;
			numEdges++;
			g.addEdge(a, b);
		}
		return g;
	}

	public static SimpleGraph<Integer,DefaultEdge> path(int n, int i){
		// Returns a path on n vertices with vertex labels starting at i
		// vertices will be labeled i to n+i-1 (so path(n,1) makes a path with vertices labeled 1 to n)
		SimpleGraph<Integer,DefaultEdge> g = new SimpleGraph<Integer,DefaultEdge>(DefaultEdge.class);
		if (n==0) return null;
		g.addVertex(i);
		for (int j=i+1; j<=n+i-1; j++) {
			g.addVertex(j);
			g.addEdge(j, j-1);
		}
		return g;
	}

	public static SimpleGraph<Integer,DefaultEdge> path(int n){
		// Returns a path on n vertices with vertex labels starting at 1
		return path(n,1);
	}

	public static SimpleGraph<Integer,DefaultEdge> cycle(int n, int i){
		// Returns a cycle on n vertices with vertex labels starting at i.
		// vertices will be labeled i to n+i-1, so cycle(n,1) makes a cycle with vertices labeled 1 to n.
		SimpleGraph<Integer,DefaultEdge> g = new SimpleGraph<Integer,DefaultEdge>(DefaultEdge.class);
		if (n==0) return null;
		g.addVertex(i);
		if (n==1) return g;
		g.addVertex(i+1);
		g.addEdge(i+1, i);		
		if (n==2) return g;						
		for (int j=i+2; j<=n+i-1; j++) {
			g.addVertex(j);
			g.addEdge(j, j-1);
		}
		g.addEdge(n+i-1, i);
		return g;
	}

	public static SimpleGraph<Integer,DefaultEdge> cycle(int n){
		// Returns a cycle on n vertices with vertex labels starting at 1
		return cycle(n,1);
	}

	public static SimpleGraph<Integer,DefaultEdge> disjointUnion(SimpleGraph<Integer,DefaultEdge> g1, SimpleGraph<Integer,DefaultEdge> g2){
		// assumes the vertex set of g1 and g2 are disjoint. If any vertices are identical, they merge via standard node identification (untested - might throw error)
		SimpleGraph<Integer,DefaultEdge> output = Tools.copy(g1);
		for (Integer i : g2.vertexSet()) output.addVertex(i);
		for (DefaultEdge e : g2.edgeSet()) output.addEdge(g2.getEdgeSource(e), g2.getEdgeTarget(e));
		return output;
	}

	public static SimpleGraph<Integer,DefaultEdge> completeJoin(SimpleGraph<Integer,DefaultEdge> g1, SimpleGraph<Integer,DefaultEdge> g2){
		// assumes the vertex set of g1 and g2 are disjoint. If any vertices are identical, they merge via standard node identification (untested - might throw error)
		SimpleGraph<Integer,DefaultEdge> output = Tools.copy(g1);
		for (Integer i : g2.vertexSet()) {
			output.addVertex(i);
			for (Integer j : g1.vertexSet()) {
				output.addEdge(i, j);
			}			
		}
		for (DefaultEdge e : g2.edgeSet()) output.addEdge(g2.getEdgeSource(e), g2.getEdgeTarget(e));
		return output;
	}

}
