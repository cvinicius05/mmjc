package ufc.ck017.mmjc.util;

import ufc.ck017.mmjc.node.AAtbStatement;
import ufc.ck017.mmjc.node.AVatbStatement;
import ufc.ck017.mmjc.node.PExpression;
import ufc.ck017.mmjc.node.TId;

public class SemanticError {
	// TODO Melhorar as mensagens de erro, dizendo por exemplo os tipos esperados e os obtidos.
	
	public static String idNotFound(TId id) {
		return "Semantic error at ["+id.getLine()+','+id.getPos()+":\n\tIdentifier \'"+id.getText()+"\' was not previously defined at this scope.";
	}
	
	public static String invalidAtb(AAtbStatement atbst) {
		return "Semantic error at ["+atbst.getId().getLine()+','+atbst.getId().getPos()+":\n\tInvalid operation. Check the types of the identifier and the expression involved.";
	}
	
	public static String invalidAtb(AVatbStatement atbst) {
		return "Semantic error at ["+atbst.getId().getLine()+','+atbst.getId().getPos()+":\n\tInvalid operation. Check the types of the identifier, the index, and the expression involved.";
	}
	
	public static String expectedExprType(PExpression expr, SemanticType expected, SemanticType got) {
		return null;//"Semantic error at ["+expr.getLine()+','+expr.getPos()+":\n\tExpected "+expected+" expression but got "+got+" expression.";
	}

	public static String methodExpected(TId mname) {
		return "Semantic error at ["+mname.getLine()+','+mname.getPos()+":\n\tIdentifier \'"+mname.getText()+"\' should name a method.";
	}
}
