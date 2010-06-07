package ufc.ck017.mmjc.translate.tree;

import java.util.List;

public class ExpList {

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
}
