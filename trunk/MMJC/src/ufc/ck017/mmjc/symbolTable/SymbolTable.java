package ufc.ck017.mmjc.symbolTable;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ufc.ck017.mmjc.node.TId;
import ufc.ck017.mmjc.semantic.SymbolTableInterface;

/**
 * Classe que implementa a gera&ccedil;&atilde;o da Tabela de
 * S&iacute;mbolos, a partir de uma Tabela de S&iacute;mbolos
 * Abstrata, como em {@link ConcreteSymbolList}, para a fase
 * de an&aacute;lise sem&acirc;ntica e seus m&eacute;todos de
 * acesso segundo a interface {@link SymbolTableInterface}. 
 * 
 * @author vinicius
 * @see ConcreteSymbolList
 * @see SymbolTableInterface
 */
public class SymbolTable implements SymbolTableInterface {

	private static Hashtable<TypeSymbol, Class> symboltable = null;
	private static SymbolTable instance = null;
	private static ScopeEntry currentScope = null;

	/**
	 * Construtor que recebe uma lista de Class representada
	 * por uma {@link ConcreteSymbolList}.
	 * 
	 * &Eacute; gerada a Tabela de S&iacute;mbolos como um
	 * ArrayList de Class para otimizar as buscas na tabela.
	 * &Eacute; inicializada tamb&eacute;m uma pilha de escopos
	 * para indicar a qual classe ou m&eacute;todo est&aacute;
	 * sendo feita a an&aacute;lise neste momento.  
	 * 
	 * @param list Uma {@link LinkedList} de Class gerada pela
	 * {@link ConcreteSymbolList}.
	 */
	private SymbolTable(int size) {
		symboltable = new Hashtable<TypeSymbol, Class>(size);
	}
	
	public static SymbolTable getInstance() {
		return instance;
	}
	
	public static void setSizeIfUndefined(int size) {
		if(instance == null) instance =  new SymbolTable(size);
	}
	
	public void addClass(Class c) {
		symboltable.put(c.getName(), c);
	}
	
	public Class getClass(TypeSymbol cname) {
		return symboltable.get(cname);
	}
	
	@Override
	public void enterScope(TId id) {
		VarSymbol nameOfId = VarSymbol.symbolFromString(id.getText());
		
		if(currentScope == null)
			currentScope = symboltable.get(nameOfId);
		else
			currentScope = ((Class)currentScope).getMethod(nameOfId);
	}

	@Override
	public void exitScope() {
		if(currentScope != null) currentScope.getParent();
	}

	@Override
	public TypeSymbol getType(TId id) {
		return getVar(id);
	}

	@Override
	public TypeSymbol getType(TypeSymbol cname, TId id) {
		return getMethod(cname, id).getTypeOfReturn();
	}

	@Override
	public boolean isObject(TId id) {
		return isClass(getVar(id));
	}

	@Override
	public boolean isVar(TId id) {
		return getVar(id) != null;
	}

	@Override
	public boolean isClass(TId cname) {
		return isClass(TypeSymbol.getFromString(cname.getText()));
	}

	@Override
	public boolean isClass(TypeSymbol type) {
		if(type == null) return false;
		
		if(TypeSymbol.getBoolSymbol().equals(type) ||
				TypeSymbol.getIntTSymbol().equals(type) ||
				TypeSymbol.getIntVSymbol().equals(type))
			return false;
		
		return true;
	}

	@Override
	public boolean isSubclassOf(TypeSymbol supcname, TypeSymbol subcname) {
		if(!isClass(supcname) || !isClass(subcname)) return false;
		
		Class sub = symboltable.get(subcname);
		
		while(sub != null) {
			if(sub.getName().equals(supcname)) return true;
			sub = sub.getParent();
		}
		
		return false;
	}

	@Override
	public boolean isMethod(TypeSymbol obj, TId mname, List<TypeSymbol> params) {
		Method m = getMethod(obj, mname);
		
		if(m == null || m.getParameters().size() != params.size()) return false;
		
		Iterator<Binding> actualparams = m.getParameters().iterator();
		Iterator<TypeSymbol> searchparams = params.iterator();
		
		while(actualparams.hasNext()) {
			TypeSymbol type = actualparams.next().getTypeSymbol();
			if(!type.equals(searchparams.next())) return false;
		}
		
		return true;
	}
	
	private Method getMethod(TypeSymbol ctype, TId mid) {
		Method m;
		Class c = symboltable.get(ctype);
		VarSymbol msymbol = VarSymbol.symbolFromString(mid.getText());
		
		while(c != null) {
			if((m = c.getMethod(msymbol)) != null)
				return m;
			
			c = c.getParent();
		}
		
		return null;
	}
	
	private TypeSymbol getVar(TId id) {
		if(currentScope == null) return null;
		
		VarSymbol var = VarSymbol.symbolFromString(id.getText());
		TypeSymbol type = null;
		Method m;
		Class c;
		
		if(currentScope instanceof Method) {
			m = (Method)currentScope;
			if((type = m.getLocalVariables().get(var)) != null)
				return type;
			
			for(Binding b : m.getParameters()) {
				if(b.getVarSymbol().equals(var)) return b.getTypeSymbol();
			}
			
			c = currentScope.getParent();
		} else c = (Class) currentScope;
				
		while(c != null) {
			if((type = c.getLocalVariables().get(var)) != null) return type;
			c = currentScope.getParent();
		}
		
		return null;
	}

}