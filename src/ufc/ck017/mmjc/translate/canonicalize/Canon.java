package ufc.ck017.mmjc.translate.canonicalize;

import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.translate.tree.*;

class MoveCall extends Stm {
	TEMP dst;
	CALL src;

	MoveCall(TEMP d, CALL s) {
		dst=d; src=s;
	}

	public ExpList kids() {
		return src.kids();
	}

	public Stm build(ExpList kids) {
		return new MOVE(dst, src.build(kids));
	}
}	 

class ExpCall extends Stm {
	CALL call;

	ExpCall(CALL c) {
		call=c;
	}

	public ExpList kids() {
		return call.kids();
	}

	public Stm build(ExpList kids) {
		return new STMEXP(call.build(kids));
	}
}	 

class StmExpList {
	Stm stm;
	ExpList exps;

	StmExpList(Stm s, ExpList e) {
		stm=s; exps=e;
	}
}

public class Canon {

	static StmExpList nopNull = new StmExpList(new STMEXP(new CONST(0)), null);
	
	static boolean isNop(Stm a) {
		return a instanceof STMEXP && ((STMEXP)a).exp instanceof CONST;
	}

	static Stm seq(Stm a, Stm b) {
		if (isNop(a)) return b;
		else if (isNop(b)) return a;
		else return new SEQ(a,b);
	}

	static boolean commute(Stm a, Exp b) {
		return isNop(a)
		|| b instanceof NAME
		|| b instanceof CONST;
	}

	static Stm do_stm(Stm s) {
		if (s instanceof SEQ) {
			return seq(do_stm(((SEQ)s).left), do_stm(((SEQ)s).right));
		}

		else if (s instanceof MOVE) {
			if (((MOVE)s).dst instanceof TEMP && ((MOVE)s).src instanceof CALL) 
				return reorder_stm(new MoveCall((TEMP)((MOVE)s).dst, (CALL)((MOVE)s).src));
			else if (((MOVE)s).dst instanceof ESEQ)
				return do_stm(new SEQ(((ESEQ)((MOVE)s).dst).stm, new MOVE(((ESEQ)((MOVE)s).dst).exp, ((MOVE)s).src)));
			else return reorder_stm(s);
		}

		else if (s instanceof STMEXP) {
			if (((STMEXP)s).exp instanceof CALL)
				return reorder_stm(new ExpCall((CALL)((STMEXP)s).exp));
			else return reorder_stm(s);
		}

		else return reorder_stm(s);
	}

	static Stm reorder_stm(Stm s) {
		StmExpList x = reorder(s.kids());
		return seq(x.stm, s.build(x.exps));
	}

	static ESEQ do_exp (Exp e) {
		if (e instanceof ESEQ) {
			Stm stms = do_stm(((ESEQ)e).stm);
			ESEQ b = do_exp(((ESEQ)e).exp);
			return new ESEQ(seq(stms, b.stm), b.exp);
		}
		else return reorder_exp(e);
	}

	static ESEQ reorder_exp (Exp e) {
		StmExpList x = reorder(e.kids());
		return new ESEQ(x.stm, e.build(x.exps));
	}

	static StmExpList reorder(ExpList exps) {
		if (exps==null) return nopNull;
		else {
			Exp a = exps.head;

			if (a instanceof CALL) {
				Temp t = new Temp();
				Exp e = new ESEQ(new MOVE(new TEMP(t), a), new TEMP(t));
				return reorder(new ExpList(e, exps.tail));
			} else {
				ESEQ aa = do_exp(a);
				StmExpList bb = reorder(exps.tail);

				if (commute(bb.stm, aa.exp))
					return new StmExpList(seq(aa.stm, bb.stm), new ExpList(aa.exp, bb.exps));
				else {
					Temp t = new Temp();
					return new StmExpList(
										seq(aa.stm, 
											seq(new MOVE(
														new TEMP(t),
														aa.exp),
												bb.stm)
											),
											new ExpList(new TEMP(t), bb.exps)
										);
				}
			}
		}
	}

	static StmList linear(Stm s, StmList l) {
		if (s instanceof SEQ) {
			return linear(((SEQ)s).left, linear(((SEQ)s).right, l));
		}
		else return new StmList(s, l);
	}

	public static StmList linearize(Stm s) {
		return linear(do_stm(s), null);
	}
}
