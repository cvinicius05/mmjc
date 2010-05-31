package ufc.ck017.mmjc.translate;

import java.util.LinkedList;
import java.util.List;

import ufc.ck017.mmjc.translate.tree.Stm;
import ufc.ck017.mmjc.activationRecords.frame.Frame;

public abstract class Frag {

	private Frame frame;
	private List<Stm> body;
	
	public Frag(Frame frame, Stm stm) {
		this.frame = frame;
		LinkedList<Stm> list = new LinkedList<Stm>();
		list.add(stm);
		this.body = frame.procEntryExit1(list);
	}
	
	public List<Stm> getBody() {
		return body;
	}
	
	public Frame getframe() {
		return frame;
	}
	
	public void setBody(List<Stm> linearize) {
		body = linearize;
	}
}
