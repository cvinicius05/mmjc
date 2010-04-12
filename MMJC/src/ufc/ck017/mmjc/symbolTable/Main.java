package ufc.ck017.mmjc.symbolTable;
import java.util.Iterator;


public class Main {
	public static void main(String[] args) {
		String s = "aa";
		String str = "ba";
		System.out.println(str.intern().hashCode() < s.intern().hashCode());

		VarSymbol ex = VarSymbol.symbol(str);
		ex = VarSymbol.symbol(s);
		System.out.println(ex.getDictonary());


		HashList list = new HashList();
		list.insert(s, str);
		list.insert(str, s);
		list.insert(s+str, str);

		System.out.println(list.getList().getFirst().getVarSymbol());
		Iterator<Bucket> iter = list.getList().iterator();
		while(iter.hasNext()){
			Bucket n = iter.next();
			System.out.println(n.getVarSymbol().getDictonary() + " " + n.getHead().getTypeSymbol().getDictonary());
		}

		Bucket[] List= list.getArray();
		int i=0;
		while(i<List.length) {
			System.out.println(List[i].getVarSymbol());
			i++;
		}
	}
}
