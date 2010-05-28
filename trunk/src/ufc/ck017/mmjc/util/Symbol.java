package ufc.ck017.mmjc.util;

import java.util.Dictionary;
import java.util.Hashtable;

public class Symbol {
	private String nameVar;
	private static Dictionary<String, Symbol> varDict = new Hashtable<String, Symbol>();

	private Symbol(String var) {
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
	public static Symbol search(String nameVar) {
		String i = nameVar.intern();
	    Symbol vs = varDict.get(i);
	    
	    return vs;
	}

	/**
	 *Gera um novo s&iacute;mbolo associado ao nome armazenado
	 *pelo token de entrada <b>TId</b>.
	 * 
	 * @param id token do tipo <b>TId</b>.
	 * @return novo s&iacute;mbolo associado ao token de entrada.
	 */
	public static Symbol symbol(String str) {
	    String u = str.intern();
	    Symbol s = varDict.get(u);

	    if(s == null) {
	    	s = new Symbol(u);
	    	varDict.put(u,s);
	    }
	    return s;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Symbol) && (((Symbol)obj).hashCode() == this.hashCode());
	}
}
