package ufc.ck017.mmjc.symbolTable;

import java.util.Dictionary;
import java.util.Hashtable;

import ufc.ck017.mmjc.node.TId;

/**
 * Classe que implementa uma representa&ccedil;&atilde;o do
 * nome de uma vari&aacute;vel em forma de um objeto &uacute;nico.
 * <p>
 * Associa a cada string de vari&aacute;vel encontrada no 
 * programa onde &eacute; criado um novo s&iacute;mbolo para 
 * represent&aacute;-la, sendo uma representa&ccedil;&atilde;o 
 * &uacute;nica dessa vari&aacute;vel. Dessa forma, a 
 * compara&ccedil;&atilde;o de nomes de vari&aacute;veis &eacute;
 * feita por meio dos s&iacute;mbolos associados a cada uma,
 * evitando a compara&ccedil;&atilde;o entre strings elemento 
 * a elemento.
 * 
 * @author vinicius
 *
 */
public class VarSymbol {
	
	private String nameVar;
	private static Dictionary<String, VarSymbol> varDict = new Hashtable<String, VarSymbol>();
	
	private VarSymbol(String var) {
		nameVar = var;
	}

	public String toString() {
		return nameVar;
	}
	
	public Dictionary<String, VarSymbol> getDictonary() {
		return varDict;
	}
	
	/**
	 * Gera um novo s&iacute;mbolo associado a uma string que
	 * representa o nome de uma vari&aacute;vel.
	 * 
	 * @param nameVar nome da vari&aacute;vel.
	 * @return novo s&iacute;mbolo associado a vari&aacute;vel encontrada.
	 */
	public static VarSymbol symbolFromString(String nameVar) {
		String u = nameVar.intern();
	    VarSymbol s = (VarSymbol)varDict.get(u);
	   
	    if(s == null) {
	    	s = new VarSymbol(u);
	    	varDict.put(u,s);
	    }
	    return s;
	}

	/**
	 *Gera um novo s&iacute;bolo associado ao nome armazenado
	 *pelo token de entrada <b>TId</b>.
	 * 
	 * @param id token do tipo <b>TId</b>.
	 * @return novo s&iacute;mbolo associado ao token de entrada.
	 */
	public static VarSymbol symbol(TId id) {
		String str = id.getText();
	    String u = str.intern();
	    VarSymbol s = (VarSymbol)varDict.get(u);
	   
	    if(s == null) {
	    	s = new VarSymbol(u);
	    	varDict.put(u,s);
	    }
	    return s;
	}
}
