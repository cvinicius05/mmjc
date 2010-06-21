package ufc.ck017.mmjc.activationRecords.temp;

import java.util.Iterator;
import java.util.LinkedList;

public class TempList implements Iterable<Temp> {

	public Temp head;
	public TempList tail;

	public TempList(Temp h, TempList t) {
		head=h;
		tail=t;
	}

	public TempList(LinkedList<Temp> l) {
		if(l.size() > 1) {
			Iterator<Temp> i = l.descendingIterator();
			
			head = i.next();
			tail = null;
			
			while(i.hasNext()) {
				tail = new TempList(head, tail);
				head = i.next();
			}
		} else {
			head = null;
			tail = null;
		}
	}

	public Iterator<Temp> iterator() {
		return new It(this);
	}

	private class It implements Iterator<Temp>{

		TempList l;

		public It(TempList accessList) {
			l = accessList;
		}

		public boolean hasNext() {
			return l != null;
		}

		public Temp next() {
			Temp h = l.head;
			l = l.tail;
			return h;
		}

		public void remove() {
			l = l.tail;
		}

	}
}
