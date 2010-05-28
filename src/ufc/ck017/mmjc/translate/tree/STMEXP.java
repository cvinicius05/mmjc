package ufc.ck017.mmjc.translate.tree;


public class STMEXP extends Stm {
	public Exp exp; 
	public STMEXP(Exp e) {
		exp=e;
	}
	
	public ExpList kids() {
		return new ExpList(exp,null);
	}
	
	public Stm build(ExpList kids) {
		return new STMEXP(kids.head);
	}
}

