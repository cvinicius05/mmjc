package ufc.ck017.mmjc.emitting.graph;

import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.activationRecords.temp.TempList;
import ufc.ck017.mmjc.emitting.graph.NodeList;
import ufc.ck017.mmjc.emitting.graph.Node;
import ufc.ck017.mmjc.instructionSelection.assem.TempSet;

/**
 * A control flow graph is a directed graph in which each edge indicates a
 * possible flow of control. Also, each node in the graph defines a set of
 * temporaries; each node uses a set of temporaries; and each node is, or is
 * not, a <strong>move</strong> instruction.
 * 
 * @see AssemFlowGraph
 */

public abstract class FlowGraph extends Graph {
	/**
	 * The set of temporaries defined by this instruction or block
	 */
	public abstract TempSet def(Node node);

	/**
	 * The set of temporaries used by this instruction or block
	 */
	public abstract TempSet use(Node node);

	/**
	 * True if this node represents a <strong>move</strong> instruction, i.e.
	 * one that can be deleted if def=use.
	 */
	public abstract boolean isMove(Node node);
	
	public abstract List<Node> moves();

	/**
	 * Print a human-readable dump for debugging.
	 */
	public void show(java.io.PrintStream out) {
		for (Node u : mynodes) {
			out.print(u.toString());
			out.print(": ");
			for (Temp t : def(u)) {
				out.print(t.toString());
				out.print(" ");
			}
			out.print(isMove(u) ? "<= " : "<- ");
			for (Temp t : use(u)) {
				out.print(t.toString());
				out.print(" ");
			}
			out.print("; goto ");
			for (Node v : u.succs) {
				out.print(v.toString());
				out.print(" ");
			}
			out.println();
		}
	}

}
