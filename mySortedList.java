import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mySortedList<V> {

	private ArrayList<Map.Entry<V,Integer>> list;
	private HashMap<V,Integer> map;
	
	
	public mySortedList() {
		list = new ArrayList<Map.Entry<V,Integer>>();
		map = new HashMap<V,Integer>();
	}
	
	public Map.Entry<V, Integer> pop() {
		map.remove(list.get(0).getKey());
		return list.remove(0);
	}
	
	public void add(Map.Entry<V, Integer> m){
		int i=0;
		map.put(m.getKey(), m.getValue());
		while (i < list.size() && list.get(i).getValue() > m.getValue()) i++;
		// at this point, i points to the first entry whose value is equal to or less than the new item
		// new item goes here.
		list.add(i, m);
	}

	public void add(V v, Integer num){
		HashMap<V,Integer> m = new HashMap<V,Integer>();
		m.put(v, num);
		map.put(v, num);
		
		int i=0;
		while (i < list.size() && list.get(i).getValue() > m.get(v)) i++;
		// at this point, i points to the first entry whose value is equal to or less than the new item
		// new item goes here.
		if (i==list.size()) list.add(m.entrySet().iterator().next());
		else list.add(i, m.entrySet().iterator().next());
	}
	
	public void increment(V v) {
		HashMap<V,Integer> m = new HashMap<V,Integer>();
		int value = map.get(v);

		m.put(v, value); 
		list.remove(m.entrySet().iterator().next());

		map.put(v, value+1);
		add(v,value+1);
	}
	
	public String toString() {
		return list.toString();
	}

	public boolean contains(V u) {
		//System.out.println(u+" " +map.containsKey(u));
		return map.containsKey(u);
	}
	
}
