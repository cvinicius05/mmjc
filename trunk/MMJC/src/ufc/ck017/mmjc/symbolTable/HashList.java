package ufc.ck017.mmjc.symbolTable;

import java.util.Iterator;
import java.util.LinkedList;

public class HashList {

	private static LinkedList<Bucket> list;

	public HashList() {
		list = new LinkedList<Bucket>();
	}

	public LinkedList<Bucket> getList() {
		return list;
	}

	public void insert(String v, String t) {
		VarSymbol var = new VarSymbol(v);
		TypeSymbol type = new TypeSymbol(t);
		Bucket node;

		if(list.isEmpty()){
			node = new Bucket(var, type);
			list.add(node);
		}

		else{
			Iterator<Bucket> iter = list.iterator();
			VarSymbol vs;
			
			while(iter.hasNext()){
				node = iter.next();
				vs = node.getVarSymbol();
				if(var.equals(vs)){
					Binding b = new Binding(type);
					b.setNext(node.getHead());
					node.setHead(b);
					break;
				}
			}
			node = new Bucket(var, type);
			list.add(node);
		}
	}
	
	public Bucket[] getArray() {
		Bucket[] vector = new Bucket[list.size()];
		Bucket n;
		if(list.isEmpty()) return null;
		else {
			int i=0;
			Iterator<Bucket> iter = list.iterator();
			while(iter.hasNext()){
				n = iter.next();
				i = hash(n);
				if(vector[i] != null){
					do{
						i = (i+1)%(vector.length);
					}
					while(vector[i] != null);
				}
				vector[i] = n;

			}
			return vector;
		}
	}
	
	public int hash(Bucket n) {
		String var = n.getVarSymbol().toString();
		int position = var.intern().hashCode();
		
		if(position < 0 ) position *=-1;
		
		position = position%list.size();
		return position;
	}
}
