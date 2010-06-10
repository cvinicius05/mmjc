package ufc.ck017.mmjc.emitting.graph;
import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.util.Pair;

abstract public class InterferenceGraph extends Graph {
	 abstract public Node tnode(Temp temp);
	 abstract public Temp gtemp(Node node);
	 abstract public List<Node> moves();
	 public int spillCost(Node node) {return 1;}
}
