package ufc.ck017.mmjc.symbolTable;

/**
 * Classe que implementa uma associa&ccedil&atilde;o entre um 
 * s&iacute;mbolo de vari&aacute;vel, {@link VarSymbol}, e seu
 * s&iacute;mbolo de tipo, {@link TypeSymbol}.
 * 
 * @author Arthur
 * @author Vinicius
 */
public class Binding {

	private TypeSymbol type;
	private VarSymbol var;

	public Binding(VarSymbol var, TypeSymbol type) {
		this.type = type;
		this.var = var;
	}

	public VarSymbol getVarSymbol() {
		return var;
	}

	public TypeSymbol getTypeSymbol() {
		return type;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Binding) && this.var.equals(((Binding)obj).var);
	}
}