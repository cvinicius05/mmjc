package ufc.ck017.mmjc.semantic;

import java.util.LinkedList;
import java.util.Stack;

import ufc.ck017.mmjc.analysis.DepthFirstAdapter;
import ufc.ck017.mmjc.node.*;
import ufc.ck017.mmjc.symbolTable.SymbolTable;
import ufc.ck017.mmjc.symbolTable.TypeSymbol;
import ufc.ck017.mmjc.util.ErrorLog;
import ufc.ck017.mmjc.util.SemanticError;

/**
 * Esta classe implementa o <code>Visitor</code> respons&aacute;vel pela
 * fase de checagem de tipos do c&oacute;digo-fonte sendo analisado, fazendo
 * uso de uma Tabela de S&iacute;mbolos que implementa a interface {@link SymbolTableInterface}.
 * <p>
 * Note que nem todos os n&oacute;s da AST precisam gerar um efeito
 * colateral nessa fase, apenas os n&oacute;s que fazem refer&ecirc;ncias
 * de uso de vari&aacute;veis, m&eacute;todos ou classes.
 * Esses n&oacute;s s&atilde;o exatamente aqueles que representam <i>statements</i>
 * e <i>expressions</i>, definidos na gram&aacute;tica da linguagem.
 * <p>
 * Outro conjunto de n&oacute;s cuja visita se faz necess&aacute;ria, mas
 * neste caso devido &agrave; nossa implementa&ccedil;&atilde;o da Tabela de
 * S&iacute;mbolos, s&atilde;o os de declara&ccedil;&atilde;o de classes
 * e m&eacute;todos. Nesses n&oacute;s apenas informamos &agrave; Tabela que o
 * escopo atual mudou.
 * 
 * @author Arthur Rodrigues
 * @see SymbolTable
 */
public class TypeChecker extends DepthFirstAdapter {
	private SymbolTable table = SymbolTable.getInstance();
	private ErrorLog errors = ErrorLog.getInstance();
	private TypeSymbol currclass = null;
	private final TypeSymbol INTT = TypeSymbol.getIntTSymbol();
	private final TypeSymbol INTV = TypeSymbol.getIntVSymbol();
	private final TypeSymbol BOOL = TypeSymbol.getBoolSymbol();
	
	private boolean checkExprType(PExpression expression, TypeSymbol type) {
		TypeSymbol exst = expression.getType();
		
		if(exst == null || !exst.equals(type)) {
			errors.addError(SemanticError.expectedExprType(expression, type, exst));
			return false;
		}
		
		return true;
	}
	
	@Override
	public void inAExtNextclass(AExtNextclass node) {
		table.enterScope(node.getName());
		currclass = TypeSymbol.search(node.getName().getText());
	}
	
	@Override
	public void outAExtNextclass(AExtNextclass node) {
		table.exitScope();
	}
	
	@Override
	public void inANonextNextclass(ANonextNextclass node) {
		table.enterScope(node.getId());
		currclass = TypeSymbol.search(node.getId().getText());
	}
	
	@Override
	public void outANonextNextclass(ANonextNextclass node) {
		table.exitScope();
	}
	
	@Override
	public void inAMethod(AMethod node) {
		table.enterScope(node.getId());
	}
	
	@Override
	public void outAMethod(AMethod node) {
		table.exitScope();
	}
	
	@Override
	public void outAAtbStatement(AAtbStatement node) {
		TId identifier = node.getId();
		PExpression expression = node.getExpression();
		
		TypeSymbol idst = table.getType(identifier);
		TypeSymbol exst = expression.getType();
		
		if(idst == null) {
			errors.addError(SemanticError.idNotFound(identifier));
		} else if(table.isObject(identifier)) {
			TypeSymbol classtype = expression.getType();
						
			if(!table.isClass(classtype)) 
				errors.addError(SemanticError.invalidAtbToObject(identifier, classtype));
			else if(!table.isSubclassOf(idst, classtype))
				errors.addError(SemanticError.incompatibleClassAtb(identifier, classtype));
		} else if(!table.isVar(identifier) || exst == null || !idst.equals(exst)) {
			errors.addError(SemanticError.invalidAtb(node));
		}
	}
	
	@Override
	public void outAVatbStatement(AVatbStatement node) {
		TId identifier = node.getId();
		PExpression iexpression = node.getI();
		PExpression vexpression = node.getV();
		
		TypeSymbol idst = table.getType(identifier);
		TypeSymbol iexst = iexpression.getType();
		TypeSymbol vexst = vexpression.getType();
		
		if(idst == null) {
			errors.addError(SemanticError.idNotFound(identifier));
		} else if(idst != INTV || iexst != INTT || vexst != INTT) {
			errors.addError(SemanticError.invalidAtb(node));
		}
	}
	
	@Override
	public void outAWhileStatement(AWhileStatement node) {
		PExpression expression = node.getExpression();
		
		checkExprType(expression, BOOL);
	}
	
