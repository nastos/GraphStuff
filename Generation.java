import java.util.Random;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Generation{

	public static void main(String[] args) {

	}

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

}
