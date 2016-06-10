import java.util.ArrayList;

public class myNode<V extends Comparable> {
	V v;
	ArrayList<V> label;
	
	public myNode(V vertex) {
		this.v = vertex;
		label = new ArrayList<V>();
	}
}
