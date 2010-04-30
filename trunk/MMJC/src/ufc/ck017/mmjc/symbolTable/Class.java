package ufc.ck017.mmjc.symbolTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import ufc.ck017.mmjc.node.AExtNextclass;
import ufc.ck017.mmjc.node.AMethod;
import ufc.ck017.mmjc.node.ANonextNextclass;
import ufc.ck017.mmjc.node.AVar;
import ufc.ck017.mmjc.node.PMethod;
import ufc.ck017.mmjc.node.PNextclass;
import ufc.ck017.mmjc.node.PVar;
import ufc.ck017.mmjc.node.TId;

/**
 * Classe que implementa a estrutura das classes declaradas no
 * c&oacute;digo do programa de entrada, onde s&atilde;o
 * armazenados os m&eacute;todos, vari&aacute;veis locais e
 * classes que extendem a classe declarada e os m&eacute;todos
 * de acesso e manipula&ccedil;&atilde;o aos mesmos.
 * 
 * @author vinicius
 *
 */
public class Class {

	private VarSymbol name = null;
	private VarSymbol parentClass = null;
	private ArrayList<Binding> localVariables = null;
	private ArrayList<Method> methods = null;
	private LinkedList<Class> childrenClasses = null;
	private boolean phantom = false;

	public Class(Class parent, TId cname, int numVars, int numMethods) {
		localVariables = new ArrayList<Binding>(numVars);
		methods = new ArrayList<Method>(numMethods);
		parentClass = (parent == null ? null : parent.getName());
		name = VarSymbol.symbol(cname);
	}
	
	public Class(Class parent, TId cname) {
		phantom = true;
		parentClass = (parent == null ? null : parent.getName());
		name = VarSymbol.symbol(cname);
	}
	
	public Class(PNextclass nextClass) {
		if(nextClass instanceof ANonextNextclass) {

			int numberOfVariables = ((ANonextNextclass) nextClass).getVar().size();
			localVariables = new ArrayList<Binding>(numberOfVariables);

			int numberOfMethods = ((ANonextNextclass) nextClass).getMethod().size();
			methods = new ArrayList<Method>(numberOfMethods);

			name = VarSymbol.symbol(((ANonextNextclass) nextClass).getId());
			parentClass = null;
		}
		else if(nextClass instanceof AExtNextclass){

			int numberOfVariables = ((AExtNextclass) nextClass).getVar().size();
			localVariables = new ArrayList<Binding>(numberOfVariables);

			int numberOfMethods = ((AExtNextclass) nextClass).getMethod().size();
			methods = new ArrayList<Method>(numberOfMethods);

			name = VarSymbol.symbol(((AExtNextclass) nextClass).getName());
			parentClass = VarSymbol.symbol(((AExtNextclass) nextClass).getExt());
		}

		childrenClasses = new LinkedList<Class>();
		setLocalVariables(nextClass);
		setMethods(nextClass);
	}
	
	public void materialize(Class c) {
		if(phantom) {
			this.localVariables = c.localVariables;
			this.methods = c.methods;
			this.childrenClasses = c.childrenClasses;
		}
	}

	/**
	 * Retorna um {@link VarSymbol} que representa o nome do
	 * identificador associado a classe.
	 * 
	 * @return um s&iacute;mbolo para o nome da classe.
	 */
	public VarSymbol getName() {
		return name;
	}

	/**
	 * Retorna um ArrayList de {@link Binding} representando as
	 * vari&aacute;veis locais da classe.
	 * 
	 * @return ArrayList de Binding.
	 */
	public ArrayList<Binding> getLocalVariables() {
		return localVariables;
	}

	/**
	 * Retorna um ArrayList de Binding representando os
	 * m&eacute;todos da classe.
	 * 
	 * @return ArrayList de Binding.
	 */
	public ArrayList<Method> getMethods() {
		return methods;
	}

	/**
	 * Retorna um VarSymbol que representa o nome do
	 * identificador associado a classe pai se esta existe.
	 * 
	 * @return um s&iacute;mbolo para o nome da classe pai.
	 */
	public VarSymbol getParentClass() {
		return parentClass;
	}

	/**
	 * Retorna uma LinkedList de Class representando as
	 * classes que extendem a classe atual.
	 * 
	 * @return LinkedList das classes que extedem a classe
	 * atual.
	 */
	public LinkedList<Class> getExtendedClass() {
		return childrenClasses;
	}

