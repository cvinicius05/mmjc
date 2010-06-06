package ufc.ck017.mmjc.instructionSelection.assem;

import java.util.Iterator;
import java.util.LinkedList;

public class InstrList implements Iterable<Instr>{
	public Instr head;
	public InstrList tail;

	public InstrList(Instr h, InstrList t) {
		head=h; tail=t;
	}
	
	public InstrList(LinkedList<Instr> l) {
		if(l.size() > 1) {
			Iterator<Instr> i = l.descendingIterator();
			
			head = i.next();
			tail = null;
			
			while(i.hasNext()) {
				tail = new InstrList(head, tail);
				head = i.next();
			}
		} else {
			head = null;
			tail = null;
		}
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
