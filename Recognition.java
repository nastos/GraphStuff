import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

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
        System.out.println(Tools.getComplement(g));
        System.out.println(isWeaklyChordal(g));
        Tools.degSeq(g);
        
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
			V v = Tools.getSimplicial(g2);
			if (v==null) return false;
			g2.removeVertex(v);
			System.out.println(g2);
		}
		return true;		
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
		if (2*M +5 < allPairs) g2=Tools.getComplement(g);
		else g2 = Tools.copy(g);
		ArrayList<V> vertices = new ArrayList<V>(g2.vertexSet());
		boolean isWC = true;
		M = g2.edgeSet().size();
		while (isWC==true) {
			isWC=false;
			for (int i=0; i<N; i++) {
				for (int j=i+1; j<N; j++) {
					if (Tools.isTwoPair(g2,vertices.get(i),vertices.get(j))) {
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
	
	
	/**
	 * Inefficiently determines if the graph is split
	 * @param g
	 * @return true if G us a split graph
	 */
	
	public static <V,E> boolean isSplit(SimpleGraph<V,E> g) {
		if (isChordal(g) && isChordal(Tools.getComplement(g))) return true;
		return false;
	}
	
	/**
	 * Usage: isP4(g,a,b,c,d). Checks if a,b,c,d (in any order) induces a P4. 
	 * @param g
	 * @param v
	 * @return
	 */
	public static <V,E> boolean isP4(SimpleGraph<V,E> g, V... v) {
		if (v.length != 4) {
			System.err.println("isP4 requires 4 nodes. Found " + v);
			return false;
		}
		int[] deg = new int[4]; // degree-sequence of this 4-vertex induced subgraph
		int edgecount = 0;
		
		for (int i=0; i<3; i++) {
			for (int j=i+1; j<=3; j++) {
				if (g.containsEdge(v[i], v[j])) {
					deg[i]++;
					deg[j]++;
					edgecount++;
				}
			}
		}
		if (edgecount != 3) return false;
		// have 3 edges. Make sure they aren't a K3+v 
		for (int i=0; i<4; i++) {
			if (deg[i] == 0) return false;
		}
		return true;
	}

	/**
	 * Usage: isOrderedP4(g,a,b,c,d). Checks if a-b-c-d is a P4 with endpoints a and d. 
	 * @param g
	 * @param v
	 * @return
	 */
	public static <V,E> boolean isOrderedP4(SimpleGraph<V,E> g, V... v) {
		if (v.length != 4) {
			System.err.println("isOrderedP4 requires 4 nodes. Found " + v);
			return false;
		}
		if (g.containsEdge(v[0], v[2])) return false;
		if (g.containsEdge(v[0], v[3])) return false;
		if (g.containsEdge(v[1], v[3])) return false;
		if (g.containsEdge(v[0], v[1]) == false) return false;
		if (g.containsEdge(v[1], v[2]) == false) return false;
		if (g.containsEdge(v[2], v[3]) == false) return false;
		return true;
	}
	
	/**
	 * Inefficiently checks if g is a cograph. Tests all 4-sets for being a P4.
	 * @param g
	 * @return
	 */
	public static <V,E> boolean isCograph(SimpleGraph<V,E> g) {
		ArrayList<V> vertices = new ArrayList<V>(g.vertexSet());
		int n = vertices.size();
		for (int i=0; i<n; i++) {
			for (int j=i+1; j<n; j++) {
				for (int k=j+1; k<n; k++) {
					for (int m=k+1; m<n; m++) {
						if (isP4(g,vertices.get(i),vertices.get(j),vertices.get(k),vertices.get(m))) return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Extremely inefficiently decides if g is trivially perfect
	 * @param g
	 * @return
	 */
	public static <V,E> boolean isTriviallyPerfect(SimpleGraph<V,E> g){
		// (P4,C4)-free . AKA chordal cographs.
		if (isCograph(g) && isChordal(g)) return true;
		return false;
	}

	/**
	 * Extremely inefficiently decides if g is a threshold graph
	 * @param g
	 * @return
	 */
	public static <V,E> boolean isThreshold(SimpleGraph<V,E> g){
		// (P4,C4,2K2)-free . AKA trivially perfect and co-triv perfect. AKA split cograph.
		if (isCograph(g) && isSplit(g)) return true;
		return false;
	}

	/**
	 * Extremely inefficiently decides if g is a p4-sparse graph
	 * @param g
	 * @return
	 */
	public static <V,E> boolean isP4Sparse(SimpleGraph<V,E> g){
		// Def: Every set of 5 vertices induces at most one P4
		ArrayList<V> v = new ArrayList<V>(g.vertexSet());
		int n = v.size();
		for (int i=0; i<n; i++) {
			for (int j=i+1; j<n; j++) {
				for (int k=j+1; k<n; k++) {
					for (int m=k+1; m<n; m++) {
						int count = 0;
						if (isP4(g,v.get(i),v.get(j),v.get(k),v.get(m))) count ++;
						for (int p=m+1; p<n; p++) {
							if (isP4(g,v.get(i),v.get(j),v.get(k),v.get(p))) count++;
							if (count >= 2) {
								System.err.println(g + " is not p4-sparse because " + v.get(i) +" "+v.get(j)+" "+v.get(k)+" "+v.get(m)+" "+v.get(p) + " induces many P4s");
								return false;
							}
							if (isP4(g,v.get(i),v.get(j),v.get(p),v.get(m))) count++;
							if (count >= 2) {
								System.err.println(g + " is not p4-sparse because " + v.get(i) +" "+v.get(j)+" "+v.get(k)+" "+v.get(m)+" "+v.get(p) + " induces many P4s");
								return false;
							}
							if (isP4(g,v.get(i),v.get(p),v.get(k),v.get(m))) count++;
							if (count >= 2) {
								System.err.println(g + " is not p4-sparse because " + v.get(i) +" "+v.get(j)+" "+v.get(k)+" "+v.get(m)+" "+v.get(p) + " induces many P4s");
								return false;
							}
							if (isP4(g,v.get(p),v.get(j),v.get(k),v.get(m))) count++;
							if (count >= 2) {
								System.err.println(g + " is not p4-sparse because " + v.get(i) +" "+v.get(j)+" "+v.get(k)+" "+v.get(m)+" "+v.get(p) + " induces many P4s");
								return false;
							}
							
							
						}
					}
				}
			}
		}
		return true;
	}

}
