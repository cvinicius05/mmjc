package ufc.ck017.mmjc.activationRecords.util;

import java.util.Iterator;

public class BoolList implements Iterable<Boolean>{
	public boolean head;
	public BoolList tail;

	public BoolList(boolean h, BoolList t) {
		head=h;tail=t;
	}

	public Iterator<Boolean> iterator() {
		return new It(this);
	}
	
	private class It implements Iterator<Boolean> {

		BoolList l;

		public It(BoolList accessList) {
			l = accessList;
		}

		public boolean hasNext() {
			return tail != null;
		}

		public Boolean next() {
			Boolean h = l.head;
			l = l.tail;
			return h;
		}

		public void remove() {
			l = l.tail;
		}

	}
}
