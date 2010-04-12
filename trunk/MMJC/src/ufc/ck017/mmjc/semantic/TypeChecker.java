package ufc.ck017.mmjc.semantic;

import ufc.ck017.mmjc.analysis.DepthFirstAdapter;
import ufc.ck017.mmjc.node.*;
import ufc.ck017.mmjc.util.ErrorLog;
import ufc.ck017.mmjc.util.SemanticError;
import ufc.ck017.mmjc.util.SemanticType;

public class TypeChecker extends DepthFirstAdapter {
	private SymbolTable table = null;
	private ErrorLog errors = ErrorLog.getInstance();
	// TODO descobrir como gerenciar isso... Uma pilha, talvez. Mas como saber a classe e o metodo ao mesmo tempo? Talvez duas, então...
	private TId currclass = null;
	private TId currmethod = null;
	private TId parent = null;
	
	public TypeChecker(SymbolTable symbols) {
		table = symbols;
	}
	
	private boolean checkExprType(PExpression expression, SemanticType type) {
		SemanticType exst = SemanticType.BOOL;
		//SemanticType exst = expression.getType();
		
		if(exst != SemanticType.BOOL) {
			errors.addError(SemanticError.expectedExprType(expression, type, exst));
			return true;
		} else 
			return false;
	}
	
	@Override
	public void outAIfStatement(AIfStatement node) {
		PExpression expression = node.getExpression();
		
		if(checkExprType(expression, SemanticType.BOOL)) {
			//node.setType(SemanticType.NONE);
		} else {
			//node.setType(SemanticType.INVALID);
		}
	}
	
	// TODO Verificar se a atribuicao entre objetos eh valida (se as classes sao a mesma ou se sao compativeis)
	// TODO Criar uma "arvore de heranca" para ajudar
	@Override
	public void outAAtbStatement(AAtbStatement node) {
		TId identifier = node.getId();
		PExpression expression = node.getExpression();
		
		SemanticType idst = SemanticType.INT, exst = SemanticType.INT;
		//SemanticType idst = table.getType(identifier);
		//SemanticType exst = expression.getType();
		
		if(idst == SemanticType.INVALID) {
			errors.addError(SemanticError.idNotFound(identifier));
			//node.setType(SemanticType.INVALID);
		} else if(idst != exst || exst == SemanticType.NONE || exst == SemanticType.INVALID || table.isMethod(currclass, identifier) || table.isClass(identifier)) {
			errors.addError(SemanticError.invalidAtb(node));
			//node.setType(SemanticType.INVALID);
		} else {
			//node.setType(idst);
		}
	}
	
	@Override
	public void outAVatbStatement(AVatbStatement node) {
		TId identifier = node.getId();
		PExpression iexpression = node.getI();
		PExpression vexpression = node.getV();
		
		SemanticType idst = SemanticType.INT, iexst = SemanticType.INT, vexst = SemanticType.INT;
		//SemanticType idst = table.getType(identifier);
		//SemanticType iexst = iexpression.getType();
		//SemanticType vexst = vexpression.getType();
		
		if(idst == SemanticType.INVALID) {
			errors.addError(SemanticError.idNotFound(identifier));
			//node.setType(SemanticType.INVALID);
		} else if(idst != SemanticType.INTV || iexst != SemanticType.INT || vexst != SemanticType.INT) {
			errors.addError(SemanticError.invalidAtb(node));
			//node.setType(SemanticType.INVALID);
		} else {
			//node.setType(idst);
		}
	}
	
	@Override
	public void outAWhileStatement(AWhileStatement node) {
		PExpression expression = node.getExpression();
		
		if(checkExprType(expression, SemanticType.BOOL)) {
			//node.setType(SemanticType.NONE);
		} else {
			//node.setType(SemanticType.INVALID);
		}
	}
	
	@Override
	public void outAPrintStatement(APrintStatement node) {
		PExpression expression = node.getExpression();
		
		if(checkExprType(expression, SemanticType.INT)) {
			//node.setType(SemanticType.NONE);
		} else {
			//node.setType(SemanticType.INVALID);
		}
	}
	
	@Override
	public void outAMcallExpression(AMcallExpression node) {
		PExpression object = node.getObj();
		TId mname = node.getId();
		
		SemanticType idst = SemanticType.INT;
		//SemanticType idst = table.getType(mname);
		
		if(idst == SemanticType.INVALID) {
			errors.addError(SemanticError.idNotFound(mname));
			//node.setType(SemanticType.INVALID);
		} else if(!table.isMethod(null, mname)) { // TODO modificar para a classe do objeto em questao
			errors.addError(SemanticError.methodExpected(mname));
		} else if(checkExprType(object, SemanticType.OBJECT)) {
			//node.setType(idst);
		}
	}
}
