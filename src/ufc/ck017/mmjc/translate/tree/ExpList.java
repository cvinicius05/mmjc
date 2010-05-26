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
		if(args.size() > 1){
			head = args.get(0);
			args.remove(0);
			tail = new ExpList(args);
		}
	}

	public ExpList(ExpList args) {
		// TODO Auto-generated constructor stub
	}

	public void add(int i, CONST const1) {
		// TODO Auto-generated method stub

	}
}



