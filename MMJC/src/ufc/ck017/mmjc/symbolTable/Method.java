package ufc.ck017.mmjc.symbolTable;

import java.util.ArrayList;
import java.util.Iterator;

import ufc.ck017.mmjc.node.AMethod;
import ufc.ck017.mmjc.node.AVar;
import ufc.ck017.mmjc.node.PMethod;
import ufc.ck017.mmjc.node.PVar;
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
public class Method {

	private VarSymbol name = null;
	private TypeSymbol typeOfReturn = null;
	private VarSymbol nameOfClass = null;
	private ArrayList<Binding> parameters = null;
	private ArrayList<Binding> localVariables = null;

	public Method(PMethod method, VarSymbol nameOfClass) {
		int numberOfParameters = ((AMethod) method).getParam().size();
		parameters = new ArrayList<Binding>(numberOfParameters);

		int numberOfVariables = ((AMethod) method).getLocal().size();
		localVariables = new ArrayList<Binding>(numberOfVariables);

		name = VarSymbol.symbol(((AMethod) method).getId());
		typeOfReturn = TypeSymbol.symbol(((AMethod) method).getType());
		this.nameOfClass = nameOfClass;
		
		setLocalVariables(method);
		setParameters(method);
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
	public VarSymbol getNameOfClass() {
		return nameOfClass;
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
	public ArrayList<Binding> getLocalVariables() {
		return localVariables;
	}

	/**
	 * M&eacute;todo que associa os par&acirc;metros do novo
	 * m&eacute;todo representado pela produ&ccedil;&atilde;o
	 * do tipo {@link PMethod}.
	 * 
	 * @param method produ&ccedil;&atilde;o do tipo {@link PMethod}.
	 */
	private void setParameters(PMethod method) {
		PVar var = null;
		Iterator<PVar> iter = ((AMethod) method).getParam().iterator();
		int index;

		while(iter.hasNext()) {
			var = iter.next();

			VarSymbol v = VarSymbol.symbol(((AVar) var).getId());
			TypeSymbol t = TypeSymbol.symbol(((AVar) var).getType());
			Binding b = new Binding(v, t);

			index = v.hashCode() % parameters.size();
			if(index < 0) index = -index;

			while(parameters.get(index) != null) index = (index+1) % parameters.size();
			parameters.add(index, b);
		}
	}

	/**
	 * M&eacute;todo que associa as vari&aacute;veis locais no
	 * novo m&eacute;todo representado pela produ&ccedil;&atilde;o
	 * do tipo {@link PMethod}.
	 * 
	 * @param method produ&ccedil;&atilde;o do tipo {@link PMethod}.
	 */
	private void setLocalVariables(PMethod method) {
		PVar var = null;
		Iterator<PVar> iter = ((AMethod) method).getLocal().iterator();
		int index;

		while(iter.hasNext()) {
			var = iter.next();

			VarSymbol v = VarSymbol.symbol(((AVar) var).getId());
			TypeSymbol t = TypeSymbol.symbol(((AVar) var).getType());
			Binding b = new Binding(v, t);

			index = v.hashCode() % localVariables.size();
			if(index < 0) index = -index;

			while(localVariables.get(index) != null) index = (index+1) % localVariables.size();
			localVariables.add(index, b);
		}
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
		VarSymbol nameOfId = VarSymbol.symbol(id);

		int index = nameOfId.hashCode() % parameters.size();
		if(index < 0) index = -index;

		if(parameters.get(index).getVarSymbol().equals(nameOfId))
			return parameters.get(index).getTypeSymbol();

		int marker = index;
		do {
			index = (index+1) % parameters.size();
		}
		while(!parameters.get(index).getVarSymbol().equals(nameOfId) && marker != index);

		if(marker != index) return parameters.get(index).getTypeSymbol();
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
		VarSymbol v = VarSymbol.symbol(id);

		int index = v.hashCode() % localVariables.size();
		if(index < 0) index = -index;

		if(localVariables.get(index).getVarSymbol().equals(v))
			return localVariables.get(index).getTypeSymbol();

		int marker = index;
		do {
			index = (index+1) % localVariables.size();
		}
		while(!localVariables.get(index).getVarSymbol().equals(v) && index != marker);

		if(marker != index) return localVariables.get(index).getTypeSymbol();
		return null;
	}
}