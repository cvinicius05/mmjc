package ufc.ck017.mmjc.semantic;

import java.util.List;

import ufc.ck017.mmjc.node.TId;
import ufc.ck017.mmjc.symbolTable.Binding;
import ufc.ck017.mmjc.symbolTable.TypeSymbol;

/**
 * Interface da Tabela de S&iacute;mbolos usada pelo {@link TypeChecker}.
 * <p>
 * Esta interface dita quais são as fun&ccedil;&otilde;es que a Tabela de
 * S&iacute;mbolos deve obrigatoriamente implementar para o funcionamento
 * correto da fase de checagem de tipos.
 *  
 * @author Arthur Rodrigues
 * @see ufc.ck017.mmjc.symbolTable.AbstractSymbolTable
 */
public interface SymbolTableInterface {
	
	/**
	 * Verifica na Tabela a exist&ecirc;ncia de um m&eacute;todo com a mesma assinatura do
	 * que o que é representado pelos par&acirc;metros dessa fun&ccedil;&atilde;o.
	 * Esta fun&ccedil;&atilde;o verifica se o m&eacute;todo representado pelo identificador
	 * <code>mname</code> é um método declarado da classe &agrave; qual pertence o objeto
	 * chamador, representada pelo par&acirc;metro <code>obj</code>, e se os par&acirc;metros
	 * passados s&atilde;o compat&iacute;veis em tipo. 
	 *  
	 * @param obj tipo do objeto a chamador.
	 * @param mname identificador do m&eacute;todo chamado.
	 * @param params lista de tipos dos par&acirc;metros passados ao m&eacute;todo.
	 * @return <code>true</code> caso o m&eacute;todo exista, <code>false</code> caso contr&aacute;rio.
	 */
	public boolean isMethod(TypeSymbol obj, TId mname, List<TypeSymbol> params);
	
	/**
	 * Verifica na Tabela a exist&ecirc;ncia de uma classe cujo nome &eacute; representado
	 * pelo par&acirc;metro <code>cname</code>.
	 * 
	 * @param cname identificador da classe referenciada.
	 * @return <code>true</code> caso uma classe com o mesmo nome seja encontrada, <code>false</code> caso contr&aacute;rio.
	 */
	public boolean isClass(TId cname);
	
	/**
	 * Verifica na Tabela se o tipo representado pelo par&acirc;metro <code>type</code> representa
	 * uma classe.
	 * 
	 * @param type tipo a verificar.
	 * @return <code>true</code> caso o tipo represente uma classe na Tabela, <code>false</code> caso contr&aacute;rio.
	 */
	public boolean isClass(TypeSymbol type);
	
	/**
	 * Verifica, no escopo atual, a exist&ecirc;ncia de uma vari&aacute;vel representada pelo identificador
	 * <code>id</code>.
	 * 
	 * @param id identificador da vari&aacute;vel a procurar.
	 * @return <code>true</code> caso a vari&aacute;vel conste no escopo atual, <code>false</code> caso contr&aacute;rio.
	 */
	public boolean isVar(TId id);
	
	/**
	 * Verifica, no escopo atual, se existe uma vari&aacute;vel representada pelo identificador
	 * <code>id</code>, e se tal vari&aacute;vel &eacute; um objeto.
	 * 
	 * @param id identificador da vari&aacute;vel a procurar.
	 * @return <code>true</code> caso a vari&aacute;vel conste no escopo atual e seja um objeto, <code>false</code> caso contr&aacute;rio.
	 */
	public boolean isObject(TId id);
	
	/**
	 * Verifica se a classe representada pelo s&iacute;mbolo <code>supcname</code> &eacute;
	 * uma superclasse da representada pelo s&iacute;mbolo <code>subcname</code>.
	 * 
	 * @param supcname s&iacute;mbolo da superclasse a verificar.
	 * @param subcname s&iacute;mbolo da subclasse a verificar.
	 * @return <code>true</code> caso <code>subcname</code> represente uma subclasse de <code>supname</code>, <code>false</code> caso contr&aacute;rio.
	 */
	public boolean isSubclassOf(TypeSymbol supcname, TypeSymbol subcname);
	
	/**
	 * Retorna o tipo salvo na tabela da vari&aacute;vel representada pelo identificador
	 * <code>id</code> no escopo atual.
	 * 
	 * @param id identificador da vari&aacute;vel a analisar.
	 * @return {@link TypeSymbol} representando o tipo presente no {@link Binding} encontrado, <code>null</code> caso n&atilde;o haja {@link Binding} no escopo atual para a vari&aacute;vel.
	 */
	public TypeSymbol getType(TId id);
	
	/**
	 * Retorna o tipo salvo na tabela do m&eacute;todo representado pelo identificador
	 * <code>id</code> no escopo da classe representada pelo s&iacute;mbolo <code>cname</code>.
	 * 
	 * @param cname s&iacute;mbolo que representa a classe cujo escopo ser&aacute; analisado.
	 * @param id identificador do m&eacute;todo a analisar.
	 * @return {@link TypeSymbol} representando o tipo presente no {@link Binding} encontrado, <code>null</code> caso n&atilde;o haja {@link Binding} no escopo da classe informada para o m&eacute;todo.
	 */
	public TypeSymbol getType(TypeSymbol cname, TId id);
	
	/**
	 * Informa &agrave; Tabela que o escopo atualmente sendo analisado &eacute; o da classe
	 * ou do m&eacute;todo representado pelo identificador <code>id</code>.
	 * 
	 * @param id identificador da classe ou m&eacute;todo cujo escopo est&aacute; sendo analisado.
	 */
	public void enterScope(TId id);
	
	/**
	 * Informa &agrave; Tabela que o escopo analisado &eacute; aquele imediatamente
	 * superior ao &uacute;ltimo informado.
	 */
	public void exitScope();
}