	@Override
	public void outAIfStatement(AIfStatement node) {
		PExpression expression = node.getExpression();
		
		checkExprType(expression, BOOL);
	}
	
	@Override
	public void outAPrintStatement(APrintStatement node) {
		PExpression expression = node.getExpression();
		
		checkExprType(expression, INTT);
	}
	
	@Override
	public void outAMcallExpression(AMcallExpression node) {
		PExpression object = node.getObj();
		TId mname = node.getId();
		LinkedList<PExpression> params = node.getPar();
		LinkedList<TypeSymbol> paramtypes = new LinkedList<TypeSymbol>();
		TypeSymbol objtype = object.getType();
		
		for(PExpression exp : params) 
			paramtypes.add(exp.getType());
		
		if(!table.isClass(objtype)) {
			errors.addError(SemanticError.objectExpected(object));
			node.setType(null);
		} else if(!table.isMethod(objtype, mname, paramtypes)) {
			errors.addError(SemanticError.methodExpected(objtype, mname));
			node.setType(null);
		} else {
			node.setType(table.getType(object.getType(), mname));
		}
	}
	
	@Override
	public void outAPlusExpression(APlusExpression node) {
		if(node.getL().getType() != INTT || node.getR().getType() != INTT) {
			errors.addError(SemanticError.invalidOperationExpr(node.getL(), node.getR(), INTT));
			node.setType(null);
		} else
			node.setType(INTT);
	}
	
	@Override
	public void outAMinusExpression(AMinusExpression node) {
		if(node.getL().getType() != INTT || node.getR().getType() != INTT) {
			errors.addError(SemanticError.invalidOperationExpr(node.getL(), node.getR(), INTT));
			node.setType(null);
		} else
			node.setType(INTT);
	}
	
	@Override
	public void outAMultExpression(AMultExpression node) {
		if(node.getL().getType() != INTT || node.getR().getType() != INTT) {
			errors.addError(SemanticError.invalidOperationExpr(node.getL(), node.getR(), INTT));
			node.setType(null);
		} else
			node.setType(INTT);
	}
	
	@Override
	public void outAGthanExpression(AGthanExpression node) {
		if(node.getL().getType() != INTT || node.getR().getType() != INTT) {
			errors.addError(SemanticError.invalidOperationExpr(node.getL(), node.getR(), INTT));
			node.setType(null);
		} else
			node.setType(BOOL);
	}
	
	@Override
	public void outALthanExpression(ALthanExpression node) {
		if(node.getL().getType() != INTT || node.getR().getType() != INTT) {
			errors.addError(SemanticError.invalidOperationExpr(node.getL(), node.getR(), INTT));
			node.setType(null);
		} else
			node.setType(BOOL);
	}
	
	@Override
	public void outAAndExpression(AAndExpression node) {
		if(node.getL().getType() != BOOL || node.getR().getType() != BOOL) {
			errors.addError(SemanticError.invalidOperationExpr(node.getL(), node.getR(), BOOL));
			node.setType(null);
		} else
			node.setType(BOOL);
	}
	
	@Override
	public void outAVectorExpression(AVectorExpression node) {
		if(node.getL().getType() == INTV) { 
			if(node.getI().getType() == INTT)
				node.setType(INTT);
			else
				errors.addError(SemanticError.invalidVAcess(node.getI(), false));
		} else {
			errors.addError(SemanticError.invalidVAcess(node.getL(), true));
			node.setType(null);
		}
	}
	
	@Override
	public void outALengthExpression(ALengthExpression node) {
		if(!checkExprType(node.getExpression(), INTV))
			node.setType(null);
		else
			node.setType(INTT);
	}
	
	@Override
	public void outANotExpression(ANotExpression node) {
		if(!checkExprType(node.getExpression(), BOOL))
			node.setType(null);
		else
			node.setType(BOOL);
	}
	
	@Override
	public void outANewvecExpression(ANewvecExpression node) {
		if(!checkExprType(node.getExpression(), INTT))
			node.setType(null);
		else
			node.setType(INTV);
	}
	
	@Override
	public void outAVarExpression(AVarExpression node) {
		TypeSymbol idst = table.getType(node.getId());
		
		if(idst == null) {
			errors.addError(SemanticError.idNotFound(node.getId()));
			node.setType(null);
		} else
			node.setType(idst);
	}
	
	@Override
	public void outANewobjExpression(ANewobjExpression node) {
		if(!table.isClass(node.getId())) {
			errors.addError(SemanticError.classNotFound(node.getId()));
			node.setType(null);
		} else
			node.setType(TypeSymbol.search(node.getId().getText()));
	}
	
	@Override
	public void outANumberExpression(ANumberExpression node) {
		node.setType(INTT);
	}
	
	@Override
	public void outABfalseExpression(ABfalseExpression node) {
		node.setType(BOOL);
	}
	
	@Override
	public void outABtrueExpression(ABtrueExpression node) {
		node.setType(BOOL);
	}
	
	@Override
	public void outASelfExpression(ASelfExpression node) {
		node.setType(currclass);
	}
}