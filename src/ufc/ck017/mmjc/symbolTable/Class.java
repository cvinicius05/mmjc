package ufc.ck017.mmjc.symbolTable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import ufc.ck017.mmjc.node.AVar;
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
	private Hashtable<VarSymbol, Method> methods = null;
	private LinkedList<Class> tempChildrenList = null;
	private boolean phantom = false;

	/**
	 * Construtor padr&atilde;o onde s&atilde;o alocados
	 * o nome, a classe pai, o vetor de vari&aacute;veis
	 * locais e os m&eacute;todos da classe.
	 * 
	 * @param pclass classe pai
	 * @param cname nome da classe
	 * @param numVars n&uacute;mero de vari&aacute;veis
	 * @param numMethods n&uacute;mero de m&eacute;todos.
	 */
	public Class(Class pclass, TId cname, int numVars, int numMethods) {
		parent = pclass;
		name = TypeSymbol.symbol(cname.getText());
		localVariables = new Hashtable<VarSymbol, TypeSymbol>(numVars);
		methods = new Hashtable<VarSymbol, Method>(numMethods);
	}

	/**
	 * Construtor de classes que n&atilde;o foram declaradas,
	 * chamadas classes fantasmas, e que s&atilde;o estendidas
	 * por alguma outra classe j&aacute; declarada.
	 * 
	 * @param pclass classe pai
	 * @param cname nome da classe.
	 */
	public Class(Class pclass, TypeSymbol cname) {
		phantom = true;
		parent = pclass;
		name = cname;
	}

	/**
	 * Transforma uma classe fantasma em uma classe com todos
	 * os seus par&aacute;metros.
	 * 
	 * @param c classe fantasma a ser materializada.
	 */
	public void materialize(Class c) {
		if(phantom) {
			this.localVariables = c.localVariables;
			this.methods = c.methods;
			this.parent = c.parent;
			phantom = false;
		}
	}

	/**
	 * Retorna um {@link TypeSymbol} que representa o nome do
	 * identificador associado a classe.
	 * 
	 * @return um s&iacute;mbolo para o nome da classe.
	 */
	public TypeSymbol getName() {
		return name;
	}

	/**
	 * Retorna um {@link Hashtable} de pares de elementos {@link VarSymbol}
	 * e {@link TypeSymbol} representando as vari&aacute;veis locais
	 * da classe.
	 * 
	 * @return {@link Hashtable} de vari&aacute;veis locais.
	 */
	public Hashtable<VarSymbol, TypeSymbol> getLocalVariables() {
		return localVariables;
	}

	/**
	 * Retorna um {@link Hashtable} de pares de elementos {@link VarSymbol}
	 * e {@link Method} representando os m&eacute;todos da classe.
	 * 
	 * @return {@link Hashtable} de m&eacute;todos da classe.
	 */
	public Hashtable<VarSymbol, Method> getMethods() {
		return methods;
	}

	/**
	 * Retorna a {@link Class} que representa a classe pai,
	 * se esta existe.
	 * 
	 * @return classe pai.
	 */
	public Class getParent() {
		return parent;
	}

	/**
	 * Retorna uma {@link LinkedList} de Class representando as
	 * classes que extendem a classe atual.
	 * 
	 * @return {@link LinkedList} das classes que extedem a classe.
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
	 * Adiciona uma nova vari&aacute;vel local.
	 * 
	 * @param var n&oacute; de vari&aacute;vel.
	 * @return true se a vari&aacute;vel ainda n&atilde;o
	 * existe na classe e false caso contr&aacute;rio.
	 */	
	public boolean addLocalVar(AVar var) {
		VarSymbol v = VarSymbol.symbol(var.getId());

		if(localVariables.get(v) != null) return false;

		localVariables.put(v, TypeSymbol.symbol(var.getType()));
		return true;
	}

	/**
	 * Adiciona um novo m&eacute;todo a classe. Se o m&eacute;todo
	 * j&aacute; foi declarado em alguma outra classe, &eacute;
	 * retornado false. Primeiro &eacute; feita a verifica&ccedil;&atilde;o
	 * das subclasses da classe atual. Se n&atilde;o for encontrada 
	 * nenhuma ocorr&ecirc;ncia do mesmo m&eacute;todo; &eacute; feita
	 * a busca pelas classes ao qual a atual pertence como subclasse.
	 * 
	 * @param m n&oacute; de {@link Method}.
	 * @return true se o m&eacute;todo ainda n&atilde;o foi
	 * declarado no escopo da classe e false caso exista outra
	 * classe com o mesmo m&eacute;todo j&aacute; declarado.
	 */
	public boolean addMethod(Method m) {
		Method method = methods.get(m.getName());

		if(method != null && m.getTypeOfReturn().equals(method.getTypeOfReturn()) && sameParameters(m, method)) {
			return false;
		}

		if(tempChildrenList != null) {
			for(Class c : tempChildrenList){
				if(existMethod(c, m)) {
					methods.put(m.getName(), m);
					return false;
				}
			}
		}

		Class pclass = this;
		method = methods.get(m.getName());

		while(pclass != null) {
			if(method != null && m.getTypeOfReturn().equals(method.getTypeOfReturn()) && sameParameters(m, method)) {
				methods.put(m.getName(), m);
				return false;
			}

			pclass = pclass.getParent();
			if(pclass != null && pclass.getMethods() != null)
				method = pclass.getMethods().get(m.getName());
		}

		methods.put(m.getName(), m);
		return true;
	}

	/**
	 * Verifica se o m&eacute;todo <code>m</code> pertence a
	 * uma subclasse de <code>m</code>.
	 * 
	 * @param c classe a ser feita a busca
	 * @param m m&eacute;todo procurado
	 * @return true se <code>c</code> possui o m&eacute;todo
	 * em alguma subclasse e false caso contr&aacute;rio.
	 */
	private boolean existMethod(Class c, Method m) {
		Method method = c.getMethod(m.getName());

		if(method != null && m.getTypeOfReturn().equals(method.getTypeOfReturn()) && sameParameters(m, method)) {
			return true;
		}

		if(c.getChildrenClasses() != null) {
			for(Class nextChildren : c.getChildrenClasses()){
				if(existMethod(nextChildren, m)) return true;
			}
		}
		return false;
	}

	/**
	 * Verifica se os par&acirc;emtros dos m&eacute;todos
	 * <code>m1</code> e <code>m2</code> s&atilde;o os mesmos.
	 * 
	 * @param m1 primeiro m&eacute;todo
	 * @param m2 segundo m&eacute;todo
	 * @return true se possuem os mesmos par&acirc;emtros e false
	 * caso contr&acute;rio.
	 */
	private boolean sameParameters(Method m1, Method m2) {
		int index = 0;
		TypeSymbol type1 = null, type2 = null;

		if(m1.getParameters().size() != m2.getParameters().size()) {
			return false;
		}
		ArrayList<Binding> m1Params = m1.getParameters();

		for(Binding b : m2.getParameters()) {
			type1 = m1Params.get(index).getTypeSymbol();
			type2 = b.getTypeSymbol();

			if(!type1.equals(type2)) return false;
			index++;
		}

		return true;
	}
	
	/**
	 * M&eacute;todo que retorna o {@link Method} cujo s&iacute;mbolo
	 * de nome de m&eacute;todo &eacute; dado por <code>mname</code>.
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

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Class) && name.equals(((Class)obj).name);
	}

}