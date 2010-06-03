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
import ufc.ck017.mmjc.symbolTable.Class;

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
	private final Exp ZERO = new CONST(0);
	private final Exp ONE = new CONST(1);

	public List<Frag> getResult() {
		return frags;
	}

	private Exp getField(VarSymbol v) {
		int index = 0, temp = 0;
		Class t = table.getClass(currclass);

		do {
			temp = table.getClassFieldIndex(t.getName(), v);

			if(temp != -1) {
				index += temp;
				break;
			}

			index += t.getLocalVariables().size();
			t = t.getParent();
		} while(t != null);

		return new MEM(
				new BINOP(
						BINOP.PLUS,
						accesstable.get(VarSymbol.search("this")),
						new CONST(index*currframe.wordSize())
				));
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
	public void outAMainclass(AMainclass node) {
		Stm body = null;

		if(node.getStatement().size() > 0) {
			body = stmstack.pop();

			while(!stmstack.isEmpty())
				body = new SEQ(stmstack.pop(), body);
		} else body = new STMEXP(ZERO);

		currframe = currframe.newFrame(new Label("main"), null);
		frags.add(new Frag(currframe, body));
	}

	@Override
	public void inAMethod(AMethod node) {
		List<Boolean> formals = new LinkedList<Boolean>();
		int size = node.getParam().size();

		localDecl = null;
		accesstable = new Hashtable<VarSymbol, Exp>();
		currmethod = VarSymbol.search(node.getId().getText());

		for(int i=0; i <= size; i++) // por conta do *this*
			formals.add(false);

		currframe = currframe.newFrame(new Label(currclass+"$"+currmethod), formals);
		Iterator<Access> formalAccesses = currframe.formals.iterator();

		accesstable.put(VarSymbol.symbol("this"), formalAccesses.next().exp());

		for(PVar var : node.getParam())
			accesstable.put(VarSymbol.search(((AVar)var).getId().getText()), formalAccesses.next().exp());

		for(PVar var : node.getLocal()) {
			Exp nlexp = currframe.allocLocal(false).exp(new TEMP(currframe.FP()));

			if(localDecl == null)
				localDecl = new MOVE(nlexp, ZERO);
			else
				localDecl = new SEQ(new MOVE(nlexp, ZERO), localDecl);

			accesstable.put(VarSymbol.search(((AVar)var).getId().getText()), nlexp);
		}
	}

	@Override
	public void outAMethod(AMethod node) {
		Stm methodBody = null;
		Stm returnvalue = new MOVE(new TEMP(currframe.RV()), expstack.pop());

		if(node.getStatement().size() > 0) {
			methodBody = stmstack.pop();

			while(!stmstack.isEmpty())
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
		expstack.push(ZERO);
	}

	@Override
	public void outABtrueExpression(ABtrueExpression node) {
		expstack.push(ONE);
	}

	@Override
	public void outASelfExpression(ASelfExpression node) {
		expstack.push(accesstable.get(VarSymbol.symbol("this")));
	}

	@Override
	public void outANotExpression(ANotExpression node) {
		expstack.push(new BINOP(BINOP.XOR, expstack.pop(), ONE));
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
		TEMP value = new TEMP(new Temp());

		expstack.push(
				new ESEQ(
						new SEQ(
								new MOVE(
										value,
										ONE
								),
								new SEQ(
										new CJUMP(CJUMP.GT, expstack.pop(), temp, t, f),
										new SEQ(
												new LABEL(f),
												new SEQ(
														new MOVE(
																value,
																ZERO
														),
														new LABEL(t)
												)
										)
								)
						),
						value
				));
	}

	@Override
	public void outALthanExpression(ALthanExpression node) {
		Exp temp = expstack.pop();
		Label t = new Label();
		Label f = new Label();
		TEMP value = new TEMP(new Temp());

		expstack.push(
				new ESEQ(
						new SEQ(
								new MOVE(
										value,
										ONE
								),
								new SEQ(
										new CJUMP(CJUMP.LT, expstack.pop(), temp, t, f),
										new SEQ(
												new LABEL(f),
												new SEQ(
														new MOVE(
																value,
																ZERO
														),
														new LABEL(t)
												)
										)
								)
						),
						value
				));
	}

	@Override
	public void outANewobjExpression(ANewobjExpression node) {
		List<Exp> args = new LinkedList<Exp>();
		Temp newobj = new Temp();
		TEMP treeobj = new TEMP(newobj);
		Class c = table.getClass(node.getType());
		Stm init = null;
		int size = 0;

		do {
			size += c.getLocalVariables().size();
			c = c.getParent();
		} while(c != null);

		args.add(new CONST(size*currframe.wordSize()));

		init = new MOVE(treeobj, currframe.externalCall("malloc", args));

		for(int i = 0; i < size; i++)
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
							ZERO
					));

		expstack.push(new ESEQ(init, treeobj));
	}

	@Override
	public void outANewvecExpression(ANewvecExpression node) {
		List<Exp> args = new LinkedList<Exp>();
		Exp size = expstack.pop();
		Label condition = new Label();
		Label body = new Label();
		Label done = new Label();
		TEMP treearray = new TEMP(new Temp());
		TEMP tempsize = new TEMP(new Temp());
		TEMP i = new TEMP(new Temp());
		Stm init = null;

		args.add(i);
		init = new MOVE(tempsize, size);
		init = new SEQ(init, new MOVE(i, new BINOP(BINOP.PLUS, tempsize, ONE)));
		init = new SEQ(init, new MOVE(treearray, currframe.externalCall("initArray", args)));
		init = new SEQ(init, new MOVE(new MEM(treearray), tempsize));
		init = new SEQ(init, new JUMP(condition));
		init = new SEQ(init, new LABEL(body));
		init = new SEQ(init, new MOVE(i, new BINOP(BINOP.MINUS, i, ONE)));
		init = new SEQ(
				init,
				new MOVE(
						new MEM(
								new BINOP(
										BINOP.PLUS,
										treearray,
										new BINOP(
												BINOP.MUL,
												i,
												new CONST(currframe.wordSize())
										)
								)
						),
						ZERO
				));
		init = new SEQ(init, new LABEL(condition));
		init = new SEQ(init, new CJUMP(CJUMP.GT, i, ONE, body, done));
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

		args = new ExpList(expstack.pop(), args); // passando o endereço do *this*

		expstack.push(
				new CALL(
						new NAME(new Label(node.getObj().getType()+"$"+VarSymbol.search(node.getId().getText()))),
						args
				));
	}

	@Override
	public void outAVectorExpression(AVectorExpression node) {
		Exp index = expstack.pop();

		expstack.push(
				new MEM(
						new BINOP(
								BINOP.PLUS,
								expstack.pop(),
								new BINOP(
										BINOP.MUL,
										new BINOP(
												BINOP.PLUS,
												index,
												ONE
										),
										new CONST(currframe.wordSize())
								)
						)
				));
	}

	@Override
	public void outAVarExpression(AVarExpression node) {
		Exp var = accesstable.get(VarSymbol.search(node.getId().getText()));

		if(var != null)
			expstack.push(var);
		else
			expstack.push(getField(VarSymbol.search(node.getId().getText())));
	}

	@Override
	public void outAAtbStatement(AAtbStatement node) {
		VarSymbol vsymbol = VarSymbol.search(node.getId().getText());
		Exp var = accesstable.get(vsymbol);

		if(var == null) var = getField(vsymbol);
		stmstack.push(
				new MOVE(
						var,
						expstack.pop()
				));
	}

	@Override
	public void outAVatbStatement(AVatbStatement node) {
		VarSymbol vsymbol = VarSymbol.search(node.getId().getText());
		Exp value = expstack.pop();
		Exp var = accesstable.get(vsymbol);

		if(var == null) var = getField(vsymbol);
		stmstack.push(
				new MOVE(
						new MEM(
								new BINOP(
										BINOP.PLUS,
										var,
										new BINOP(
												BINOP.MUL,
												new BINOP(
														BINOP.PLUS,
														expstack.pop(),
														ONE
												),
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
								ONE,
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
				));
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
																ONE,
																body,
																done
														),
														new LABEL(done)
												)
										)
								)
						)
				));
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
