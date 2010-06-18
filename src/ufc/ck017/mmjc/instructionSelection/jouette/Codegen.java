package ufc.ck017.mmjc.instructionSelection.jouette;

import java.util.LinkedList;
import java.util.List;

import ufc.ck017.mmjc.activationRecords.frame.Frame;
import ufc.ck017.mmjc.activationRecords.frame.JouetteFrame;
import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.LabelList;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.instructionSelection.assem.*;
import ufc.ck017.mmjc.translate.tree.*;

public class Codegen {

	private Frame frame;
	private LinkedList<Instr> ilist = null;

	public Codegen(Frame frame) {
		this.frame = frame;
	}

	private List<Temp> L(Temp... temps) {
		LinkedList<Temp> list = new LinkedList<Temp>();
		
		for(Temp t : temps)
			list.add(t);
		
		return list;
	}

	private void emit(Instr inst) {
		ilist.add(inst);
	}

	public List<Instr> codegen(Stm s) {
		ilist = new LinkedList<Instr>();
		
		munchStm(s);
		
		return ilist;
	}

	private void munchStm(Stm stm) {
		if (stm instanceof MOVE) munchMove(((MOVE)stm).dst, ((MOVE)stm).src);
		else if(stm instanceof ufc.ck017.mmjc.translate.tree.LABEL) munchLABEL(((ufc.ck017.mmjc.translate.tree.LABEL)stm).label);
		else if(stm instanceof JUMP) munchJUMP(((JUMP)stm).exp, ((JUMP)stm).targets);
		else if(stm instanceof CJUMP) munchCJUMP(((CJUMP)stm).relop, ((CJUMP)stm).left, ((CJUMP)stm).right, ((CJUMP)stm).iftrue, ((CJUMP)stm).iffalse);
		else if(stm instanceof SEQ) munchSEQ(((SEQ)stm).left, ((SEQ)stm).right);
		else if(stm instanceof STMEXP) munchExp(((STMEXP)stm).exp);
	}

	private void munchCJUMP(int relop, Exp left, Exp right, Label iftrue, Label iffalse) {
		Temp l = munchExp(left);
		Temp r = munchExp(right);
		Label ltrue = iftrue;
		Label lfalse = iffalse;
		
		switch (relop) {
			case CJUMP.GE:
				emit(new OPER("BRANCHGE if `s0 >= `s1 goto `j0\n", null, L(l, r), new LabelList(ltrue, null)));
				emit(new OPER("JUMP goto `j0\n", null, null, new LabelList(lfalse, null)));
				break;
	
			case CJUMP.GT:
				emit(new OPER("BRANCHLT if `s1 < `s0 goto `j0\n", null, L(l, r), new LabelList(ltrue, null)));
				emit(new OPER("JUMP goto `j0\n", null, null, new LabelList(lfalse, null)));
				break;
	
			case CJUMP.EQ:
				emit(new OPER("BRANCHEQ if `s0 = `s1 goto `j0\n", null, L(l, r), new LabelList(ltrue, null)));
				emit(new OPER("JUMP goto `j0\n", null, null, new LabelList(lfalse, null)));
				break;
				
			case CJUMP.NE:
				emit(new OPER("BRANCHNE if `s0 != `s1 goto `j0\n", null, L(l, r), new LabelList(ltrue, null)));
				emit(new OPER("JUMP goto `j0\n", null, null, new LabelList(lfalse, null)));
				break;
				
			case CJUMP.LE:
				emit(new OPER("BRANCHGE if `s1 >= `s0 goto `j0\n", null, L(l, r), new LabelList(ltrue, null)));
				emit(new OPER("JUMP goto `j0\n", null, null, new LabelList(lfalse, null)));
				break;
				
			case CJUMP.LT:
				emit(new OPER("BRANCHLT if `s0 < `s1 goto `j0\n", null, L(l, r), new LabelList(ltrue, null)));
				emit(new OPER("JUMP goto `j0\n", null, null, new LabelList(lfalse, null)));
				break;
			default:
				break;
		}
	}

