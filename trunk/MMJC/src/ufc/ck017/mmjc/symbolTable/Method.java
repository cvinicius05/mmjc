package ufc.ck017.mmjc.symbolTable;

import java.util.ArrayList;
import java.util.Hashtable;

import ufc.ck017.mmjc.node.AVar;
import ufc.ck017.mmjc.node.PMethod;
import ufc.ck017.mmjc.node.PType;
import ufc.ck017.mmjc.node.TId;

/**
 * Classe que implementa os m&eacute;todos das classes declaradas
 * no programa de entrada, onde s&atilde;o implementados os
 * m&eacute;todos de acesso e aloca&ccedil;&atilde;o &agrave;s
 * suas var&aacute;veis locais, par&acirc;metros formais, tipo de
 * retorno e classe ao qual pertencem.
 * 
 * @author vinicius
 *
 */
public class Method extends ScopeEntry {

	private VarSymbol name = null;
	private TypeSymbol typeOfReturn = null;
	private Class superclass = null;
	private ArrayList<Binding> parameters = null;
	private Hashtable<VarSymbol, TypeSymbol> localVariables = null;

	public Method(TId name, Class pclass, PType type, int numParam, int numLocal) {
		this.name = VarSymbol.symbol(name);
		typeOfReturn = TypeSymbol.symbol(type);
		superclass = pclass;
		parameters = new ArrayList<Binding>(numParam);
		localVariables = new Hashtable<VarSymbol, TypeSymbol>(numLocal);
	}
	
	/**
	 * Retorna o nome do m&eacute;todo representado por
	 * um s&iacute;mbolo do tipo {@link VarSymbol}.
	 * 
	 * @return {@link VarSymbol} representando o nome do m&eacute;todo.
	 */
	public VarSymbol getName() {
		return name;
	}

	/**
	 * Retorna o tipo de retorno do m&eacute;todo representado
	 * por um s&iacute;mbolo do tipo {@link TypeSymbol}.
	 * 
	 * @return {@link TypeSymbol} representando o nome do tipo de retorno.
	 */
	public TypeSymbol getTypeOfReturn() {
		return typeOfReturn;
	}

	/**
	 * Retorna o nome da classe ao qual o m&eacute;todo 
	 * pertence representado por um s&iacute;mbolo do tipo
	 * {@link VarSymbol}.
	 * 
	 * @return {@link VarSymbol} representando o nome da classe
	 * ao qual o m&eacute;todo pertence.
	 */
	public Class getNameOfClass() {
		return superclass;
	}

	/**
	 * Retorna o vetor de par&acirc;metros do m&eacute;todo,
	 * onde cada elemento &eacute; uma associa&ccedil;&atilde;o
	 * entre um tipo e uma vari&aacute;vel na forma de s&iacute;mbolos
	 * formando um binding.
	 * 
	 * @return par&acirc;metros na forma de um ArrayList de {@link Binding}.
	 */
	public ArrayList<Binding> getParameters() {
		return parameters;
	}

	/**
	 * Retorna o vetor de vari&aacute;veis locais do novo 
	 * m&eacute;todo, onde cada elemento &eacute; uma 
	 * associa&ccedil;&atilde;o entre um tipo e uma 
	 * vari&aacute;vel na forma de s&iacute;mbolos
	 * formando um binding.
	 * 
	 * @return  vari&aacute;veis locais na forma de um
	 * ArrayList de {@link Binding}.
	 */
	public Hashtable<VarSymbol, TypeSymbol> getLocalVariables() {
		return localVariables;
	}

	/**
	 * M&eacute;todo que associa os par&acirc;metros do novo
	 * m&eacute;todo representado pela produ&ccedil;&atilde;o
	 * do tipo {@link PMethod}.
	 * 
	 * @param method produ&ccedil;&atilde;o do tipo {@link PMethod}.
	 */
	public boolean addParamater(AVar var) {
		Binding b = new Binding(VarSymbol.symbol(var.getId()), TypeSymbol.symbol(var.getType()));

		if(parameters.contains(b)) return false;
		
		parameters.add(b);
		return true;
	}

	/**
	 * M&eacute;todo que associa as vari&aacute;veis locais no
	 * novo m&eacute;todo representado pela produ&ccedil;&atilde;o
	 * do tipo {@link PMethod}.
	 * 
	 * @param method produ&ccedil;&atilde;o do tipo {@link PMethod}.
	 */
	public boolean addLocalVar(AVar var) {
		VarSymbol v = VarSymbol.symbol(var.getId());

		if(localVariables.get(v) != null) return false;
		
		localVariables.put(v, TypeSymbol.symbol(var.getType()));
		return true;
	}

	/**
	 * Retorna um s&iacute;mbolo para o tipo do par&acirc;metro
	 * formal cujo nome seja o mesmo associado ao valor de entrada,
	 * se tal elemento existe.
	 * 
	 * @param id token que representa uma vari&aacute;vel.
	 * @return TypeSymbol do par&acirc;metro formal, se
	 * encontrado, e null caso contr&aacute;rio.
	 */
	public TypeSymbol getTypeOfParameter(TId id) {
		VarSymbol temp = VarSymbol.symbol(id);
		
		for(Binding b : parameters) {
			if(b.getVarSymbol().equals(temp)) return b.getTypeSymbol();
		}
		
		return null;
	}

	/**
	 * Retorna um s&iacute;mbolo para o tipo da vari&aacute;vel
	 * local cujo nome seja o mesmo associado ao valor de entrada,
	 * se tal elemento existe.
	 * 
	 * @param id token que representa uma vari&aacute;vel.
	 * @return TypeSymbol da vari&aacute;vel local, se
	 * encontrado, e null caso contr&aacute;rio.
	 */
	public TypeSymbol getTypeOfLocalVariable(TId id) {
		return localVariables.get(VarSymbol.symbol(id));
	}

	@Override
	public Class getParent() {
		return superclass;
	}

	@Override
	public ScopeEntry getSuperScope() {
		return superclass;
	}
}