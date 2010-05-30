package ufc.ck017.mmjc.translate.assem;

public class InstrList {
	public Instr head;
	public InstrList tail;
	public InstrList(Instr h, InstrList t) {
		head=h; tail=t;
	}
}
