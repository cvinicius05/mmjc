package ufc.ck017.mmjc.instructionSelection.assem;

import java.util.SortedSet;
import java.util.TreeSet;

import ufc.ck017.mmjc.activationRecords.temp.Temp;

public class TempSet extends TreeSet<Temp> {
		
	private static final long serialVersionUID = -4119318652923707938L;
	
	public TempSet(SortedSet<Temp> s) {
		super(s);
	}

	public TempSet() {
		super();
	}
	
	public boolean union(TempSet t) {
		return addAll(t);
	}
	
	public boolean difference(TempSet t) {
		return removeAll(t);
	}
	
	public boolean intersection(TempSet t) {
		return retainAll(t);
	}
	
	public static TempSet union(TempSet t1, TempSet t2) {
		TempSet temp = new TempSet(t1);
		temp.addAll(t2);
		
		return temp;
		
	}
	
	public static TempSet difference(TempSet t1, TempSet t2) {
		TempSet temp = new TempSet(t1);
		temp.removeAll(t2);
		
		return temp;
		
	}
	
	public static TempSet intersection(TempSet t1, TempSet t2) {
		TempSet temp = new TempSet(t1);
		temp.retainAll(t2);
		
		return temp;
	}
}
