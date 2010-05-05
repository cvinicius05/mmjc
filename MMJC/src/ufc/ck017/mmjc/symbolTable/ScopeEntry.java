package ufc.ck017.mmjc.symbolTable;

public abstract class ScopeEntry {
	public abstract Class getParent();
	public abstract ScopeEntry getSuperScope();
}
