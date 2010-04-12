package ufc.ck017.mmjc.symbolTable;

import java.util.Dictionary;
import java.util.Hashtable;

public class TypeSymbol {

	private String typeName;
	private static Dictionary<String, TypeSymbol> typeDict = new Hashtable<String, TypeSymbol>();
	
	public TypeSymbol(String type) {
		typeName = type;
	}
	
	public String toString() {
		return typeName;
	}
	
	public static TypeSymbol symbol(String n) {
	      String u = n.intern();
	      TypeSymbol s = (TypeSymbol)typeDict.get(u);
	      if(s == null) {
	    	  s = new TypeSymbol(u);
	    	  typeDict.put(u,s);
	      }
	      return s;
	}
	
	public Dictionary<String, TypeSymbol> getDictonary() {
		return typeDict;
		}
}
