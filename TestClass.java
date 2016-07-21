import java.util.ArrayList;
import java.util.List;

import org.jgraph.JGraph;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.alg.ChromaticNumber;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphUnion;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.SimpleGraph;

public class TestClass {

	public static void main(String[] args) {
		//SimpleGraph<Integer,DefaultEdge> g = Generation.cycle(500);
		//g = Tools.getComplement(g);
		//System.out.println(g);

		//SimpleGraph<Integer,DefaultEdge> h = Generation.path(500,501);
		//System.out.println(h);
		
		//SimpleGraph<Integer,DefaultEdge> big = Generation.completeJoin(g,h);
		//System.out.println(big);

		
		//System.out.println(Tools.greedyClique(big));
		
		// test list of lists pivotting and stuff

		//System.out.println(Tools.MCS(big, 5));

		//System.out.println(System.currentTimeMillis());
		
		//long start = System.currentTimeMillis();

//		System.out.println(Tools.greedyColoring(big));
//		System.out.println(System.currentTimeMillis()-start);
//		
//		start = System.currentTimeMillis();
//		System.out.println(ChromaticNumber.findGreedyChromaticNumber(big));
//		System.out.println(System.currentTimeMillis()-start);
//		
//		start = System.currentTimeMillis();
//		System.out.println(ChromaticNumber.findGreedyColoredGroups(big));
//		System.out.println(System.currentTimeMillis()-start);


		//System.out.println(System.currentTimeMillis());

	}

	

}
