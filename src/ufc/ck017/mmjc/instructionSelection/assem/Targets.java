package ufc.ck017.mmjc.instructionSelection.assem;

import java.util.Iterator;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.LabelList;

public class Targets implements Iterable<Label>{
	public LabelList labels;
	public Targets(LabelList l) {labels=l;}
	@Override
	public Iterator<Label> iterator() {
		return labels.iterator();
	}
	
}
