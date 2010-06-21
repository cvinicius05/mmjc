package ufc.ck017.mmjc.symbolTable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import ufc.ck017.mmjc.node.TId;
import ufc.ck017.mmjc.semantic.SymbolTableInterface;

/**
 * Classe que implementa os m&eacute;todos de acesso da
 * tabela de s&iacute;mbolos segundo a interface
 * {@link SymbolTableInterface}. 
 * 
 * @author Arthur
 * @author Vinicius
 * @see SymbolTableInterface
 */
public class SymbolTable implements SymbolTableInterface {

	private static Hashtable<TypeSymbol, Class> symboltable = null;
	private static SymbolTable instance = null;
	private static ScopeEntry currentScope = null;

	/**
	 * Construtor privado que possibilita o uso do 
	 * padr&atilde;o singleton, onde recebe-se o
	 * tamanho da tabela de s&iacute;mbolos.
	 * 
	 * @param size tamanho da tabela.
	 */
	private SymbolTable(int size) {
		symboltable = new Hashtable<TypeSymbol, Class>(size);
	}

	/**
	 * Inst&acirc;ncia &uacute;nica da tabela de s&iacute;mbolos
	 * gerada pelo padr&atilde;o singleton.
	 * 
	 * @return a tabela de s&iacute;mbolos.
	 */
	public static SymbolTable getInstance() {
		return instance;
	}

	/**
	 * Cria uma tabela de s&iacute;mbolos com size posi&ccedil;&otilde;es
	 * se esta n&atilde;o foi instanciada. 
	 * 
	 * @param size n&uacute;mero de posi&ccedil;&otilde;es da nova tabela.
	 */
	public static void setSizeIfUndefined(int size) {
		if(instance == null) instance = new SymbolTable(size);
	}

	public void addClass(Class c) {
		symboltable.put(c.getName(), c);
	}

	public Class getClass(TypeSymbol cname) {
		return symboltable.get(cname);
	}

	public int getClassFieldIndex(TypeSymbol c, VarSymbol v) {
		return symboltable.get(c).getFieldIndex(v);
	}

	@Override
	public void enterScope(TId id) {
		if(currentScope == null)
			currentScope = symboltable.get(TypeSymbol.search(id.getText()));
		else
			currentScope = ((Class)currentScope).getMethod(VarSymbol.search(id.getText()));
	}

	@Override
	public void exitScope() {
		if(currentScope != null) currentScope = currentScope.getSuperScope();
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
		return cname != null && isClass(TypeSymbol.search(cname.getText()));
	}

	@Override
	public boolean isClass(TypeSymbol type) {
		if(type == null) return false;

		if(TypeSymbol.getBoolSymbol().equals(type) ||
				TypeSymbol.getIntTSymbol().equals(type) ||
				TypeSymbol.getIntVSymbol().equals(type) ||
				symboltable.get(type) == null)
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
		int index = 0;

		if(m == null || m.getParameters().size() != params.size()) return false;
		ArrayList<Binding> actualparams = m.getParameters();

		for(TypeSymbol ts : params) {
			TypeSymbol type = actualparams.get(index).getTypeSymbol();
			if(ts == null || ((isClass(type) && isClass(ts) && !isSubclassOf(type, ts)) && !ts.equals(type)))
				return false;
			index++;
		}

		return true;
	}

	private Method getMethod(TypeSymbol ctype, TId mid) {
		Method m;
		Class c = symboltable.get(ctype);
		VarSymbol msymbol = VarSymbol.search(mid.getText());

		if(msymbol == null || c.isPhantom()) return null;
		while(c != null) {
			if((m = c.getMethod(msymbol)) != null)
				return m;

			c = c.getParent();
		}

		return null;
	}

	private TypeSymbol getVar(TId id) {
		if(currentScope == null) return null;

		VarSymbol var = VarSymbol.search(id.getText());

		TypeSymbol type = null;
		Method m;
		Class c;

		if(currentScope instanceof Method) {
			m = (Method)currentScope;

			if(var == null) return null;
			if((type = m.getLocalVariables().get(var)) != null)
				return type;

			for(Binding b : m.getParameters()) {
				if(b.getVarSymbol().equals(var)) return b.getTypeSymbol();
			}

			c = currentScope.getParent();
		} else c = (Class) currentScope;

		while(c != null) {
			if((type = c.getLocalVariables().get(var)) != null) return type;
			c = c.getParent();
		}

		return null;
	}

}