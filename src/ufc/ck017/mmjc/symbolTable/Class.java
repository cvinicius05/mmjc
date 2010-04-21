package ufc.ck017.mmjc.symbolTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import ufc.ck017.mmjc.node.AExtNextclass;
import ufc.ck017.mmjc.node.AMethod;
import ufc.ck017.mmjc.node.ANonextNextclass;
import ufc.ck017.mmjc.node.AVar;
import ufc.ck017.mmjc.node.PMethod;
import ufc.ck017.mmjc.node.PNextclass;
import ufc.ck017.mmjc.node.PVar;

public class Class {

	private VarSymbol name = null;
	private VarSymbol parentClass = null;
	private ArrayList<Binding> localVariables = null;
	private ArrayList<Method> methods = null;
	private LinkedList<Class> extendedClass = null;

	public Class(PNextclass nextClass) {
		if(nextClass instanceof ANonextNextclass) {

			int sizeOfVariables = ((ANonextNextclass) nextClass).getVar().size();
			localVariables = new ArrayList<Binding>(sizeOfVariables);

			int sizeOfMethods = ((ANonextNextclass) nextClass).getMethod().size();
			methods = new ArrayList<Method>(sizeOfMethods);

			name = VarSymbol.symbol(((ANonextNextclass) nextClass).getId());
		}
		else if(nextClass instanceof AExtNextclass){

			int sizeOfVariables = ((AExtNextclass) nextClass).getVar().size();
			localVariables = new ArrayList<Binding>(sizeOfVariables);

			int sizeOfMethods = ((AExtNextclass) nextClass).getMethod().size();
			methods = new ArrayList<Method>(sizeOfMethods);

			name = VarSymbol.symbol(((AExtNextclass) nextClass).getName());
			parentClass = VarSymbol.symbol(((AExtNextclass) nextClass).getExt());
		}
		extendedClass = new LinkedList<Class>();
	}

	public VarSymbol getName() {
		return name;
	}

	public ArrayList<Binding> getLocalVariables() {
		return localVariables;
	}

	public ArrayList<Method> getMethods() {
		return methods;
	}

	public VarSymbol getParentClass() {
		return parentClass;
	}

	public LinkedList<Class> getExtendedClass() {
		return extendedClass;
	}

	public void setExtendedList(Class c) {
		extendedClass.add(c);
	}

	public void setLocalVariables(PNextclass nextClass) {
		Iterator<PVar> iter = null;

		if(nextClass instanceof ANonextNextclass) iter = ((ANonextNextclass) nextClass).getVar().iterator();
		else if(nextClass instanceof AExtNextclass) iter = ((AExtNextclass) nextClass).getVar().iterator();

		PVar var = null;
		while(iter.hasNext()) {
			var = iter.next();

			VarSymbol v = VarSymbol.symbol(((AVar) var).getId());
			TypeSymbol t = TypeSymbol.symbol(((AVar) var).getType());
			Binding b = new Binding(v, t);

			int index = v.hashCode() % localVariables.size();
			while(localVariables.get(index) != null) index = (index+1) % localVariables.size();
			localVariables.add(index, b);
		}
	}

	public void setMethods(PNextclass nextClass) {
		PMethod method = null;
		int index;
		Iterator<PMethod> iter = null;

		if(nextClass instanceof ANonextNextclass) iter = ((ANonextNextclass) nextClass).getMethod().iterator();
		else if(nextClass instanceof AExtNextclass) iter = ((AExtNextclass) nextClass).getMethod().iterator();

		while(iter.hasNext()){
			method = iter.next();
			VarSymbol nameOfClass = VarSymbol.symbol(((AMethod) method).getId());
			index = nameOfClass.hashCode() % methods.size();

			while(methods.get(index) != null) index = (index+1) % methods.size();

			Method m = new Method(method, nameOfClass);
			methods.add(index, m);
		}
	}
}