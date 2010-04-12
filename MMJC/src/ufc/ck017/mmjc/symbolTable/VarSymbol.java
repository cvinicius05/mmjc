package ufc.ck017.mmjc.symbolTable;

import java.util.Dictionary;
import java.util.Hashtable;

public class VarSymbol {

	private String nameVar;
	private static Dictionary<String, VarSymbol> varDict = new Hashtable<String, VarSymbol>();
	
	public VarSymbol(String var) {
		nameVar = var;
	}
	
	public String toString() {
		return nameVar;
	}
	
	public static VarSymbol symbol(String n) {
	      String u = n.intern();
	      VarSymbol s = (VarSymbol)varDict.get(u);
	      if(s == null) {
	    	  s = new VarSymbol(u);
	    	  varDict.put(u,s);
	      }
	      return s;
	}
	
	public Dictionary<String, VarSymbol> getDictonary() {
		return varDict;
		}
}
