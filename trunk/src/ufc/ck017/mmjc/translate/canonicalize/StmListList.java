package ufc.ck017.mmjc.translate.canonicalize;

import ufc.ck017.mmjc.translate.tree.StmList;

public class StmListList {
	public StmList head;
	public StmListList tail;
	
	public StmListList(StmList h, StmListList t) {
		head=h; tail=t;
	}
}

