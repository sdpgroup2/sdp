package sdp.group2.util;


public class Tuple<K, V> {

	private K k;
	private V v;
	
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
	
	
	
}
