package ufc.ck017.mmjc.activationRecords.temp;

import java.util.Iterator;

public class LabelList implements Iterable<Label> {
	
	public Label head;
	public LabelList tail;

	public LabelList(Label h, LabelList t) {
		head=h;
		tail=t;
	}
	
	public Iterator<Label> iterator() {
		return new It(this);
	}
	
	private class It implements Iterator<Label> {

		LabelList l;

		public It(LabelList accessList) {
			l = accessList;
		}

		public boolean hasNext() {
			return l != null;
		}

		public Label next() {
			Label h = l.head;
			l = l.tail;
			return h;
		}

		public void remove() {
			l = l.tail;
		}

	}
}
