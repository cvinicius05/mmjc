package ufc.ck017.mmjc.translate;

import java.util.Iterator;
import java.util.LinkedList;

public class FragList {

	LinkedList<Frag> frags;
	
	public FragList() {
		frags = new LinkedList<Frag>();
	}
	
	public void add(Frag f) {
		frags.add(f);
	}

	public Iterator<Frag> iterator() {
		return frags.iterator();
	}

	public int size() {
		return frags.size();
	}
	
	public Frag get(int i) {
		return frags.get(i);
	}
}
