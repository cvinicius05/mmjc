package ufc.ck017.mmjc.semantic;

import java.util.List;

import ufc.ck017.mmjc.node.PType;
import ufc.ck017.mmjc.node.TId;
import ufc.ck017.mmjc.util.SemanticType;

public interface SymbolTable {
	public boolean isMethod(TId obj, TId mname, List<PType> params);
	public boolean isMethod(TId mname, List<PType> params);
	public boolean isClass(TId cname);
	public boolean isVar(TId id);
	public boolean isSubclassOf(TId obj, TId cname);
	
	public SemanticType getType(TId id);
	
	public void enterScope(TId id);
	public void exitScope();
}
