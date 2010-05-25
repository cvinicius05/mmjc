package ufc.ck017.mmjc.translate.activationRegister.temp;

import ufc.ck017.mmjc.symbolTable.VarSymbol;

/**
 * A Label represents an address in assembly language.
 */

public class Label  {
	private String name;
	private static int count;

	/**
	 * a printable representation of the label, for use in assembly 
	* language output.
	*/
	public String toString() {return name;}

	/**
	* Makes a new label that prints as "name".
	* Warning: avoid repeated calls to <tt>new Label(s)</tt> with
	* the same name <tt>s</tt>.
	*/
	public Label(String n) {
		name=n;
	}

	/**
	* Makes a new label with an arbitrary name.
	*/
	public Label() {
		this("L" + count++);
	}

	/**
	* Makes a new label whose name is the same as a symbol.
	*/
	public Label(VarSymbol s) {
		this(s.toString());
	}
}
