package ufc.ck017.mmjc.activationRecords.jouette;import ufc.ck017.mmjc.activationRecords.frame.Access;import ufc.ck017.mmjc.translate.tree.BINOP;import ufc.ck017.mmjc.translate.tree.CONST;import ufc.ck017.mmjc.translate.tree.Exp;import ufc.ck017.mmjc.translate.tree.MEM;public class InFrame extends Access {	int offset;	Exp access;		InFrame(int o) {		offset = o;	}	public Exp exp(Exp fp) {		access = new MEM(new BINOP(BINOP.PLUS, fp, new CONST(offset)));		return access;	}		public Exp exp() {		return access;	}	public String toString() {		return new Integer(this.offset).toString();	}}