package ufc.ck017.mmjc.instructionSelection.assem;

import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.Temp;

public class ALABEL extends Instr {
	public Label label;

	public ALABEL(String a, Label l) {
		assem=a; label=l;
	}

	public List<Temp> use() {return null;}
	public List<Temp> def() {return null;}
	public Targets jumps()	{return null;}
}