	private void munchMove(Exp dst, Exp src) {
		if(dst instanceof MEM) munchMove((MEM)dst, src);
		else if(dst instanceof TEMP) munchMove((TEMP)dst, src);
	}

	private void munchMove(TEMP dst, Exp src) {
		// MOVE(TEMP(t1), e)
		emit(new AMOVE("ADDI `d0 <- `s0 + 0\n", munchExp(dst), munchExp(src)));
	}

	private void munchMove(MEM dst, Exp src) {
		// MOVE(MEM(BINOP(PLUS, e1, CONST(i))), e2)
		if (dst.exp instanceof BINOP && ((BINOP)dst.exp).binop == BINOP.PLUS
				&& ((BINOP)dst.exp).right instanceof CONST) {

			emit(new OPER("STORE M[`s0 +" + ((CONST)((BINOP)dst.exp).right).value + "] <- `s1\n",
					null, L(munchExp(((BINOP)dst.exp).left), munchExp(src))));
		}

		// MOVE(MEM(BINOP(PLUS, CONST(i), e1)), e2)
		else if (dst.exp instanceof BINOP && ((BINOP)dst.exp).binop == BINOP.PLUS
				&& ((BINOP)dst.exp).left instanceof CONST) {

			emit(new OPER("STORE M[`s0 +" + ((CONST)((BINOP)dst.exp).left).value + "] <- `s1\n",
					null, L(munchExp(((BINOP)dst.exp).right), munchExp(src))));
		}

		// MOVE(MEM(e1), MEM(e2))
		else if (src instanceof MEM) {
			emit(new OPER("MOVEM M[`s0] <- M[`s1]\n", null, L(munchExp(dst.exp), munchExp(((MEM)src).exp))));
		}

		// MOVE(MEM(CONST(i)), e2))
		else if(dst.exp instanceof CONST) {
			emit(new OPER("STORE M[`s0 +" + ((CONST)dst.exp).value + "] <- `s1\n", null, L(JouetteFrame.R0(), munchExp(src))));
		}

		// MOVE(MEM(e1), e2)
		else {
			emit(new OPER("STORE M[`s0] <- `s1\n", null, L(munchExp(dst.exp), munchExp(src))));
		}
	}

	private void munchSEQ(Stm left, Stm right) {
		munchStm(left);
		munchStm(right);
	}

	private void munchLABEL(Label label) {
		emit(new ALABEL(label.toString() + ":\n", label));
	}

	private void munchJUMP(Exp exp, LabelList targets) {
		emit(new OPER("JUMP goto `j0\n", null, null, targets));
	}

	public Temp munchExp(Exp e) {
		if(e instanceof MEM) return munchMEM(((MEM)e).exp);
		else if(e instanceof BINOP) return munchBINOP(((BINOP)e).binop, ((BINOP)e).left, ((BINOP)e).right);
		else if(e instanceof CONST) return munchCONST(((CONST)e).value);
		else if(e instanceof TEMP) return munchTEMP(((TEMP)e).temp);
		else if(e instanceof NAME) return munchNAME(((NAME)e).label);
		else if(e instanceof CALL) return munchCALL((CALL)e);
		else return null;
	}

	private Temp munchCALL(CALL e) {
		Temp r = munchExp(e.func);
		List<Temp> l = munchArgs(e.args);
		l.add(0, r);
		
		emit(new OPER("CALL `s0\n", JouetteFrame.calldefs(), l));
		return frame.RV();
	}

	private List<Temp> munchArgs(ExpList args) {
		Temp[] argregs = JouetteFrame.argRegs();
		LinkedList<Temp> list = new LinkedList<Temp>();
		int i = 0;
		
		for(Exp e : args) {
			if(i >= argregs.length) {
				emit(new OPER("STORE M[`s0+" + i*frame.wordSize() + "] <- `s1\n", null, L(frame.FP(), munchExp(e))));
			} else {
				emit(new OPER("STORE `d0 <- `s0\n", L(argregs[i]), L(munchExp(e))));
				list.addLast(argregs[i]);
			}
			i++;
		}
		
		return list;
	}

