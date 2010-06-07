import ufc.ck017.mmjc.translate.tree.BINOP;
import ufc.ck017.mmjc.translate.tree.CALL;
import ufc.ck017.mmjc.translate.tree.CJUMP;
import ufc.ck017.mmjc.translate.tree.CONST;
import ufc.ck017.mmjc.translate.tree.ESEQ;
import ufc.ck017.mmjc.translate.tree.Exp;
import ufc.ck017.mmjc.translate.tree.ExpList;
import ufc.ck017.mmjc.translate.tree.JUMP;
import ufc.ck017.mmjc.translate.tree.LABEL;
import ufc.ck017.mmjc.translate.tree.MEM;
import ufc.ck017.mmjc.translate.tree.MOVE;
import ufc.ck017.mmjc.translate.tree.NAME;
import ufc.ck017.mmjc.translate.tree.SEQ;
import ufc.ck017.mmjc.translate.tree.STMEXP;
import ufc.ck017.mmjc.translate.tree.Stm;
import ufc.ck017.mmjc.translate.tree.TEMP;


	
	private static void verifynull(Stm s) {
		if(s instanceof MOVE) {
			MOVE m = (MOVE)s;
			if(m.dst == null || m.src == null) throw new Error("MOVE com null");
			else {
				verifynull(m.dst);
				verifynull(m.src);
			}
		} else if(s instanceof STMEXP) {
			STMEXP e = (STMEXP)s;
			if(e.exp == null) throw new Error("STMEXP com null");
			else verifynull(e.exp);
		} else if(s instanceof JUMP) {
			JUMP j = (JUMP)s;
			if(j.exp == null || j.targets == null) throw new Error("JUMP com null");
			else verifynull(j.exp);
		} else if(s instanceof CJUMP) {
			CJUMP c = (CJUMP)s;
			if(c.iffalse == null || c.iftrue == null || c.left == null || c.right == null) throw new Error("CJUMP com null");
			else {
				verifynull(c.right);
				verifynull(c.left);
			}
		} else if(s instanceof SEQ) {
			SEQ q = (SEQ)s;
			if(q.right == null || q.left == null) throw new Error("SEQ com null");
			else {
				verifynull(q.left);
				verifynull(q.right);
			}
		} else if(s instanceof LABEL) {
			LABEL l = (LABEL)s;
			if(l.label == null) throw new Error("LABEL com null");
		} else throw new Error("no Stm desconhecido: "+s);
	}
	
	private static void verifynull(Exp e) {
		if(e instanceof CONST);
		else if(e instanceof NAME) {
			NAME n = (NAME)e;
			if(n.label == null) throw new Error("NAME com null");
		} else if(e instanceof TEMP) {
			TEMP t = (TEMP)e;
			if(t.temp == null) throw new Error("TEMP com null");
		} else if(e instanceof BINOP) {
			BINOP b = (BINOP)e;
			if(b.left == null || b.right == null) throw new Error("BINOP com null");
			else {
				verifynull(b.left);
				verifynull(b.right);
			}
		} else if(e instanceof MEM) {
			MEM m = (MEM)e;
			if(m.exp == null) throw new Error("MEM com null");
			else verifynull(m.exp);
		} else if(e instanceof CALL) {
			CALL c = (CALL)e;
			if(c.args == null || c.func == null) throw new Error("CALL com null");
			else {
				ExpList el = c.args;
				
				while(el != null) {
					if(el.head == null) throw new Error("CALL com null");
					else verifynull(el.head);
					el = el.tail;
				}
				verifynull(c.func);
			}
		} else if(e instanceof ESEQ) {
			ESEQ q = (ESEQ)e;
			if(q.stm == null || q.exp == null) throw new Error("ESEQ com null");
			else {
				verifynull(q.exp);
				verifynull(q.stm);
			}
		} else throw new Error("no Exp desconhecido: "+e);
		
	}