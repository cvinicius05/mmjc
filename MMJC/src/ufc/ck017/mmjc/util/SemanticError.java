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
	
	public static String idNotFound(TId id) {
		return "Semantic error at ["+id.getLine()+','+id.getPos()+"]:\n\tIdentifier \'"+id.getText()+"\' was not previously defined at this scope.";
	}
	
	public static String invalidAtb(AAtbStatement atbst) {
		return "Semantic error at ["+atbst.getId().getLine()+','+atbst.getId().getPos()+"]:\n\tInvalid assignment. Check the types of the identifier and the expression involved.";
	}
	
	public static String invalidAtb(AVatbStatement atbst) {
		return "Semantic error at ["+atbst.getId().getLine()+','+atbst.getId().getPos()+"]:\n\tInvalid assignment. Check the types of the identifier (must be <int[]>), the index (must be <int>), and the expression involved (must be <int>).";
	}
	
	public static String expectedExprType(PExpression expr, TypeSymbol expected, TypeSymbol got) {
		return null;//"Semantic error at ["+expr.getLine()+','+expr.getPos()+"]:\n\tExpected <"+expected+"> type expression but got <"+got+"> type expression.";
	}

	public static String methodExpected(TypeSymbol cname, TId mname) {
		return "Semantic error at ["+mname.getLine()+','+mname.getPos()+"]:\n\tIdentifier \'"+mname.getText()+"\' does not name a method of class <"+cname+">.";
	}

	public static String invalidAtbToObject(TId idst, TypeSymbol classtype) {
		return "Semantic error at ["+idst.getLine()+','+idst.getPos()+"]:\n\tIdentifier has type <"+TypeSymbol.symbolOfID(idst)+"> but was assigned an incompatible value of type <"+classtype+">.";
	}

	public static String incompatibleClassAtb(TId idst, TypeSymbol classtype) {
		return "Semantic error at ["+idst.getLine()+','+idst.getPos()+"]:\n\tThe class <"+TypeSymbol.symbolOfID(idst)+"> is not a superclass of the <"+classtype+"> class.";
	}

	public static String invalidTypePrint(PExpression expr) {
		return null;//"Semantic error at ["+expr.getLine()+','+expr.getPos()+"]:\n\tExpected <"+TypeSymbol.getIntTSymbol()+"> expression but got <"+expr.getType()+"> type expression.";
	}

	public static String objectExpected(PExpression object) {
		return null;//"Semantic error at ["+object.getLine()+','+object.getPos()+"]:\n\tExpected object or \'this\' expression, but got <"+object.getType()+"> type expression.";
	}
}