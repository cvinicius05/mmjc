package ufc.ck017.mmjc.translate.tree;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.LabelList;

public class JUMP extends Stm {

	public Exp exp;
	public LabelList targets;

	public JUMP(Exp e, LabelList t) {
		exp = e;
		targets = t;
	}

	public JUMP(Label target) {
		this(new NAME(target), new LabelList(target, null));
	}

	public ExpList kids() {
		return new ExpList(exp, null);
	}

	public Stm build(ExpList kids) {
		return new JUMP(kids.head, targets);
	}
}
