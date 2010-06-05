package ufc.ck017.mmjc.instructionSelection.assem;

import java.util.Iterator;

public class InstrList {
	public Instr head;
	public InstrList tail;

	public InstrList(Instr h, InstrList t) {
		head=h; tail=t;
	}
	
	public Iterator<Instr> iterator() {
		return new It(this);
	}

	private class It implements Iterator<Instr>{

		InstrList l;

		public It(InstrList instrList) {
			l = instrList;
		}

		public boolean hasNext() {
			return tail != null;
		}

		public Instr next() {
			Instr h = l.head;
			l = l.tail;
			return h;
		}

		public void remove() {
			l = l.tail;
		}
	}
}
