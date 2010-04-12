package ufc.ck017.mmjc.symbolTable;

public class Binding {

	private TypeSymbol type;
	private Binding next;
	
	public Binding(TypeSymbol type) {
		this.type = type;
		next = null;
	}
	
	public void setType(TypeSymbol type) {
		this.type = type;
	}
	
	public TypeSymbol getTypeSymbol() {
		return type;
	}
	
	public Binding getNext(){
		return next;
	}
	
	public void setNext(Binding b){
		next = b;
	}
}
