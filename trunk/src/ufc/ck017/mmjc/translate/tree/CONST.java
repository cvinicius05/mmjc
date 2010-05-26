package ufc.ck017.mmjc.translate.tree;

public class CONST extends Exp {
	public int value;
	
	public CONST(int v) {
		value=v;
	}
	
	public ExpList kids() {
		return null;
	}
	
	public Exp build(ExpList kids) {
		return this;
	}
}

