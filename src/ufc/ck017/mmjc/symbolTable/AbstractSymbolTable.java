package ufc.ck017.mmjc.symbolTable;

import java.util.Iterator;
import java.util.LinkedList;

import ufc.ck017.mmjc.node.AExtNextclass;
import ufc.ck017.mmjc.node.ANonextNextclass;
import ufc.ck017.mmjc.node.AProgram;
import ufc.ck017.mmjc.node.PNextclass;
import ufc.ck017.mmjc.node.PProgram;

public class AbstractSymbolTable{

	private static LinkedList<Class> auxiliarList;
	private static LinkedList<Class> extendedNotDefined;
	
	public AbstractSymbolTable() {
		auxiliarList = new LinkedList<Class>();
		extendedNotDefined = new LinkedList<Class>();
	}
	
	public LinkedList<Class> createAuxiliarList(PProgram program) {
		Iterator<PNextclass> iter = ((AProgram) program).getNextclass().iterator();
		PNextclass next = null;

		while(iter.hasNext()) {
			next = iter.next();
			
			Class newClass = new Class(next);
			newClass.setLocalVariables(next);
			newClass.setMethods(next);
			
			if(next instanceof ANonextNextclass) {
				auxiliarList.add(newClass);
				removeOfExtendedNotDefined(newClass);
			}
			
			else if(next instanceof AExtNextclass) {
				if(!auxiliarList.isEmpty()) {
					Iterator<Class> iter2 = auxiliarList.iterator();
					
					while(iter2.hasNext()) {
						Class aux = iter2.next();
						
						if(aux.getName().equals(newClass.getParentClass())) {
							aux.setExtendedList(newClass);
							removeOfExtendedNotDefined(newClass);
							break;
						}
						else if(setExtendedClass(aux, newClass)) break;
					}
				}
				else extendedNotDefined.add(newClass);
			}
		}
		if(!extendedNotDefined.isEmpty()) return null;
		return auxiliarList;
	}
	
	private boolean setExtendedClass(Class aux, Class newClass) {
		if(!aux.getExtendedClass().isEmpty()) {
			Iterator<Class> iter = aux.getExtendedClass().iterator();
			
			while(iter.hasNext()) {
				Class aux2 = iter.next();
				
				if(aux2.getName().equals(newClass.getParentClass())) {
					aux2.setExtendedList(newClass);
					removeOfExtendedNotDefined(newClass);
					return true;
				}
				else if(setExtendedClass(aux2, newClass)) return true;
			}
		}
		return false;
	}
	
	private void removeOfExtendedNotDefined(Class newClass) {
		if(!extendedNotDefined.isEmpty()) {

			LinkedList<Class> auxExtendedNotDefinedList = new LinkedList<Class>();
			Iterator<Class> iter = extendedNotDefined.iterator();
			
			while(iter.hasNext()) {
				Class c = iter.next();
				
				if(c.getParentClass().equals(newClass.getName())) {
					newClass.setExtendedList(c);
					auxExtendedNotDefinedList.add(c);
				}
			}
			if(!auxExtendedNotDefinedList.isEmpty()) {
				iter = auxExtendedNotDefinedList.iterator();
				while(iter.hasNext()) extendedNotDefined.remove(iter.next());
				
				iter = newClass.getExtendedClass().iterator();
				while(iter.hasNext()) removeOfExtendedNotDefined(iter.next());					
			}
		}
	}
}