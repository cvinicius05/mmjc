package ufc.ck017.mmjc.activationRecords.util;

public class Exp {

	private ufc.ck017.mmjc.translate.tree.Exp exp;

	public Exp(ufc.ck017.mmjc.translate.tree.Exp exp) {
		this.exp = exp;
	}

	public ufc.ck017.mmjc.translate.tree.Exp unEx(){
		return exp;
	}
}
