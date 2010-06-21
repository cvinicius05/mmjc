package ufc.ck017.mmjc.activationRecords.frame;

import java.util.Iterator;

public class AccessList implements Iterable<Access> {
	public Access head;
	public AccessList tail;

	public AccessList(Access h, AccessList t) {
		head=h;tail=t;
	}

	public Iterator<Access> iterator() {
		return new It(this);
	}

	private class It implements Iterator<Access>{

		AccessList l;

		public It(AccessList accessList) {
			l = accessList;
		}

		public boolean hasNext() {
			return l != null;
		}

		public Access next() {
			Access h = l.head;
			l = l.tail;
			return h;
		}

		public void remove() {
			l = l.tail;
		}

	}
}
