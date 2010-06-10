package ufc.ck017.mmjc.instructionSelection.assem;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.LabelList;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.emitting.graph.FlowGraph;
import ufc.ck017.mmjc.emitting.graph.Node;

public class AssemFlowGraph extends FlowGraph {
	Hashtable<Label, Node> labeled;
	Hashtable<Node, TempSet> defs;
	Hashtable<Node, TempSet> uses;
	LinkedList<Node> moves;

	public AssemFlowGraph(List<Instr> instrs) {
		labeled = new Hashtable<Label, Node>((int)(instrs.size()*0.4));
		uses = new Hashtable<Node, TempSet>(instrs.size());
		defs = new Hashtable<Node, TempSet>(instrs.size());
		moves = new LinkedList<Node>();
		
		for(Instr i : instrs) {
			Node n = newNode(i);
			TempSet ts_use = new TempSet();
			TempSet ts_def = new TempSet();
			
			if(i instanceof ALABEL) labeled.put(((ALABEL)i).label, n);
			else if(i instanceof AMOVE) moves.add(n);
			
			uses.put(n, ts_use);
			defs.put(n, ts_def);
			
			for(Temp t : i.use())
				ts_use.add(t);
			
			for(Temp t : i.def())
				ts_def.add(t);
		}
		
		for(Node n : this) {
			Instr i = (Instr)infotable.get(n);
			
			if(i.jumps() != null) {
				LabelList list = i.jumps().labels;
				for(Label l : list)
					addEdge(n, labeled.get(l));
			}
		}
	}
	
	@Override
	public List<Temp> def(Node node) {
		return defs.get(node).getList();
	}

	@Override
	public boolean isMove(Node node) {
		return false;
	}

	@Override
	public List<Temp> use(Node node) {
		return uses.get(node).getList();
	}

}
