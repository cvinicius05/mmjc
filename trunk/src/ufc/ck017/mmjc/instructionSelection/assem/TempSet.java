package ufc.ck017.mmjc.instructionSelection.assem;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import ufc.ck017.mmjc.activationRecords.temp.Temp;

public class TempSet extends TreeSet<Temp> {
		
	private static final long serialVersionUID = -4119318652923707938L;
	private boolean changed = true;
	private List<Temp> elems = new LinkedList<Temp>();
	
	public TempSet(SortedSet<Temp> s) {
		super(s);
	}

	public TempSet() {
		super();
	}

	@Override
	public boolean add(Temp e) {
		boolean r = super.add(e);
		
		if(r && !changed) changed = true;
		return r;
	}
	
	@Override
	public boolean addAll(Collection<? extends Temp> c) {
		boolean r = super.addAll(c);
		
		if(r && !changed) changed = true;
		return r;
	}
	
	@Override
	public boolean remove(Object o) {
		boolean r = super.remove(o);
		
		if(r && !changed) changed = true;
		return r;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean r = super.removeAll(c);
		
		if(r && !changed) changed = true;
		return r;
	}
	
	@Override
	public void clear() {
		if(!isEmpty()) changed = true;
		super.clear();
	}
	
	@Override
	public Temp pollFirst() {
		if(!isEmpty()) changed = true;
		return super.pollFirst();
	}
	
	@Override
	public Temp pollLast() {
		if(!isEmpty()) changed = true;
		return super.pollLast();
	}
	
	public TempSet union(TempSet t) {
		TempSet temp = new TempSet(this);
		temp.addAll(t);
		
		return temp;
	}
	
	public TempSet difference(TempSet t) {
		TempSet temp = new TempSet(this);
		temp.removeAll(t);
		
		return temp;
	}
	
	public TempSet intersection(TempSet t) {
		TempSet temp = new TempSet();
		
		for(Temp tmp : t) {
			if(this.contains(tmp))
				temp.add(tmp);
		}
		
		return temp;
	}
	
	public List<Temp> getList() {
		List<Temp> l = (changed ? getElementsList() : elems);
		
		elems = l;
		changed = false;
		return l;
	}

	private LinkedList<Temp> getElementsList() {
		LinkedList<Temp> l = new LinkedList<Temp>();
		
		for(Temp t : this)
			l.add(t);
		
		return l;
	}
}
