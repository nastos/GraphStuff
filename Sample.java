import org.jgrapht.*;
import org.jgrapht.alg.*;
import org.jgrapht.graph.*;
import java.util.List;
public class Sample {
    public static void main(String args[]) {
     
        SimpleGraph<Integer, DefaultEdge>  g = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class); 

        for (int i=1; i<=100; i++) g.addVertex(i);
        for (int i=1; i<=100; i++)
        	for (int j=i+1; j<=100; j++)
        		if (j%i == 0 || i%j == 0) g.addEdge(i, j);

        g.removeVertex(1);
        
        System.out.println("Shortest path from 99 to 100:");
        List shortest_path =   DijkstraShortestPath.findPathBetween(g, 99, 100);
        System.out.println(shortest_path);
        
    }
}