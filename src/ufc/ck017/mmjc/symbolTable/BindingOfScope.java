package ufc.ck017.mmjc.symbolTable;

public class BindingOfScope {

	private VarSymbol scope = null;
	private int type = 0;
	
	public BindingOfScope(VarSymbol scope, int type) {
		this.setScope(scope);
		this.setType(type);
	}

	public void setScope(VarSymbol scope) {
		this.scope = scope;
	}

	public VarSymbol getScope() {
		return scope;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}