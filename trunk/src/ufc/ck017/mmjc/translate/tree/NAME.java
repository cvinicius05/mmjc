package ufc.ck017.mmjc.translate.tree;

import ufc.ck017.mmjc.activationRecords.temp.Label;



public class NAME extends Exp {
	public Label label;
	public NAME(Label l) {label=l;}
	public ExpList kids() {return null;}
	public Exp build(ExpList kids) {return this;}
}

