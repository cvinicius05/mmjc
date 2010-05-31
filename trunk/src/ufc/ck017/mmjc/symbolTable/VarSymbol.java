package ufc.ck017.mmjc.symbolTable;

import java.util.Dictionary;
import java.util.Hashtable;

import ufc.ck017.mmjc.node.TId;

/**
 * Classe que implementa uma representa&ccedil;&atilde;o do
 * nome de uma vari&aacute;vel na forma de um objeto &uacute;nico.
 * <p>
 * Associa, a cada string de vari&aacute;vel encontrada no 
 * programa, um novo s&iacute;mbolo para represent&aacute;-la,
 * onde esta &eacute; uma representa&ccedil;&atilde;o &uacute;nica 
 * dessa vari&aacute;vel. Dessa forma, a compara&ccedil;&atilde;o
 * de nomes de vari&aacute;veis &eacute; feita por meio dos
 * s&iacute;mbolos associados a cada uma destas, evitando a 
 * compara&ccedil;&atilde;o entre strings elemento a elemento.
 * A associa&ccedil;&atilde;o entre um nome de vari&aacute;vel e
 * seu s&iacute;mbolo &eacute; feita atrav&eacute;s do uso de um
 * dicion&aacute;rio que associa uma String a seu s&iacute;mbolo.
 * 
 * @author Arthur
 * @author Vinicius
 *
 */
public class VarSymbol {

	private String nameVar;
	private static Dictionary<String, VarSymbol> varDict = new Hashtable<String, VarSymbol>();

	private VarSymbol(String var) {
		nameVar = var.intern();
	}

	public String toString() {
		return nameVar;
	}

	/**
	 * Busca por um s&iacute;mbolo associado a string de entrada que
	 * representa o nome de uma vari&aacute;vel.
	 * 
	 * @param nameVar nome da vari&aacute;vel.
	 * @return s&iacute;mbolo associado ao nome da vari&aacute;vel se encontrada
	 * e null, caso contr&aacute;rio.
	 */
	public static VarSymbol search(String nameVar) {
		String i = nameVar.intern();
	    VarSymbol vs = varDict.get(i);
	    
	    return vs;
	}
	
	/**
	 *Gera um novo s&iacute;mbolo associado ao nome armazenado
	 *pelo token de entrada <b>TId</b>.
	 * 
	 * @param id token do tipo <b>TId</b>.
	 * @return novo s&iacute;mbolo associado ao token de entrada.
	 */
	public static VarSymbol symbol(TId id) {
		return symbol(id.getText());
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof VarSymbol) && (((VarSymbol)obj).hashCode() == this.hashCode());
	}

	public static VarSymbol symbol(String str) {
	    String u = str.intern();
	    VarSymbol s = varDict.get(u);

	    if(s == null) {
	    	s = new VarSymbol(u);
	    	varDict.put(u,s);
	    }
	    return s;
	}
}