package ufc.ck017.mmjc.instructionSelection.assem;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.TempList;

public class LABEL extends Instr {
	public Label label;

	public LABEL(String a, Label l) {
		assem=a; label=l;
	}

	public TempList use() {return null;}
	public TempList def() {return null;}
	public Targets jumps()	{return null;}
}
