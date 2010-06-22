package ufc.ck017.mmjc.emitting.regalloc;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import ufc.ck017.mmjc.activationRecords.frame.Frame;
import ufc.ck017.mmjc.activationRecords.frame.JouetteFrame;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.emitting.graph.Node;
import ufc.ck017.mmjc.emitting.graph.flow.AssemFlowGraph;
import ufc.ck017.mmjc.emitting.graph.flow.FlowGraph;
import ufc.ck017.mmjc.emitting.graph.interference.InterferenceGraph;
import ufc.ck017.mmjc.emitting.graph.interference.Liveness;
import ufc.ck017.mmjc.instructionSelection.assem.AMOVE;
import ufc.ck017.mmjc.instructionSelection.assem.Instr;

public class RegAlloc{

	private static Frame frame = null;
	private FlowGraph fgraph;
	private InterferenceGraph igraph;

	private List<Node> spillWorklist, freezeWorklist,
		simplifyWorklist, coalescedNodes, spilledNodes;
	private List<Instr> activeMoves, workListMoves,
		coalescedMoves, constrainedMoves, frozenMoves;

	private Dictionary<Node, List<Instr>> moveList;
	private Dictionary<Node, Node> alias;
	public HashMap<Temp, String> colors;

	private int[] degree;
	private static Stack<Node> stake = new Stack<Node>();

	public RegAlloc(Frame f, List<Instr> ilist) {
		frame = f;
		fgraph = new AssemFlowGraph(ilist);
		igraph = new Liveness(fgraph);
		degree = new int[igraph.size()];

		spillWorklist = new LinkedList<Node>();
		freezeWorklist = new LinkedList<Node>();
		simplifyWorklist = new LinkedList<Node>();
		coalescedNodes = new LinkedList<Node>();
		spilledNodes = new LinkedList<Node>();

		activeMoves = new LinkedList<Instr>();
		workListMoves = new LinkedList<Instr>();
		coalescedMoves = new LinkedList<Instr>();
		constrainedMoves = new LinkedList<Instr>();
		frozenMoves = new LinkedList<Instr>();
		
		moveList = new Hashtable<Node, List<Instr>>();
		alias = new Hashtable<Node, Node>();
		
		Main(ilist);
		
		JouetteFrame.tempMap = colors;
	}

	@SuppressWarnings("unchecked")
	public FlowGraph LivenessAnalysis(List<Instr> ilist) {
		colors = (HashMap<Temp, String>) JouetteFrame.tempMap.clone();
		return fgraph = new AssemFlowGraph(ilist);
	}

	public InterferenceGraph Build(FlowGraph fg) {
		return new Liveness(fg);
	}

	public void MakeLists() {
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
	
	public boolean OK(Node v, Node u) {
		for(Node n : v.adj()) {
			if(!(degree[n.getMykey()] < frame.registers().length || isPrecolored(n) || n.adj(u)))
				return false;
		}
		return true;
	}
	
	public void Coalesce() {
		if(!workListMoves.isEmpty()) {
			Node u, v;
			
			AMOVE m = (AMOVE) workListMoves.get(0);
			Node x = igraph.tnode(m.dst);
			Node y = igraph.tnode(m.src);
			
			x = getAlias(x);
			y = getAlias(y);
			
			if(isPrecolored(y)) {
				u = y;
				v = x;
			} else {
				u = x;
				v = y;
			}
			workListMoves.remove(m);
			LinkedList<Node> list = new LinkedList<Node>(u.adj());
			list.addAll(v.adj());
			
			if(u == v) {
				coalescedMoves.add(m);
				addWorkList(u);
			} else if(isPrecolored(v) || u.adj(v)) {
				constrainedMoves.add(m);
				addWorkList(u);
				addWorkList(v);
			} else if((isPrecolored(u) && OK(v, u)) || (!isPrecolored(u) && conservative(list))) {
				coalescedMoves.add(m);
				combine(u, v);
				addWorkList(u);
			} else activeMoves.add(m);
		}
	}
	
	private void freezeMoves(Node n) {
		Node x, y, v;
		for(Instr m : NodeMoves(n)) {
			x = igraph.tnode(((AMOVE)m).dst);
			y = igraph.tnode(((AMOVE)m).src);
			
			if(getAlias(x) == getAlias(y))
				v = getAlias(x);
			else v = getAlias(y);
			
			activeMoves.remove(m);
			frozenMoves.add(m);
			if(freezeWorklist.contains(v) && NodeMoves(v).isEmpty()) {
				freezeWorklist.remove(v);
				simplifyWorklist.add(v);
			}
		}
	}
	
	public void Freeze() {
		if(!freezeWorklist.isEmpty()) {
			Node n = freezeWorklist.get(0);
			freezeWorklist.remove(0);
			simplifyWorklist.add(n);
			freezeMoves(n);
		}
	}
	
	public void SelectSpill() {
		if(!spillWorklist.isEmpty()) {
			Node less = spillWorklist.get(0);
			for(Node n : spillWorklist) {
				if(n.degree() > less.degree())
					less = n;
			}

			spillWorklist.remove(less);
			simplifyWorklist.add(less);
			freezeMoves(less);
		}
	}
	
	public void AssignColors() {
		Node n;
		LinkedList<String> okColors = new LinkedList<String>(colors.values());

		while(!stake.isEmpty()) {
			n = stake.pop();

			for(Node w : n.adj()) {
				Node v = getAlias(w);
				if(isPrecolored(v))
					okColors.remove(colors.get(v));
			}
			
			if(okColors.isEmpty()) spilledNodes.add(n);
			else colors.put(igraph.gtemp(n), okColors.getFirst());
			
			for(Node u : coalescedNodes) {
				colors.remove(u);
				colors.put(igraph.gtemp(u), colors.get(getAlias(u)));
			}
		}
	}
	
	private List<Temp> spills() {
		LinkedList<Temp> slist = new LinkedList<Temp>();
		for(Node n : spilledNodes) {
			slist.add(igraph.gtemp(n));
		}
		
		return slist;
	}
	
	public void RewriteProgram(List<Instr> ilist) {
		((JouetteFrame) frame).spill(ilist, spills());
		
		spilledNodes = new LinkedList<Node>();
		coalescedNodes = new LinkedList<Node>();
	}
	
	private List<Instr> Main(List<Instr> ilist) {
		do{
			fgraph = LivenessAnalysis(ilist);
			igraph = Build(fgraph);
			MakeLists();

			do{
				if(!simplifyWorklist.isEmpty()) Simplify();
				else if(!workListMoves.isEmpty()) Coalesce();
				else if(!freezeWorklist.isEmpty()) Freeze();
				else if(!spillWorklist.isEmpty()) SelectSpill();
				
			}while(!simplifyWorklist.isEmpty() || !workListMoves.isEmpty() ||
					!freezeWorklist.isEmpty() || !spillWorklist.isEmpty());

			AssignColors();
			if(!spilledNodes.isEmpty())
				RewriteProgram(ilist);
		}while(!spilledNodes.isEmpty());
		
		return ilist;
	}
}