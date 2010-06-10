package ufc.ck017.mmjc.emitting.regalloc;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.emitting.graph.FlowGraph;
import ufc.ck017.mmjc.emitting.graph.InterferenceGraph;
import ufc.ck017.mmjc.emitting.graph.Node;
import ufc.ck017.mmjc.instructionSelection.assem.TempSet;

public class Liveness extends InterferenceGraph {
	
	private Dictionary<Node, TempSet> liveout = new Hashtable<Node, TempSet>();
	private Dictionary<Node, TempSet> livein = new Hashtable<Node, TempSet>();
	private FlowGraph fgraph;

	public Liveness(FlowGraph flow) {
		fgraph = flow;
		
		for(Node n : flow) {
			livein.put(n, new TempSet(flow.use(n)));
			liveout.put(n, new TempSet());
		}
		
		setIterate();
		
		// TODO continuar daqui
	}
	
	private void setIterate() {
		boolean changed;
		Iterator<Node> it = fgraph.descendingIterator();
		
		do {
			changed = false;
			
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Node> moves() {
		return fgraph.moves();
	}

	@Override
	public Node tnode(Temp temp) {
		// TODO Auto-generated method stub
		return null;
	}

}
