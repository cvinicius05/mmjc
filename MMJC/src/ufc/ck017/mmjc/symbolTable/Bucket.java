package ufc.ck017.mmjc.symbolTable;

public class Bucket {
	
	private Binding head;
	private VarSymbol var;
	private Bucket next;
	
	public Bucket(VarSymbol var, TypeSymbol type) {
		this.var = var;
		head = new Binding(type);
	}
	
	public VarSymbol getVarSymbol() {
		return var;
	}
	
	public Bucket getNext() {
		return next;
	}
	
	public Binding getHead() {
		return head;
	}
	
	public void setVar(VarSymbol var) {
		this.var = var;
	}
	
	public void setHead(Binding b) {
		head = b;
	}
	
	public void setNext(Bucket next) {
		this.next = next;
	}
}
