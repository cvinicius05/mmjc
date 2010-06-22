package ufc.ck017.mmjc.emitting.graph.interference;
import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.emitting.graph.Graph;
import ufc.ck017.mmjc.emitting.graph.Node;
import ufc.ck017.mmjc.util.Pair;

abstract public class InterferenceGraph extends Graph {

	public abstract Node tnode(Temp temp);
	public abstract Temp gtemp(Node node);
	public abstract List<Pair<Node>> moves();

	public int spillCost(Node node) {
		return node.degree();
	}
}
