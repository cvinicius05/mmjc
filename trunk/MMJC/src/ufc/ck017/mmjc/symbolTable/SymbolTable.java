package ufc.ck017.mmjc.symbolTable;

public class SymbolTable {

private Bucket[] table;
	
	public SymbolTable(HashList list) {
		table = list.getArray();
	}
	
	public Bucket[] getSymbolTable() {
		return table;
	}
	
	public void pop(String var) {
		VarSymbol v = new VarSymbol(var);
		
		int position = v.hashCode()%table.length;
		while(!table[position].getVarSymbol().equals(v)) {
			position = (position+1)%(table.length);
		}
		table[position] = table[position].getNext();
	}
	
	public TypeSymbol getActualType(String var) {
		VarSymbol v = new VarSymbol(var);
		
		int position = v.hashCode()%table.length;
		while(!table[position].getVarSymbol().equals(v)) {
			position = (position+1)%(table.length);
		}
		TypeSymbol type = table[position].getHead().getTypeSymbol();
		return type;
	}	
}
