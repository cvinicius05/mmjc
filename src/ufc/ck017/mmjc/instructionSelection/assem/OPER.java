package ufc.ck017.mmjc.instructionSelection.assem;

import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.LabelList;
import ufc.ck017.mmjc.activationRecords.temp.Temp;

public class OPER extends Instr {
	public List<Temp> dst; 
	public List<Temp> src;
	public Targets jump;

	public OPER(String a, List<Temp> d, List<Temp> s, LabelList j) {
		assem=a; dst=d; src=s; jump=new Targets(j);
	}
	public OPER(String a, List<Temp> d, List<Temp> s) {
		assem=a; dst=d; src=s; jump=null;
	}

	public List<Temp> use() {return src;}
	public List<Temp> def() {return dst;}
	public Targets jumps() {return jump;}

}
