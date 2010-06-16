package ufc.ck017.mmjc.emitting.regalloc;

import ufc.ck017.mmjc.emitting.graph.Node;

public class MoveList {

	public Node src, dst;
	public MoveList tail;

	public MoveList(Node s, Node d, MoveList t) {
		src=s; dst=d; tail=t;
   }
}
