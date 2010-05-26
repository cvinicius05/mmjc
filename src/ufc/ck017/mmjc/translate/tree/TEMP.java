package ufc.ck017.mmjc.translate.tree;

import ufc.ck017.mmjc.activationRecords.temp.Temp;

public class TEMP extends Exp {
	public Temp temp;
	public TEMP(Temp t) {temp=t;}
	public ExpList kids() {return null;}
	public Exp build(ExpList kids) {return this;}
}

