package ufc.ck017.mmjc.activationRecords.frame;

import ufc.ck017.mmjc.translate.tree.Exp;

public abstract class Access {
	public abstract Exp exp(Exp framePtr);
	public abstract Exp exp();
}
