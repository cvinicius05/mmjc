package ufc.ck017.mmjc.util;

import ufc.ck017.mmjc.node.AAtbStatement;
import ufc.ck017.mmjc.node.AVatbStatement;
import ufc.ck017.mmjc.node.PExpression;
import ufc.ck017.mmjc.node.TId;
import ufc.ck017.mmjc.symbolTable.TypeSymbol;

/**
 * F&aacute;brica de {@link String}s representativas dos erros sem&acirc;nticos que podem
 * ocorrer durante as fases de cria&ccedil;&atilde;o da Tabela de S&iacute;mbolos e de
 * checagem de tipos do compilador.
 * Elas ser&atilde;o os erros visualizados pelo usu&aacute;rio ao fim dessas fases.  
 * 
 * @author Arthur Rodrigues
 */
public class SemanticError {
	// TODO Melhorar as mensagens de erro, dizendo por exemplo os tipos esperados e os obtidos.
	public static final String err = "Semantic error at [";
	
	public static String idNotFound(TId id) {
		return err+id.getLine()+','+id.getPos()+"]:\n\tIdentifier \'"+id.getText()+"\' was not previously defined at this scope.";
	}
	
	public static String invalidAtb(AAtbStatement atbst) {
		return err+atbst.getId().getLine()+','+atbst.getId().getPos()+"]:\n\tInvalid assignment. Check the types of the identifier and the expression involved.";
	}
	
	public static String invalidAtb(AVatbStatement atbst) {
		return err+atbst.getId().getLine()+','+atbst.getId().getPos()+"]:\n\tInvalid assignment. Check the types of the identifier (must be <int[]>), the index (must be <int>), and the expression involved (must be <int>).";
	}
	
	public static String expectedExprType(PExpression expr, TypeSymbol expected, TypeSymbol got) {
		return err+expr.getLine()+','+expr.getPos()+"]:\n\tExpected <"+expected+"> type expression but got <"+got+"> type expression.";
	}

	public static String methodExpected(TypeSymbol cname, TId mname) {
		return err+mname.getLine()+','+mname.getPos()+"]:\n\tIdentifier \'"+mname.getText()+"\' either does not name a method of class <"+cname+"> or the parameters are invalid.";
	}

	public static String invalidAtbToObject(TId idst, TypeSymbol classtype) {
		return err+idst.getLine()+','+idst.getPos()+"]:\n\tIdentifier has type <"+TypeSymbol.symbolOfID(idst)+"> but was assigned an incompatible value of type <"+classtype+">.";
	}

	public static String incompatibleClassAtb(TId idst, TypeSymbol classtype) {
		return err+idst.getLine()+','+idst.getPos()+"]:\n\tThe class <"+TypeSymbol.symbolOfID(idst)+"> is not a superclass of the <"+classtype+"> class.";
	}

	public static String objectExpected(PExpression object) {
		return err+object.getLine()+','+object.getPos()+"]:\n\tExpected object or \'this\' expression, but got <"+object.getType()+"> type expression.";
	}

	public static String invalidOperationExpr(PExpression l, PExpression r, TypeSymbol type) {
		return err+r.getLine()+','+r.getPos()+"]:\n\tThe operands of the desired operation must both be <"+type+">, but got <"+l.getType()+"> and <"+r.getType()+"> instead.";
	}

	public static String invalidVAcess(PExpression expr, boolean vec) {
		String cerr = err+expr.getLine()+','+expr.getPos()+"]:\n\tInvalid Array access. Expected <";
		if(vec)
			return cerr+TypeSymbol.getIntVSymbol()+"> type, but got <"+expr.getType()+"> instead.";
		else
			return cerr+TypeSymbol.getIntTSymbol()+"> type, but got <"+expr.getType()+"> instead.";
	}

	public static String classNotFound(TId id) {
		return err+id.getLine()+','+id.getPos()+"]:\n\tIdentifier \'"+id.getText()+"\' does not name a class.";
	}
}