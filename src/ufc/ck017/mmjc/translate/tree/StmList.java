package ufc.ck017.mmjc.translate.tree;

import java.util.Iterator;

public class StmList implements Iterable<Stm> {

	public Stm head;
	public StmList tail;

	public StmList(Stm h, StmList t) {
		head=h; tail=t;
	}

	public Iterator<Stm> iterator() {
		return new It(this);
	}

	public void add(int i, Stm stm) {
	
	}

	public void add(Stm stm) {
	
	}

	private class It implements Iterator<Stm> {

		StmList l;

		public It(StmList accessList) {
			l = accessList;
		}

		public boolean hasNext() {
			return l != null;
		}

		public Stm next() {
			Stm h = l.head;
			l = l.tail;
			return h;
		}

		public void remove() {
			l = l.tail;
		}

	}
}
