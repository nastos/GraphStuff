import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphUnion;
import org.jgrapht.graph.SimpleGraph;

public class TestClass {

	public static void main(String[] args) {
		SimpleGraph<Integer,DefaultEdge> g = Generation.path(3);
		System.out.println(g);
		SimpleGraph<Integer,DefaultEdge> h = Generation.path(4,4);
		System.out.println(h);
		
		SimpleGraph<Integer,DefaultEdge> big = Generation.completeJoin(g,h);
		System.out.println(big);

		System.out.println(Tools.greedyClique(big));

	}

}
