package ufc.ck017.mmjc.util;

import java.util.LinkedList;

public class ErrorLog {
	private LinkedList<String> errors = null;
	private static ErrorLog instance = null;
	
	private ErrorLog() {
		errors = new LinkedList<String>();
	}
	
	public void addError(String e) {
		errors.add(e);
	}
	
	public int printErrors() {
		System.err.println(errors.size()+" errors reported:");
		for(String e : errors)
			System.err.println(e);
		
		return errors.size();
	}
	
	public static ErrorLog getInstance() {
		if(instance == null) instance = new ErrorLog();
		
		return instance;
	}
}