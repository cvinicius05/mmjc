package ufc.ck017.mmjc.translate;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import ufc.ck017.mmjc.activationRecords.frame.Access;
import ufc.ck017.mmjc.activationRecords.frame.Frame;
import ufc.ck017.mmjc.activationRecords.jouette.JouetteFrame;
import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.analysis.DepthFirstAdapter;
import ufc.ck017.mmjc.node.*;
import ufc.ck017.mmjc.translate.tree.*;
import ufc.ck017.mmjc.symbolTable.TypeSymbol;
import ufc.ck017.mmjc.symbolTable.VarSymbol;

public class Translate extends DepthFirstAdapter {
	private TypeSymbol currclass = null;
	private VarSymbol currmethod = null;
	private Stm localDecl = null;
	private Frame currframe = new JouetteFrame();
	private Stack<Exp> expstack = new Stack<Exp>();
	private Hashtable<VarSymbol, Access> accesstable = new Hashtable<VarSymbol, Access>();
	private List<Frag> frags = new LinkedList<Frag>();
	
	public List<Frag> getResult() {
		return frags;
	}
	
	@Override
	public void inAExtNextclass(AExtNextclass node) {
		currclass = TypeSymbol.search(node.getName().getText());
	}
	
	@Override
	public void inANonextNextclass(ANonextNextclass node) {
		currclass = TypeSymbol.search(node.getId().getText());
	}
	
	@Override
	public void inAMethod(AMethod node) {
		List<Boolean> formals = new LinkedList<Boolean>();
		Access newlocal = null;
		int i, size = node.getParam().size();
		
		localDecl = null;
		currmethod = VarSymbol.search(node.getId().getText());
		
		for(i=0; i <= size; i++) // por conta do *this*
			formals.add(false);
		
		currframe = currframe.newFrame(new Label(currclass+"$"+currmethod), formals);
		Iterator<Access> formalAccesses = currframe.formals.iterator();
		
		accesstable.put(VarSymbol.symbol("this"), formalAccesses.next());
		
		for(PVar var : node.getParam()) {
			accesstable.put(VarSymbol.search(((AVar)var).getId().getText()), formalAccesses.next());
		}
				
		for(PVar var : node.getLocal()) {
			newlocal = currframe.allocLocal(false);
			
			Exp nlexp = newlocal.exp(new TEMP(currframe.FP()));
			
			if(localDecl == null)
				localDecl = new STMEXP(nlexp);
			else
				localDecl = new SEQ(localDecl, new STMEXP(nlexp));
			
			accesstable.put(VarSymbol.search(((AVar)var).getId().getText()), newlocal);
		}
	}
	
	@Override
	public void outAMethod(AMethod node) {
		Stm methodBody = null;
		Stm returnvalue = new MOVE(new TEMP(currframe.RV()), expstack.pop());
		
		while(!expstack.isEmpty()) {
			if(methodBody == null)
				methodBody = new STMEXP(expstack.pop());
			else
				methodBody = new SEQ(new STMEXP(expstack.pop()), methodBody);
		}
				
		if(methodBody == null)
			methodBody = returnvalue;  
		if(localDecl != null)
			methodBody = new SEQ(localDecl, methodBody);
		
		// chamada a procEntryExit1 feita no construtor de Frag
		frags.add(new Frag(currframe, methodBody));
	}
	
	@Override
	public void outANumberExpression(ANumberExpression node) {
		expstack.push(new CONST(Integer.parseInt(node.getNumber().getText())));
	}
	
	@Override
	public void outABfalseExpression(ABfalseExpression node) {
		expstack.push(new CONST(0));
	}

	@Override
	public void outABtrueExpression(ABtrueExpression node) {
		expstack.push(new CONST(1));
	}
	
	@Override
	public void outASelfExpression(ASelfExpression node) {
		expstack.push(accesstable.get(VarSymbol.symbol("this")).exp());
	}
	
	@Override
	public void outANotExpression(ANotExpression node) {
		expstack.push(new BINOP(BINOP.XOR, expstack.pop(), new CONST(1)));
	}
	
	@Override
	public void outAAndExpression(AAndExpression node) {
		Exp temp = expstack.pop();
		expstack.push(new BINOP(BINOP.AND, expstack.pop(), temp));
	}
	
	@Override
	public void outAPlusExpression(APlusExpression node) {
		Exp temp = expstack.pop();
		expstack.push(new BINOP(BINOP.PLUS, expstack.pop(), temp));
	}
	
	@Override
	public void outAMinusExpression(AMinusExpression node) {
		Exp temp = expstack.pop();
		expstack.push(new BINOP(BINOP.MINUS, expstack.pop(), temp));
	}
	
	@Override
	public void outAMultExpression(AMultExpression node) {
		Exp temp = expstack.pop();
		expstack.push(new BINOP(BINOP.MUL, expstack.pop(), temp));
	}
	
	@Override
	public void outAGthanExpression(AGthanExpression node) {
		Exp temp = expstack.pop();
		Label t = new Label();
		Label f = new Label();
		Temp v = new Temp();
		
		expstack.push(
				new ESEQ(
						new SEQ(
								new MOVE(
										new TEMP(v),
										new CONST(1)
										),
								new SEQ(
										new CJUMP(CJUMP.GT, expstack.pop(), temp, t, f),
										new SEQ(
												new LABEL(f),
												new SEQ(
														new MOVE(
																new TEMP(v),
																new CONST(0)
																),
														new LABEL(t)
														)
												)
										)
								),
						new TEMP(v)
						));
	}
	
	@Override
	public void outALthanExpression(ALthanExpression node) {
		Exp temp = expstack.pop();
		Label t = new Label();
		Label f = new Label();
		Temp v = new Temp();
		
		expstack.push(
				new ESEQ(
						new SEQ(
								new MOVE(
										new TEMP(v),
										new CONST(1)
										),
								new SEQ(
										new CJUMP(CJUMP.LT, expstack.pop(), temp, t, f),
										new SEQ(
												new LABEL(f),
												new SEQ(
														new MOVE(
																new TEMP(v),
																new CONST(0)
																),
														new LABEL(t)
														)
												)
										)
								),
						new TEMP(v)
						));
	}
}
