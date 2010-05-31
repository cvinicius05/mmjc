package ufc.ck017.mmjc.translate.tree;

import ufc.ck017.mmjc.activationRecords.temp.Label;

public class LABEL extends Stm { 

	public Label label;

	public LABEL(Label l) {
		label=l;
	}

	public ExpList kids() {
		return null;
	}

	public Stm build(ExpList kids) {
		return this;
	}
}
