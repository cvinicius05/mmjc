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
import ufc.ck017.mmjc.symbolTable.SymbolTable;
import ufc.ck017.mmjc.symbolTable.TypeSymbol;
import ufc.ck017.mmjc.symbolTable.VarSymbol;

public class Translate extends DepthFirstAdapter {
	private TypeSymbol currclass = null;
	private VarSymbol currmethod = null;
	private Stm localDecl = null;
	private Hashtable<VarSymbol, Exp> accesstable = null;
	private Frame currframe = new JouetteFrame();
	private Stack<Exp> expstack = new Stack<Exp>();
	private Stack<Stm> stmstack = new Stack<Stm>();
	private List<Frag> frags = new LinkedList<Frag>();
	private SymbolTable table = SymbolTable.getInstance();

	public List<Frag> getResult() {
		return frags;
	}

	@Override
	public void inAExtNextclass(AExtNextclass node) {
		accesstable =  new Hashtable<VarSymbol, Exp>();
		currclass = TypeSymbol.search(node.getName().getText());
	}

	@Override
	public void inANonextNextclass(ANonextNextclass node) {
		accesstable = new Hashtable<VarSymbol, Exp>();
		currclass = TypeSymbol.search(node.getId().getText());
	}

	@Override
	public void inAMethod(AMethod node) {
		List<Boolean> formals = new LinkedList<Boolean>();
		Access newlocal = null;
		int i, size = node.getParam().size();

		//localDecl = null;
		currmethod = VarSymbol.search(node.getId().getText());

		for(i=0; i <= size; i++) // por conta do *this*
			formals.add(false);

		currframe = currframe.newFrame(new Label(currclass+"$"+currmethod), formals);
		Iterator<Access> formalAccesses = currframe.formals.iterator();

		accesstable.put(VarSymbol.symbol("this"), formalAccesses.next().exp());

		for(PVar var : node.getParam())
			accesstable.put(VarSymbol.search(((AVar)var).getId().getText()), formalAccesses.next().exp());

		for(PVar var : node.getLocal()) {
			newlocal = currframe.allocLocal(false);

			Exp nlexp = newlocal.exp(new TEMP(currframe.FP()));

			if(localDecl == null)
				localDecl = new MOVE(nlexp, new CONST(0));
			else
				localDecl = new SEQ(localDecl, new MOVE(nlexp, new CONST(0)));

			accesstable.put(VarSymbol.search(((AVar)var).getId().getText()), nlexp);
		}
	}

