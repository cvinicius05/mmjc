package ufc.ck017.mmjc.emitting.graph;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Graph implements Iterable<Node>{

	int nodecount = 0;

	List<Node> mynodes = new LinkedList<Node>();
	protected Dictionary<Node, Object> infotable = new Hashtable<Node, Object>(); 

	public void addEdge(Node from, Node to) {
		if(from == null || to == null) return;
		
		check(from);
		check(to);
		
		if (from.goesTo(to)) return;
		
		to.preds.add(from);
		to.adjlist.add(from);
		
		from.succs.add(to);
		from.adjlist.add(to);
	}

	void check(Node n) {
		if (n.mygraph != this)
			throw new Error("Graph.addEdge using nodes from the wrong graph");
	}

	public Node newNode(Object info) {
		Node n = new Node(this);
		infotable.put(n, info);
		
		return n;
	}

	public List<Node> nodes() {
		return mynodes;
	}

	public void rmEdge(Node from, Node to) {
		if(from == null || to == null) return;
		
		to.preds.remove(from);
		from.succs.remove(to);
	}

	/**
	 * Print a human-readable dump for debugging.
	 */
	public void show(java.io.PrintStream out) {
		for (Node u : mynodes) {
			out.print(u.toString());
			out.print(": ");
			for (Node v : u.succs) {
				out.print(v.toString());
				out.print(" ");
			}
			out.println();
		}
	}

	@Override
	public Iterator<Node> iterator() {
		return mynodes.iterator();
	}

}