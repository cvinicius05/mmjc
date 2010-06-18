package ufc.ck017.mmjc.emitting.regalloc;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import ufc.ck017.mmjc.activationRecords.frame.Frame;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.activationRecords.temp.TempMap;
import ufc.ck017.mmjc.emitting.graph.Node;
import ufc.ck017.mmjc.emitting.graph.flow.AssemFlowGraph;
import ufc.ck017.mmjc.emitting.graph.flow.FlowGraph;
import ufc.ck017.mmjc.emitting.graph.interference.InterferenceGraph;
import ufc.ck017.mmjc.emitting.graph.interference.Liveness;
import ufc.ck017.mmjc.instructionSelection.assem.AMOVE;
import ufc.ck017.mmjc.instructionSelection.assem.Instr;

public class RegAlloc implements TempMap{

	private static Frame frame = null;
	private FlowGraph fgraph;

	private List<Node> spillWorklist, freezeWorklist,
		simplifyWorklist, coalescedNodes, spilledNodes;
	private List<Instr> activeMoves, workListMoves;
	private Dictionary<Node, List<Instr>> moveList;
	private Dictionary<Node, Node> alias;
	private int[] degree;
	private static Stack<Node> stake = new Stack<Node>();

	public RegAlloc(Frame f, List<Instr> ilist) {
		frame = f;
		fgraph = new AssemFlowGraph(ilist);

		spillWorklist = new LinkedList<Node>();
		freezeWorklist = new LinkedList<Node>();
		simplifyWorklist = new LinkedList<Node>();
		workListMoves = new LinkedList<Instr>();
		activeMoves = new LinkedList<Instr>();
		moveList = new Hashtable<Node, List<Instr>>();
		alias = new Hashtable<Node, Node>();
		coalescedNodes = new LinkedList<Node>();
		spilledNodes = new LinkedList<Node>();
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
		return new Liveness(fg);
	}

	public void MakeLists() {
		InterferenceGraph igraph = new Liveness(fgraph);
		degree = new int[igraph.size()];

		for(Node n : fgraph.moves()) {
			AMOVE m = (AMOVE)fgraph.getNodeInfo(n);
			workListMoves.add(m);

			moveList.get(igraph.tnode(m.dst)).add(m);
			moveList.get(igraph.tnode(m.src)).add(m);
		}

		int i=0;
		for(Node n : igraph.nodes()) {
			degree[i] = n.degree();
			i++;
			if(n.degree() >= frame.registers().length) spillWorklist.add(n);
			else if(moveRelated(n)) freezeWorklist.add(n);
			else simplifyWorklist.add(n);
		}
	}

	// moveList[n] ∩ (activeMoves ∪ worklistMoves)
	private List<Instr> NodeMoves(Node n) {
		List<Instr> l = new LinkedList<Instr>(activeMoves);

		l.addAll(workListMoves);
		l.retainAll(moveList.get(n));

		return l;
	}

	private boolean moveRelated(Node n) {
		if(!NodeMoves(n).isEmpty()) return true;
		return false;
	}
	
	private void enableMoves(List<Node> list) {
		List<Instr> l;
		for(Node n : list) {
			l = NodeMoves(n);
			for(Instr i : l) {
				if(activeMoves.remove(i))
					workListMoves.add(i);
			}
		}
	}
	
	private void decrementDegree(Node n) {
		int d = degree[n.getMykey()];
		degree[n.getMykey()]--;
		if(d <= frame.registers().length) {
			List<Node> list = new LinkedList<Node>(n.adj());
			list.add(n);
			enableMoves(list);
			spillWorklist.remove(n);

			if(moveRelated(n))
				freezeWorklist.add(n);
			else
				simplifyWorklist.add(n);
		}
	}
	
	public void Simplify() {
		if(simplifyWorklist.isEmpty()) return;
		
		Node n = simplifyWorklist.remove(0);
		stake.push(n);
		for(Node node : n.adj()) {
			decrementDegree(node);
		}
	}
	
	private boolean isPrecolored(Node n) {
		return frame.tempMap(((Liveness) n.getMyGraph()).gtemp(n)) != null;
	}
	
	private void addWorkList(Node n) {
		if(!isPrecolored(n) && !moveRelated(n) && degree[n.getMykey()] < frame.registers().length) {
			freezeWorklist.remove(n);
			simplifyWorklist.add(n);
		}
	}
	
	private boolean conservative(List<Node> nodes) {
		int k = 0;
		for(Node n : nodes) {
			if(degree[n.getMykey()] >= frame.registers().length)
				k++;
		}
		
		return k < frame.registers().length;
	}
	
	private Node getAlias(Node n) {
		if(coalescedNodes.contains(n))
			return getAlias(alias.get(n));
		else return n;
	}
	
	private void combine(Node u, Node v) {
		if(freezeWorklist.contains(v))
			freezeWorklist.remove(v);
		else spillWorklist.remove(v);
		
		coalescedNodes.add(v);
		alias.put(v, u);
		
		// moveList[u] ← moveList[u] ∪ moveList[v]
		moveList.get(u).addAll(moveList.get(v));
		LinkedList<Node> list = new LinkedList<Node>();
		list.add(v);
		enableMoves(list);
		
		for(Node n : v.adj()) {
			v.getMyGraph().addEdge(v, n);
			decrementDegree(n);
		}
		
		if(degree[u.getMykey()] >= frame.registers().length && freezeWorklist.contains(u)) {
			freezeWorklist.remove(u);
			spillWorklist.add(u);
		}
	}
	
	public void Coalesce() {
		
	}
}
