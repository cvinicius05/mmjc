package ufc.ck017.mmjc.translate.activationRegister.util;

import java.util.Iterator;

public class BoolList {
	public boolean head;
	public BoolList tail;

	public BoolList(boolean h, BoolList t) {
		head=h;tail=t;
	}

	public Iterator iterator() {
		class It implements Iterator{

			BoolList l;

			public It(BoolList accessList) {
				l = accessList;
			}

			public boolean hasNext() {
				return tail != null;
			}

			public Object next() {
				Object h = l.head;
				l = l.tail;
				return h;
			}

			public void remove() {
				l = l.tail;
			}

		}
		return new It(this);
	}
}