	private Temp munchNAME(Label label) {
		Temp r = new Temp();
		emit(new OPER("LOAD `d0 <- `j0\n", L(r), null, new LabelList(label, null)));
		return r;
	}

	private Temp munchMEM(Exp exp) {
		Temp r = new Temp();

		// MEM(BINOP(PLUS, e1, CONST(i)))
		if(exp instanceof BINOP && ((BINOP)exp).binop == BINOP.PLUS && ((BINOP)exp).right instanceof CONST) {
			emit(new OPER("LOAD `d0 <- M[`s0 + " + ((CONST)((BINOP)exp).right).value + "]\n",
					L(r), L(munchExp(((BINOP)exp).left))));
		}

		// MEM(BINOP(PLUS, CONST(i), e1))
		else if(exp instanceof BINOP && ((BINOP)exp).binop == BINOP.PLUS && ((BINOP)exp).left instanceof CONST) {
			emit(new OPER("LOAD `d0 <- M[`s0 + " + ((CONST)((BINOP)exp).left).value + "]\n",
					L(r), L(munchExp(((BINOP)exp).right))));
		}

		// MEM(CONST (i))
		else if(exp instanceof CONST) {
			emit(new OPER("LOAD `d0 <- M[`s0+" + ((CONST)exp).value + "]\n", L(r), L(JouetteFrame.R0())));
		}

		// MEM(e1)
		else {
			emit(new OPER("LOAD `d0 <- M[`s0+`s1]\n", L(r), L(munchExp(exp), JouetteFrame.R0())));
		}
		return r;
	}

	private Temp munchBINOP(int binop, Exp left, Exp right) {
		Temp r = new Temp();

		// BINOP(PLUS, e1, CONST(i))
		if(binop == BINOP.PLUS && right instanceof CONST) {
			emit(new OPER("ADDI `d0 <- `s0 + " + ((CONST)right).value + "\n", L(r), L(munchExp(left))));
		}

		// BINOP(PLUS, CONST(i), e1)
		else if(binop == BINOP.PLUS && left instanceof CONST) {
			emit(new OPER("ADDI `d0 <- `s0 + " + ((CONST)left).value + "\n", L(r), L(munchExp(right))));
		}

		// BINOP(MINUS, e1, CONST(i))
		if(binop == BINOP.MINUS && right instanceof CONST) {
			emit(new OPER("SUBI `d0 <- `s0 - " + ((CONST)right).value + "\n", L(r), L(munchExp(left))));
		}

		// BINOP(PLUS, e1, e2)
		else if(binop == BINOP.PLUS) {
			emit(new OPER("ADD `d0 <- `s0 + `s1\n", L(r), L(munchExp(left), munchExp(right))));
		}

		// BINOP(MUL, e1, e2)
		else if(binop == BINOP.MUL) {
			emit(new OPER("MUL `d0 <- `s0 * `s1\n", L(r), L(munchExp(left), munchExp(right))));
		}

		// BINOP(DIV, e1, e2)
		else if(binop == BINOP.DIV) {
			emit(new OPER("DIV `d0 <- `s0 / `s1\n", L(r), L(munchExp(left), munchExp(right))));
		}

		// BINOP(MINUS, e1, e2)
		else if(binop == BINOP.MINUS) {
			emit(new OPER("SUB `d0 <- `s0 - `s1\n", L(r), L(munchExp(left), munchExp(right))));
		}

		return r;
	}

	private Temp munchCONST(int value) {
		Temp r = new Temp();
		// CONST(i)
		emit(new OPER("ADDI `d0 <- `s0 + " + value + "\n", L(r), L(JouetteFrame.R0())));
		return r;
	}

	private Temp munchTEMP(Temp temp) {
		return temp;
	}

}
