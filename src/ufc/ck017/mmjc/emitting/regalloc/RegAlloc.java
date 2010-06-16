package ufc.ck017.mmjc.emitting.regalloc;

import java.util.LinkedList;
import java.util.List;

import ufc.ck017.mmjc.activationRecords.frame.Frame;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.activationRecords.temp.TempMap;
import ufc.ck017.mmjc.emitting.graph.Node;
import ufc.ck017.mmjc.emitting.graph.flow.AssemFlowGraph;
import ufc.ck017.mmjc.emitting.graph.flow.FlowGraph;
import ufc.ck017.mmjc.emitting.graph.interference.InterferenceGraph;
import ufc.ck017.mmjc.emitting.graph.interference.Liveness;
import ufc.ck017.mmjc.instructionSelection.assem.Instr;

public class RegAlloc implements TempMap{

	private Frame frame = null;
	private List<Node> initial, spillWorklist;
	private List<Instr> activeMoves;
	private FlowGraph fgraph;
	private InterferenceGraph igraph;
	
	public RegAlloc(Frame f, List<Instr> ilist) {
		frame = f;
		fgraph = new AssemFlowGraph(ilist);
		igraph = new Liveness(fgraph);
		spillWorklist = new LinkedList<Node>();

		initial = new LinkedList<Node>();
		for(Node n : igraph.nodes()) {
			initial.add(n); 
		}
	}
	
	@Override
	public String tempMap(Temp t) {
		// TODO Auto-generated method stub
		return null;
	}

	public FlowGraph LivenessAnalysis(List<Instr> ilist) {
		return fgraph = new AssemFlowGraph(ilist);
	}
	
	public InterferenceGraph Build(FlowGraph fg) {
		return igraph = new Liveness(fg);
	}
	
	public void makeWorklist() {
		for(Node n : initial) {
			initial.remove(0);
			
			if(n.degree() >= frame.registers().length) spillWorklist.add(n);
			
		}
	}
}
