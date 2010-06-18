package ufc.ck017.mmjc.translate.tree;

import java.util.Iterator;
import java.util.List;

public class ExpList implements Iterable<Exp> {

	public Exp head;
	public ExpList tail;

	public ExpList(Exp exp, ExpList t) {
		head=exp; 
		tail=t;
	}

	public ExpList(List<Exp> args) {
		if(!args.isEmpty()) {
			head = args.remove(0);
			tail = (args.size() > 0 ? new ExpList(args) : null);
		}
	}

	public Iterator<Exp> iterator() {
		return new It(this);
	}

	private class It implements Iterator<Exp>{

		ExpList l;

		public It(ExpList accessList) {
			l = accessList;
		}

		public boolean hasNext() {
			return tail != null;
		}

		public Exp next() {
			Exp h = l.head;
			l = l.tail;
			return h;
		}

		public void remove() {
			l = l.tail;
		}

	}
}