	/**
	 * M&eacute;todo que adiciona uma {@link Class} ao vetor de classes
	 * que extendem &agrave; classe atual.
	 * 
	 * @param c Classe que extende a classe atual.
	 */
	public void setExtendedList(Class c) {
		if(childrenClasses == null) childrenClasses = new LinkedList<Class>();
		childrenClasses.add(c);
	}

	/**
	 * Associa as vari&aacute;veis locais da classe atual.
	 * 
	 * @param nextClass Produ&ccedil;&atilde;o do tipo {@link PNextclass}. 
	 */
	private void setLocalVariables(PNextclass nextClass) {
		Iterator<PVar> iter = null;
		int index;

		if(nextClass instanceof ANonextNextclass)
			iter = ((ANonextNextclass) nextClass).getVar().iterator();
		else if(nextClass instanceof AExtNextclass)
			iter = ((AExtNextclass) nextClass).getVar().iterator();

		PVar localVar = null;
		while(iter.hasNext()) {
			localVar = iter.next();

			VarSymbol v = VarSymbol.symbol(((AVar) localVar).getId());
			TypeSymbol t = TypeSymbol.symbol(((AVar) localVar).getType());
			Binding b = new Binding(v, t);

			index = v.hashCode() % localVariables.size();
			if(index < 0) index = -index;

			while(localVariables.get(index) != null) index = (index+1) % localVariables.size();
			localVariables.add(index, b);
		}
	}

	/**
	 * Associa os m&eacute;todos &agrave; classe atual.
	 * 
	 * @param nextClass Produ&ccedil;&atilde;o do tipo {@link PNextclass}.
	 */
	private void setMethods(PNextclass nextClass) {
		Iterator<PMethod> iter = null;
		int index;

		if(nextClass instanceof ANonextNextclass)
			iter = ((ANonextNextclass) nextClass).getMethod().iterator();
		else if(nextClass instanceof AExtNextclass)
			iter = ((AExtNextclass) nextClass).getMethod().iterator();

		PMethod m = null;
		while(iter.hasNext()){
			m = iter.next();

			Method newMethod = new Method(m, name);
			VarSymbol nameOfMethod = VarSymbol.symbol(((AMethod) m).getId());

			index = nameOfMethod.hashCode() % methods.size();
			if(index < 0) index = -index;

			while(methods.get(index) != null) index = (index+1) % methods.size();
			methods.add(index, newMethod);
		}
	}

	/**
	 * M&eacute;todo que retorna o {@link Binding} cujo s&iacute;mbolo
	 * de nome de vari&aacute;vel &eacute; dado por <code>nameOfvariable</code>.
	 * 
	 * @param nameOfVariable s&iacute;mbolo do nome da vari&aacute;vel
	 * que se procura. 
	 * @return {@link Binding} da vari&aacute;vel encontrada ou null, caso
	 * contr&aacute;rio.
	 */
	public Binding getLocalVariable(VarSymbol nameOfVariable) {
		int index = nameOfVariable.hashCode() % localVariables.size();
		if(index < 0) index = -index;

		if(localVariables.get(index).getVarSymbol().equals(nameOfVariable))
			return localVariables.get(index);

		int marker = index;
		do{
			index = (index+1) % localVariables.size();
		}
		while(!localVariables.get(index).getVarSymbol().equals(nameOfVariable) && index != marker);

		if(index != marker) return localVariables.get(index);
		return null;
	}

	/**
	 * M&eacute;todo que retorna o {@link Method} cujo s&iacute;mbolo
	 * de nome de m&eacute;todo &eacute; dado por <code>nameOfMethod</code>.
	 * 
	 * @param nameOfMethod s&iacute;mbolo do nome do m&eacute;todo
	 * que se procura.
	 * @return {@link Method} encontrado ou null, caso contr&aacute;rio.
	 */
	public Method getMethod(VarSymbol nameOfMethod) {
		int index = nameOfMethod.hashCode() % methods.size();
		if(index < 0) index = -index;

		if(methods.get(index).getName().equals(nameOfMethod))
			return methods.get(index);

		int marker = index;
		do{
			index = (index+1) % methods.size();
		}
		while(!methods.get(index).getName().equals(nameOfMethod) && index != marker);

		if(index != marker) return methods.get(index);
		return null;
	}
}