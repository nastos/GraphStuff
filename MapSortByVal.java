import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapSortByVal {

	/*
	 * Taken from http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
	 * Modified version of answer given by:  	
			edited Oct 1 '08 at 21:02
			community wiki
			7 revs
			volley
	 */
	
	
	/**
	 * returns a list of Map Entries sorted by value.
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> getMapSortedByValue(Map<K, V> map) {
		final int size = map.size();
		final List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(size);
		list.addAll(map.entrySet());
		final ValueComparator<V> cmp = new ValueComparator<V>();
		Collections.sort(list, cmp);
		return list;
		/*
		final List<K> keys = new ArrayList<K>(size);
		for (int i = 0; i < size; i++) {
			keys.set(i, list.get(i).getKey());
		}
		return keys;
		*/
	}

	private static final class ValueComparator<V extends Comparable<? super V>>
	implements Comparator<Map.Entry<?, V>> {
		public int compare(Map.Entry<?, V> o1, Map.Entry<?, V> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	}

}