package ufc.ck017.mmjc.symbolTable;

import java.util.Dictionary;
import java.util.Hashtable;

import ufc.ck017.mmjc.node.ABoolType;
import ufc.ck017.mmjc.node.AClassType;
import ufc.ck017.mmjc.node.AInttType;
import ufc.ck017.mmjc.node.AIntvType;
import ufc.ck017.mmjc.node.PType;
import ufc.ck017.mmjc.node.TId;

/**
 * Classe que implementa uma representa&ccedil;&atilde;o do
 * tipo de um vari&aacute;vel na forma de um objeto &uacute;nico.
 * <p>
 * Associado a cada string de tipo encontrada no programa &eacute;
 * criado um novo s&iacute;mbolo para esse tipo que &eacute; uma
 * representa&ccedil;&atilde;o &uacute;nica. Dessa forma, a 
 * compara&ccedil;&atilde;o de tipos &eacute; feita por meio dos
 * s&iacute;mbolos de cada tipo, evitando a compara&ccedil;&atilde;o
 * entre strings elemento a elemento.
 * A associa&ccedil;&atilde;o entre um tipo de vari&aacute;vel e
 * seu s&iacute;mbolo &eacute; feita atrav&eacute;s do uso de um
 * dicion&aacute;rio que associa uma String a seu s&iacute;mbolo.
 * 
 * @author vinicius
 *
 */
public class TypeSymbol {
	
	private String typeName;
	private static Dictionary<String, TypeSymbol> typeDict = new Hashtable<String, TypeSymbol>();
	
	private TypeSymbol(String type) {
		typeName = type;
	}
	
	public String toString() {
		return typeName;
	}
	
	public Dictionary<String, TypeSymbol> getDictonary() {
		return typeDict;
	}
	
	/**
	 * M&eacute;todo que cria e armazena no dicion&aacute;rio 
	 * um s&iacute;mbolo associado a string <b>int</b>.
	 * 
	 * @return s&iacute;mbolo associado a string <b>int</b>.
	 */
	public static TypeSymbol getIntTSymbol() {
		return TypeSymbol.getFromString("int");
	}
	
	/**
	 * M&eacute;todo que cria e armazena no dicion&aacute;rio 
	 * um s&iacute;mbolo associado a string <b>int[]</b>.
	 * 
	 * @return s&iacute;mbolo associado a string <b>int[]</b>.
	 */
	public static TypeSymbol getIntVSymbol() {
		return TypeSymbol.getFromString("int[]");
	}
	
	/**
	 * M&eacute;todo que cria e armazena no dicion&aacute;rio 
	 * um s&iacute;mbolo associado a string <b>boolean</b>.
	 * 
	 * @return s&iacute;mbolo associado a string <b>boolean</b>.
	 */
	public static TypeSymbol getBoolSymbol() {
		return TypeSymbol.getFromString("boolean");
	}
	
	/**
	 * M&eacute;todo que cria e armazena no dicion&aacute;rio 
	 * um s&iacute;mbolo associado a produ&ccedil;&atilde;o
	 * do tipo <b>PType</b> atrav&eacute;s da string que 
	 * representa o tipo.
	 * 
	 * @param ptype representa a produ&ccedil;&atilde;o do tipo <b>PType</b>.
	 * @return s&iacute;mbolo associado ao tipo de <b>PType</b>.
	 */
	public static TypeSymbol symbol(PType ptype) {
		String s = null;

		if(ptype instanceof AInttType) s = "int";
		else if(ptype instanceof AIntvType) s = "int[]";
		else if(ptype instanceof ABoolType) s = "boolean";
		else if(ptype instanceof AClassType) s = ((AClassType) ptype).getId().getText();

		return TypeSymbol.getFromString(s);
	}
	
	/**
	 * M&eacute;todo que cria e armazena no dicion&aacute;rio 
	 * um s&iacute;mbolo associado a um token do tipo 
	 * <b>TId</b> atrav&eacute;s da string que representa
	 * o tipo do token.
	 * 
	 * @param id representa o token do tipo <b>TId</b>.
	 * @return novo s&iacute;mbolo associado ao tipo de <b>TId</b>.
	 */
	public static TypeSymbol symbolOfID(TId id) {
		return TypeSymbol.getFromString(id.getText());
	}
	
	/**
	 * M&eacute;todo que cria e armazena no dicion&aacute;rio 
	 * um s&iacute;mbolo associado a uma string de um tipo. 
	 * 
	 * @param typeName representa string de tipo a ser traduzida. 
	 * @return novo s&iacute;mbolo associado a string de entrada.
	 */
	private static TypeSymbol getFromString(String typeName){
		String u = typeName.intern();
		TypeSymbol symbol = (TypeSymbol)typeDict.get(u);
		
		if(symbol == null) {
			symbol = new TypeSymbol(u);
			typeDict.put(u,symbol);
		}
		return symbol;
	}
}