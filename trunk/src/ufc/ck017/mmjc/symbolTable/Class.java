package ufc.ck017.mmjc.symbolTable;

import java.util.Hashtable;
import java.util.LinkedList;

import ufc.ck017.mmjc.node.AVar;
import ufc.ck017.mmjc.node.PNextclass;
import ufc.ck017.mmjc.node.TId;

/**
 * Classe que implementa a estrutura das classes declaradas no
 * c&oacute;digo do programa de entrada, onde s&atilde;o
 * armazenados os m&eacute;todos, vari&aacute;veis locais e
 * classes que extendem a classe declarada e os m&eacute;todos
 * de acesso e manipula&ccedil;&atilde;o aos mesmos.
 *
 * @author Arthur Rodrigues
 * @author Carlos Vinicius
 *
 */
public class Class extends ScopeEntry {

	private TypeSymbol name = null;
	private Class parent = null;
	private Hashtable<VarSymbol, TypeSymbol> localVariables = null;
	private Hashtable<VarSymbol, Integer> fieldsIndex = null;
	private Hashtable<VarSymbol, Method> methods = null;
	private LinkedList<Class> tempChildrenList = null;
	private int fields = 0;
	private boolean phantom = false;

	public Class(Class pclass, TId cname, int numVars, int numMethods) {
		localVariables = new Hashtable<VarSymbol, TypeSymbol>(numVars);
		fieldsIndex = new Hashtable<VarSymbol, Integer>(numVars);
		methods = new Hashtable<VarSymbol, Method>(numMethods);
		parent = pclass;
		name = TypeSymbol.symbol(cname.getText());
	}

	public Class(Class pclass, TypeSymbol cname) {
		phantom = true;
		name = cname;
		parent = pclass;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Class) && name.equals(((Class)obj).name);
	}

	public void materialize(Class c) {
		if(phantom) {
			this.localVariables = c.localVariables;
			this.fieldsIndex = c.fieldsIndex;
			this.methods = c.methods;
			this.parent = c.parent;
			phantom = false;
		}
	}

	/**
	 * Retorna um {@link VarSymbol} que representa o nome do
	 * identificador associado a classe.
	 *
	 * @return um s&iacute;mbolo para o nome da classe.
	 */
	public TypeSymbol getName() {
		return name;
	}

	/**
	 * Retorna um ArrayList de {@link Binding} representando as
	 * vari&aacute;veis locais da classe.
	 *
	 * @return ArrayList de Binding.
	 */
	public Hashtable<VarSymbol, TypeSymbol> getLocalVariables() {
		return localVariables;
	}

	/**
	 * Retorna um ArrayList de Binding representando os
	 * m&eacute;todos da classe.
	 *
	 * @return ArrayList de Binding.
	 */
	public Hashtable<VarSymbol, Method> getMethods() {
		return methods;
	}

	/**
	 * Retorna um VarSymbol que representa o nome do
	 * identificador associado a classe pai se esta existe.
	 *
	 * @return um s&iacute;mbolo para o nome da classe pai.
	 */
	public Class getParent() {
		return parent;
	}

	/**
	 * Retorna uma LinkedList de Class representando as
	 * classes que extendem a classe atual.
	 *
	 * @return LinkedList das classes que extedem a classe
	 * atual.
	 */
	public LinkedList<Class> getChildrenClasses() {
		return tempChildrenList;
	}

	/**
	 * M&eacute;todo que adiciona uma {@link Class} ao vetor de classes
	 * que extendem &agrave; classe atual.
	 *
	 * @param c Classe que extende a classe atual.
	 */
	public boolean setChildrenClass(Class c) {
		if(tempChildrenList == null)
			tempChildrenList = new LinkedList<Class>();
		else if(tempChildrenList.contains(c))
			return false;

		tempChildrenList.add(c);                
		return true;
	}

	/**
	 * Associa as vari&aacute;veis locais da classe atual.
	 *
	 * @param nextClass Produ&ccedil;&atilde;o do tipo {@link PNextclass}.
	 */    
	public boolean addLocalVar(AVar var) {
		VarSymbol v = VarSymbol.symbol(var.getId());

		if(localVariables.get(v) != null) return false;

		localVariables.put(v, TypeSymbol.symbol(var.getType()));
		fieldsIndex.put(v, new Integer(fields++));
		return true;
	}

	public int getFieldIndex(VarSymbol var) {
		Integer index = fieldsIndex.get(var);
		return (index == null ? -1 : index.intValue());
	}

	/**
	 * Associa os m&eacute;todos &agrave; classe atual.
	 *
	 * @param nextClass Produ&ccedil;&atilde;o do tipo {@link PNextclass}.
	 */
	public boolean addMethod(Method m) {
		if(methods.get(m.getName()) != null) return false;

		methods.put(m.getName(), m);
		return true;
	}

	/**
	 * M&eacute;todo que retorna o {@link Method} cujo s&iacute;mbolo
	 * de nome de m&eacute;todo &eacute; dado por <code>nameOfMethod</code>.
	 *
	 * @param mname s&iacute;mbolo do nome do m&eacute;todo
	 * que se procura.
	 * @return {@link Method} encontrado ou null, caso contr&aacute;rio.
	 */
	public Method getMethod(VarSymbol mname) {
		return methods.get(mname);
	}

	@Override
	public ScopeEntry getSuperScope() {
		return null;
	}

}