	@Override
	public void outAMethod(AMethod node) {
		Stm methodBody = null;
		Stm returnvalue = new MOVE(new TEMP(currframe.RV()), expstack.pop());

		while(!expstack.isEmpty()) {
			if(methodBody == null)
				methodBody = stmstack.pop();
			else
				methodBody = new SEQ(stmstack.pop(), methodBody);
		}

		if(methodBody == null)
			methodBody = returnvalue; 
		else
			methodBody = new SEQ(methodBody, returnvalue);
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
		expstack.push(accesstable.get(VarSymbol.symbol("this")));
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

	@Override
	public void outANewobjExpression(ANewobjExpression node) {
		List<Exp> args = new LinkedList<Exp>();
		Temp newobj = new Temp();
		TEMP treeobj = new TEMP(newobj);
		Stm init = null;
		int size = table.getClass(node.getType()).getLocalVariables().size();

		args.add(new CONST((size+1)*currframe.wordSize()));

		init = new MOVE(treeobj, currframe.externalCall("malloc", args));

		for(int i = 0; i <= size; i++)
			init = new SEQ(
					init,
					new MOVE(
							new MEM(
									new BINOP(
											BINOP.PLUS,
											treeobj,
											new CONST(i*currframe.wordSize())
									)
							),
							new CONST(0)
					)
			);

		expstack.push(new ESEQ(init, treeobj));
	}

	@Override
	public void outANewvecExpression(ANewvecExpression node) {
		List<Exp> args = new LinkedList<Exp>();
		Exp size = expstack.pop();
		Temp newarray = new Temp();
		Temp arraysize = new Temp();
		Label condition = new Label();
		Label body = new Label();
		Label done = new Label();
		TEMP treearray = new TEMP(newarray);
		TEMP tempsize = new TEMP(arraysize);
		MEM treesize = new MEM(tempsize);
		Stm init = null;

		args.add(treesize);
		init = new MOVE(tempsize, new BINOP(BINOP.PLUS, size, new CONST(1)));
		init = new SEQ(init, new MOVE(treearray, currframe.externalCall("initArray", args)));
		init = new SEQ(init, new MOVE(new MEM(treearray), size));
		init = new SEQ(init, new JUMP(condition));
		init = new SEQ(init, new LABEL(body));
		init = new SEQ(init, new MOVE(tempsize, new BINOP(BINOP.MINUS, treesize, new CONST(1))));
		init = new SEQ(
				init,
				new MOVE(
						new MEM(
								new BINOP(
										BINOP.PLUS,
										treearray,
										new BINOP(
												BINOP.MUL,
												treesize,
												new CONST(currframe.wordSize())
										)
								)
						),
						new CONST(0)
				)
		);
		init = new SEQ(init, new LABEL(condition));
		init = new SEQ(init, new CJUMP(CJUMP.GT, treesize, new CONST(1), body, done));
		init = new SEQ(init, new LABEL(done));

		expstack.push(new ESEQ(init, treearray));
	}

	@Override
	public void outALengthExpression(ALengthExpression node) {
		expstack.push(new MEM(expstack.pop()));
	}
	
	@Override
	public void outAMcallExpression(AMcallExpression node) {
		ExpList args = null;
		int size = node.getPar().size();
		
		for(int i=0; i<size; i++)
			args = new ExpList(expstack.pop(), args);
		
		args = new ExpList(new MEM(expstack.pop()), args);
		
		expstack.push(
				new CALL(
					new NAME(new Label(node.getType()+"$"+VarSymbol.search(node.getId().getText()))),
					args
				)
		);
	}
	
	@Override
	public void outAVectorExpression(AVectorExpression node) {
		//Exp index = expstack.pop();
		
		//expstack.push();
	}
	
	@Override
	public void outAVarExpression(AVarExpression node) {
		// TODO Auto-generated method stub
		super.outAVarExpression(node);
	}

	// TODO rever no caso de atribuicao a um campo!!!
	@Override
	public void outAAtbStatement(AAtbStatement node) {
		stmstack.push(
				new MOVE(
						accesstable.get(VarSymbol.search(node.getId().getText())),
						expstack.pop()
				));
	}

	// TODO rever no caso de atribuicao a um campo!!!
	@Override
	public void outAVatbStatement(AVatbStatement node) {
		Exp value = expstack.pop();
		stmstack.push(
				new MOVE(
						new MEM(
								new BINOP(
										BINOP.PLUS,
										accesstable.get(VarSymbol.search(node.getId().getText())),
										new BINOP(
												BINOP.MUL,
												expstack.pop(),
												new CONST(currframe.wordSize())
										)
								)
						),
						value
				));
	}
	
	@Override
	public void outAIfStatement(AIfStatement node) {
		Label t = new Label();
		Label f = new Label();
		Label done = new Label();
		
		stmstack.push(
				new SEQ(
						new CJUMP(
								CJUMP.EQ,
								expstack.pop(),
								new CONST(1),
								t,
								f
						),
						new SEQ(
								new LABEL(f),
								new SEQ(
										stmstack.pop(),
										new SEQ(
												new JUMP(done),
												new SEQ(
														new LABEL(t),
														new SEQ(
																stmstack.pop(),
																new LABEL(done)
														)
												)
										)
								)
						)
				)
		);
	}
	
	@Override
	public void outAWhileStatement(AWhileStatement node) {
		Label body = new Label();
		Label done = new Label();
		Label condition = new Label();
		
		stmstack.push(
				new SEQ(
						new JUMP(condition),
						new SEQ(
								new LABEL(body),
								new SEQ(
										stmstack.pop(),
										new SEQ(
												new LABEL(condition),
												new SEQ(
														new CJUMP(
																CJUMP.EQ,
																expstack.pop(),
																new CONST(1),
																body,
																done
														),
														new LABEL(done)
												)
										)
								)
						)
				)
		);
	}
	
	@Override
	public void outAPrintStatement(APrintStatement node) {
		List<Exp> args = new LinkedList<Exp>();
		args.add(expstack.pop());
		
		stmstack.push(new STMEXP(currframe.externalCall("print", args)));
	}
	
	@Override
	public void outAManyStatement(AManyStatement node) {
		int size = node.getStatement().size();
		Stm stms = null;
		
		if(size > 0) {
			stms = stmstack.pop();
			
			for(int i = 1; i < size; i++)
				stms = new SEQ(stmstack.pop(), stms);
			
			stmstack.push(stms);
		}	
	}
}
