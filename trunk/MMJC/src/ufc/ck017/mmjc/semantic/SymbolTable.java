package ufc.ck017.mmjc.semantic;

import java.util.List;

import ufc.ck017.mmjc.node.TId;
import ufc.ck017.mmjc.symbolTable.TypeSymbol;

// TODO verificar se todas as versões dos métodos são realmente necessárias.
// ATENCAO COM AS ASSINATURAS QUE MUDARAM... ESPERO QUE TEJAM CERTAS... :(
public interface SymbolTable {
	public boolean isMethod(TypeSymbol obj, TId mname, List<TypeSymbol> params);
	public boolean isMethod(TId mname, List<TypeSymbol> params);
	
	public boolean isClass(TId cname);
	public boolean isClass(TypeSymbol cname);
	
	public boolean isVar(TId id);
	
	public boolean isObject(TId id);
	public boolean isObject(TypeSymbol id);
	
	public boolean isSubclassOf(TId obj, TypeSymbol cname);
	public boolean isSubclassOf(TId obj, TId cname);
	
	
	public TypeSymbol getType(TId id);
	public TypeSymbol getType(TypeSymbol cname, TId id);
	
	
	public void enterScope(TId id);
	public void exitScope();
}
