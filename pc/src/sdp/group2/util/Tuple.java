package sdp.group2.util;


public class Tuple<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<Tuple<K, V>> {

	private final K k;
	private final V v;
	
	public Tuple(K k, V v) {
		this.k = k;
		this.v = v;
	}
	
	public K getFirst() {
		return k;
	}
	
	public V getSecond() {
		return v;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)",
				k != null ? k.toString() : "null",
				v != null ? v.toString() : "null");
	}

	@Override
	public int compareTo(Tuple<K, V> o) {
		int firstCompare = getFirst().compareTo(o.getFirst());
		if (firstCompare == 0) {
			return getSecond().compareTo(o.getSecond());
		}
		return firstCompare;
	}
	
}
