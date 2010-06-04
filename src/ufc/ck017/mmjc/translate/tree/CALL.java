package ufc.ck017.mmjc.translate.tree;

public class CALL extends Exp {

	public Exp func;
	public ExpList args;

	public CALL(Exp f, ExpList a) {
		func=f;
		args=a;
	}

	public CALL(LABEL label, ExpList a) {
		
	}

	public ExpList kids() {
		return new ExpList(func, args);
	}

	public Exp build(ExpList kids) {
		return new CALL(kids.head, kids.tail);
	}
}
