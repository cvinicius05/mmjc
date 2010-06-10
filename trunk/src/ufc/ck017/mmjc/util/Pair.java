package ufc.ck017.mmjc.util;

public class Pair<T> {
	private T f, s;
	
	public Pair(T first, T second) {
		f = first;
		s = second;
	}
	
	public T getFirst() {
		return f;
	}
	
	public T getSecond() {
		return s;
	}
}
