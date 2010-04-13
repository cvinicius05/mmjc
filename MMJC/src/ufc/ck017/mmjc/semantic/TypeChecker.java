package ufc.ck017.mmjc.semantic;

import java.util.LinkedList;
import java.util.Stack;

import ufc.ck017.mmjc.analysis.DepthFirstAdapter;
import ufc.ck017.mmjc.node.*;
import ufc.ck017.mmjc.symbolTable.TypeSymbol;
import ufc.ck017.mmjc.util.ErrorLog;
import ufc.ck017.mmjc.util.SemanticError;

public class TypeChecker extends DepthFirstAdapter {
	private SymbolTable table = null;
	private ErrorLog errors = ErrorLog.getInstance();
	private Stack<TypeSymbol> currclass = null;
	private final TypeSymbol INTT = null; //TypeSymbol.getIntTSymbol();
	private final TypeSymbol INTV = null; //TypeSymbol.getIntVSymbol();
	private final TypeSymbol BOOL = null; //TypeSymbol.getBoolSymbol();
	
	public TypeChecker(SymbolTable symbols) {
		table = symbols;
		currclass = new Stack<TypeSymbol>();
	}
	
	private boolean checkExprType(PExpression expression, TypeSymbol type) {
		TypeSymbol exst = BOOL;
		//TypeSymbol exst = expression.getType();
		
		if(exst != type) {
			errors.addError(SemanticError.expectedExprType(expression, type, exst));
			return true;
		} else 
			return false;
	}
	
	@Override
	public void inAExtNextclass(AExtNextclass node) {
		table.enterScope(node.getName());
		currclass.push(TypeSymbol.symbol(node.getName().getText()));
	}
	
	@Override
	public void outAExtNextclass(AExtNextclass node) {
		table.exitScope();
		currclass.pop();
	}
	
	@Override
	public void inANonextNextclass(ANonextNextclass node) {
		table.enterScope(node.getId());
		currclass.push(TypeSymbol.symbol(node.getId().getText()));
	}
	
	@Override
	public void outANonextNextclass(ANonextNextclass node) {
		table.exitScope();
		currclass.pop();
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
	public void outAIfStatement(AIfStatement node) {
		PExpression expression = node.getExpression();
		
		if(checkExprType(expression, BOOL)) {
			//node.setType(SemanticType.NONE);
		} else {
			//node.setType(SemanticType.INVALID);
		}
	}
	
	@Override
	public void outAAtbStatement(AAtbStatement node) {
		TId identifier = node.getId();
		PExpression expression = node.getExpression();
		
		TypeSymbol idst = INTT, exst = INTT;
		//TypeSymbol idst = table.getType(identifier);
		//TypeSymbol exst = expression.getType();
		
		if(idst == null) {
			errors.addError(SemanticError.idNotFound(identifier));
			//node.setType(SemanticType.INVALID);
		} else if(!table.isVar(identifier) || idst != exst || exst == null) {
			errors.addError(SemanticError.invalidAtb(node));
			//node.setType(SemanticType.INVALID);
		} else if(table.isObject(identifier)) {
			TypeSymbol classtype = INTT;
			//TypeSymbol classname = expression.getType();
						
			if(!table.isClass(classtype)) 
				errors.addError(SemanticError.invalidAtbToObject(identifier, classtype));
			else if(!table.isSubclassOf(identifier, classtype))
				errors.addError(SemanticError.incompatibleClassAtb(identifier, classtype));
			else {
				//node.setType(idst);
			}
		}
	}
	
	@Override
	public void outAVatbStatement(AVatbStatement node) {
		TId identifier = node.getId();
		PExpression iexpression = node.getI();
		PExpression vexpression = node.getV();
		
		TypeSymbol idst = INTT, iexst = INTT, vexst = INTT;
		//TypeSymbol idst = table.getType(identifier);
		//TypeSymbol iexst = iexpression.getType();
		//TypeSymbol vexst = vexpression.getType();
		
		if(idst == null) {
			errors.addError(SemanticError.idNotFound(identifier));
			//node.setType(SemanticType.INVALID);
		} else if(idst != INTV || iexst != INTT || vexst != INTT) {
			errors.addError(SemanticError.invalidAtb(node));
			//node.setType(SemanticType.INVALID);
		} else {
			//node.setType(idst);
		}
	}
	
	@Override
	public void outAWhileStatement(AWhileStatement node) {
		PExpression expression = node.getExpression();
		
		if(checkExprType(expression, BOOL)) {
			//node.setType(SemanticType.NONE);
		} else {
			//node.setType(SemanticType.INVALID);
		}
	}
	
	@Override
	public void outAPrintStatement(APrintStatement node) {
		PExpression expression = node.getExpression();
		
		if(!checkExprType(expression, INTT)) 
			errors.addError(SemanticError.invalidTypePrint(expression));
		//node.setType(null);
	}
	
	@Override
	public void outAMcallExpression(AMcallExpression node) {
		PExpression object = node.getObj();
		TId mname = node.getId();
		
		TypeSymbol idst = INTT;
		//SemanticType idst = table.getType(mname);
		
		if(idst == null) {
			errors.addError(SemanticError.idNotFound(mname));
			//node.setType(null);
		} else {
			LinkedList<PExpression> params = node.getPar();
			LinkedList<TypeSymbol> paramtypes = new LinkedList<TypeSymbol>();
			TypeSymbol objtype = null; //object.getType();
			
			for(PExpression exp : params) 
				paramtypes.add(null /*exp.getType()*/);
			
			if(!table.isClass(objtype)) {
				errors.addError(SemanticError.objectExpected(object));
				//node.setType(null);
			} else if(!table.isMethod(objtype, mname, paramtypes)) {
				errors.addError(SemanticError.methodExpected(objtype, mname));
				//node.setType(null);
			} else {
				//node.setType(table.getType(object.getType(), mname));
			}
		}
	}
}
