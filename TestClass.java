import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphUnion;
import org.jgrapht.graph.SimpleGraph;

public class TestClass {

	public static void main(String[] args) {
		SimpleGraph<Integer,DefaultEdge> g = Generation.cycle(6);
		g = Tools.getComplement(g);
		System.out.println(g);

//		SimpleGraph<Integer,DefaultEdge> h = Generation.path(5,24);
//		System.out.println(h);
//		
//		SimpleGraph<Integer,DefaultEdge> big = Generation.completeJoin(g,h);
//		System.out.println(big);
//
//		System.out.println(Tools.greedyClique(big));
		
		// test list of lists pivotting and stuff

		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(4); a.add(2); a.add(1); a.add(3); a.add(5); a.add(6);

//		ArrayList<Integer> b = new ArrayList<Integer>();
//		b.add(14); b.add(12); b.add(11); b.add(13);
//
//		ArrayList<Integer> c = new ArrayList<Integer>();
//		c.add(24); c.add(22); c.add(21); c.add(23);
		
		ArrayList<List<Integer>> L = new ArrayList<List<Integer>>();
		L.add(a);
//		L.add(b);
//		L.add(c);
		
		System.out.println(Tools.lexBFS(g, 5));
		
	}

	

}
