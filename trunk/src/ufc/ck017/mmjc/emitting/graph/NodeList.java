package ufc.ck017.mmjc.emitting.graph;

import java.util.Iterator;


public class NodeList implements Iterable<Node>{

	public Node head;
	public NodeList tail;

	public NodeList(Node h, NodeList t) {
		head=h; tail=t;
	}

	public Iterator<Node> iterator() {
		return new It(this);
	}

	private class It implements Iterator<Node>{

		NodeList l;

		public It(NodeList accessList) {
			l = accessList;
		}

		public boolean hasNext() {
			return l != null;
		}

		public Node next() {
			Node h = l.head;
			l = l.tail;
			return h;
		}

		public void remove() {
			l = l.tail;
		}

	}
}
