package ufc.ck017.mmjc.instructionSelection.jouette;

import ufc.ck017.mmjc.activationRecords.frame.Frame;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.activationRecords.temp.TempList;
import ufc.ck017.mmjc.instructionSelection.assem.Instr;
import ufc.ck017.mmjc.instructionSelection.assem.InstrList;
import ufc.ck017.mmjc.instructionSelection.assem.OPER;
import ufc.ck017.mmjc.translate.tree.*;

public class Codegen {

	private Frame frame;
	private InstrList ilist = null, last = null;

	public Codegen(Frame frame) {
		this.frame = frame;
	}

	private TempList L(Temp h, TempList t) {
		return new TempList(h,t);
	}

	private void emit(Instr inst) {
		if(last != null)
			last = last.tail = new InstrList(inst, null);
		else last = ilist = new InstrList(inst, null);
	}

	public Temp munchExp(Exp e) {
		return null;
	}

	public InstrList codegen(Stm s) {
		InstrList l;
		munchStm(s);
		l = ilist;
		ilist = last = null;
		return l;
	}

	private void munchStm(Stm stm) {
		if (stm instanceof MOVE) munchMove(((MOVE)stm).dst, ((MOVE)stm).src);
		else if(stm instanceof LABEL) ;
		else if(stm instanceof JUMP) ;
		else if(stm instanceof CJUMP) ;
		else if(stm instanceof SEQ) ;
	}
	
	private void munchMove(Exp dst, Exp src) {
		if(dst instanceof MEM) munchMove((MEM)dst, src);
		else if(dst instanceof TEMP) munchMove((TEMP)dst, src);
	}
	
	void munchMove(TEMP dst, Exp src) {
		// MOVE(TEMP(t1), e)
		/*Temp[] list = new Temp[]{munchExp(dst), munchExp(src)};
		emit(new OPER("STORE M[‘s0] <- ‘s1\n", null, list));*/

		emit(new OPER("ADD   ‘d0 <- ‘s0 + r0\n", L(munchExp(dst), null), L(munchExp(src), null)));
	}

	void munchMove(MEM dst, Exp src) {
		// MOVE(MEM(BINOP(PLUS, e1, CONST(i))), e2)
		if (dst.exp instanceof BINOP && ((BINOP)dst.exp).binop == BINOP.PLUS
				&& ((BINOP)dst.exp).right instanceof CONST) {

			/*Temp[] list = new Temp[]{munchExp(((BINOP)dst.exp).left), munchExp(src)};
			emit(new OPER("STORE M[‘s0+" + ((BINOP)dst.exp).right + "] <- ‘s1\n", null, list));*/
			
			emit(new OPER("STORE M[‘s0+" + ((BINOP)dst.exp).right + "] <- ‘s1\n",
		            null, L(munchExp(((BINOP)dst.exp).left), L(munchExp(src), null))));
			
		}

		// MOVE(MEM(BINOP(PLUS, CONST(i), e1)), e2)
		else if (dst.exp instanceof BINOP && ((BINOP)dst.exp).binop == BINOP.PLUS
		           && ((BINOP)dst.exp).left instanceof CONST) {

			/*Temp[] list = new Temp[]{munchExp(((BINOP)dst.exp).right), munchExp(src)};
			emit(new OPER("STORE M[‘s0+" + ((BINOP)dst.exp).left + "] <- ‘s1\n", null, list));*/

			emit(new OPER("STORE M[‘s0+" + ((BINOP)dst.exp).left + "] <- ‘s1\n",
		            null, L(munchExp(((BINOP)dst.exp).right), L(munchExp(src), null))));
		}
		
		// MOVE(MEM(e1), MEM(e2))
		else if (src instanceof MEM) {
			/*Temp[] list = new Temp[]{munchExp(dst.exp), munchExp(((MEM)src).exp)};
			emit(new OPER("MOVE M[‘s0] <- M[‘s1]\n", null, list));*/
			
			emit(new OPER("MOVE M[‘s0] <- M[‘s1]\n",
		            null, L(munchExp(dst.exp), L(munchExp(((MEM)src).exp), null))));
		}

		// MOVE(MEM(CONST(i), e2))
		else if(dst.exp instanceof CONST) {
			/*Temp[] list = new Temp[]{munchExp((CONST)dst.exp), munchExp(src)};
			emit(new OPER("STORE M[r0+" + ((CONST)dst.exp) + "] <- ‘s0\n", null, list));*/
			
			emit(new OPER("STORE M[r0+" + (CONST)dst.exp + "] <- ‘s0\n",
		            null, L(munchExp(src), null)));
		}

		// MOVE(MEM(e1), e2)
		else {
			/*Temp[] list = new Temp[]{munchExp(dst.exp), munchExp(src)};			
			emit(new OPER("STORE M[‘s0] <- ‘s1\n", null, list));*/
			
			emit(new OPER("STORE M[‘s0] <- ‘s1\n",
		            null, L(munchExp(dst.exp), L(munchExp(src), null))));
		}
	}

}
