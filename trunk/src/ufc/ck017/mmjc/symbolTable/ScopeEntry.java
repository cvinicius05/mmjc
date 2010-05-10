package ufc.ck017.mmjc.symbolTable;

/**
 * Classe abstrata que representa a mudan&ccedil;a
 * de escopo a ser analisado.
 * 
 * @author Arthur
 * @author Vinicius
 *
 */
public abstract class ScopeEntry {
	
	/**
	 * Muda o escopo atual para o escopo da classe
	 * m&atilde;e da classe atual.
	 * 
	 * @return a classe m&atilde;e.
	 */
	public abstract Class getParent();
	
	/**
	 * Muda o escopo atual para o escopo global.
	 * 
	 * @return super escopo da classe atual. 
	 */
	public abstract ScopeEntry getSuperScope();
}