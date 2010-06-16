package ufc.ck017.mmjc.emitting.graph.interference;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.emitting.graph.Node;
import ufc.ck017.mmjc.emitting.graph.flow.FlowGraph;
import ufc.ck017.mmjc.instructionSelection.assem.AMOVE;
import ufc.ck017.mmjc.instructionSelection.assem.TempSet;
import ufc.ck017.mmjc.util.Pair;

public class Liveness extends InterferenceGraph {
	
	private Dictionary<Node, TempSet> liveout;
	private Dictionary<Node, TempSet> livein;
	private Dictionary<Temp, Node> tnode;
	private FlowGraph fgraph;

	public Liveness(FlowGraph flow) {
		fgraph = flow;
		liveout = new Hashtable<Node, TempSet>(flow.size());
		livein = new Hashtable<Node, TempSet>(flow.size());
		tnode = new Hashtable<Temp, Node>((int)(flow.size()*0.6));
		
		for(Node n : flow) {
			livein.put(n, new TempSet(flow.use(n)));
			liveout.put(n, new TempSet());
		}
		
		setIterate();
		
		for(Node n : flow) {
			int i = 0, j = 0;
			
			for(Temp t1 : liveout.get(n)) {
				j = i++;
				
				for(Temp t2 : liveout.get(n)) {
					if(j > 0) { j--; continue; }
					
					if(tnode.get(t2) == null) tnode.put(t2, newNode(t2));
					if(t1 != t2) addUEdge(tnode.get(t1), tnode.get(t2));
				}
			}
		}
	}
	
	private void setIterate() {
		boolean changed;
		Iterator<Node> it;
		
		do {
			changed = false;
			it = fgraph.descendingIterator();
			
			while(it.hasNext()) {
				Node n = it.next();
				TempSet out = liveout.get(n);
				TempSet in = livein.get(n);
				
				for(Node s : n.succ()) {
					if(out.union(livein.get(s)) && !changed)
						changed = true;
				}
				
				if(in.union(TempSet.difference(out, fgraph.def(n))) && !changed)
					changed = true;
			}
		} while(changed);
	}

	@Override
	public Temp gtemp(Node node) {
		return (Temp) infotable.get(node);
	}

	@Override
	public List<Pair<Node>> moves() {
		LinkedList<Pair<Node>> movelist = new LinkedList<Pair<Node>>();
		
		for(Node n : fgraph.moves()) {
			AMOVE m = (AMOVE)fgraph.getNodeInfo(n);
			movelist.add(new Pair<Node>(tnode.get(m.dst), tnode.get(m.src)));
		}
		
		return movelist;
	}

	@Override
	public Node tnode(Temp temp) {
		return tnode.get(temp);
	}

}
