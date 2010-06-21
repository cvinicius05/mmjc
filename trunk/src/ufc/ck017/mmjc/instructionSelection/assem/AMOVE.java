package ufc.ck017.mmjc.instructionSelection.assem;

import java.util.LinkedList;
import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.Temp;

public class AMOVE extends Instr {
	public Temp dst;	 
	public Temp src;
	private LinkedList<Temp> use = new LinkedList<Temp>();
	private LinkedList<Temp> def = new LinkedList<Temp>();

	public AMOVE(String a, Temp d, Temp s) {
		assem=a; dst=d; src=s;
		use.add(s);
		def.add(d);
	}
	
	public List<Temp> use() {
		return use;
	}
	public List<Temp> def() {
		return use;
	}
	public Targets jumps() {
		return null;
	}
}
