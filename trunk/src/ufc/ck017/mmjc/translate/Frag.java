package ufc.ck017.mmjc.translate;

import java.util.LinkedList;
import java.util.List;

import ufc.ck017.mmjc.translate.tree.SEQ;
import ufc.ck017.mmjc.translate.tree.Stm;
import ufc.ck017.mmjc.activationRecords.frame.Frame;

public class Frag {

	private Frame frame;
	private Stm body;
	
	public Frag(Frame frame, Stm stm) {
		this.frame = frame;
		LinkedList<Stm> list = new LinkedList<Stm>();
		list.add(stm);
		this.body = linearize(frame.procEntryExit1(list));
	}
	
	public Stm getBody() {
		return body;
	}
	
	public Frame getframe() {
		return frame;
	}
	
	private Stm linearize(List<Stm> stmlist) {
		Stm aux = stmlist.remove(0);
		
		for(Stm stm : stmlist)
			aux = new SEQ(aux, stm);
		
		return aux;
	}
}
