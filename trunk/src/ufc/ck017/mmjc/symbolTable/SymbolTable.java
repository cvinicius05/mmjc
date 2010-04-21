package ufc.ck017.mmjc.symbolTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ufc.ck017.mmjc.node.AMainclass;
import ufc.ck017.mmjc.node.AProgram;
import ufc.ck017.mmjc.node.PProgram;
import ufc.ck017.mmjc.node.TId;
import ufc.ck017.mmjc.semantic.SymbolTableInterface;

public class SymbolTable implements SymbolTableInterface{

	private VarSymbol main = null;
	private VarSymbol mainArgument = null;
	private static ArrayList<Class> table = null;
	private static LinkedList<BindingOfScope> stakeOfScope = null;
	
	public SymbolTable(PProgram program) {
		main = VarSymbol.symbol(((AMainclass)((AProgram) program).getMainclass()).getCn());
		mainArgument = VarSymbol.symbol(((AMainclass)((AProgram) program).getMainclass()).getAn());
		
		AbstractSymbolTable abstractTable = new AbstractSymbolTable();
		LinkedList<Class> list = abstractTable.createAuxiliarList(program);
		createTableSymbol(list);
		
		stakeOfScope = new LinkedList<BindingOfScope>();
	}
	
	private void createTableSymbol(LinkedList<Class> list) {
		table = new ArrayList<Class>(list.size());

		int position = 0;
		Iterator<Class> iter = list.iterator();
		Class c = null;
		
		while(iter.hasNext()) {
			c = iter.next();
			position = c.getName().hashCode() % list.size();
			
			while(table.get(position) != null) position = (position+1) % list.size();
			table.set(position, c);
		}
	}
	
	public VarSymbol getMain() {
		return main;
	}
	
	public VarSymbol getMainArgument() {
		return mainArgument;
	}

	public ArrayList<Class> getTable() {
		return table;
	}
	
	public VarSymbol getActualScope() {
		return stakeOfScope.peek().getScope();
	}
	
	public int getTypeOfActualScope() {
		return stakeOfScope.peek().getType();
	}
	
	public LinkedList<BindingOfScope> getStackOfScope() {
		return stakeOfScope;
	}
	
	public void setActualScope(VarSymbol scope, int type) {
		BindingOfScope b = new BindingOfScope(scope, type);
		stakeOfScope.push(b);
	}

	public void setTypeOfActualScope(int type) {
		stakeOfScope.peek().setType(type);
	}
	
	public VarSymbol getGlobalScope() {
		return stakeOfScope.peekLast().getScope();
	}
	
	public void setGlobalScope(VarSymbol scope) {
		
		int position = scope.hashCode();
		if(position < 0) position *= -1;
		position = position % table.size();
		int marker = position;
		
		if(!table.get(position).getName().equals(scope)) {
			do{
				position = (position+1) % table.size();
			}while(!table.get(position).getName().equals(scope) && position != marker);
		}
		
		if(position == marker) {
			// TODO ERROR escopo nao encontrado!
		}
		BindingOfScope b = new BindingOfScope(scope, 0);
		stakeOfScope.push(b);
	}
	
	public Class getClassOfGlobalScope() {
		if(!stakeOfScope.isEmpty()) {
			VarSymbol globalScope = stakeOfScope.peekLast().getScope();
			
			int position = globalScope.hashCode();
			if(position < 0) position *= -1;
			position = position % table.size();
			
			if(table.get(position).getName().equals(globalScope)) return table.get(position);
			else {
				int marker = position;
				do{
					position = (position+1) % table.size();
				}while(!table.get(position).getName().equals(globalScope) && position != marker);
				
				if(position == marker) {
					//TODO ERROR escopo nao encontrado!
				}
				return table.get(position);
			}
		}
		// TODO Nao existe escopo para avaliar!
		return null;
	}
	
	public Object getObjectOfActualScope() {
		Class c = getClassOfGlobalScope();

		if(c != null) {
			if(stakeOfScope.size() == 1) return c;
			
			Iterator<BindingOfScope> iterOfStake = stakeOfScope.descendingIterator();
			BindingOfScope b = iterOfStake.next();
			
			while(iterOfStake.hasNext()) {
				b = iterOfStake.next();
				
				if(b.getType() == 0) {
					int position = b.getScope().hashCode() % c.getExtendedClass().size();
					if(position < 0) position *= -1;
					
					if(c.getExtendedClass().get(position).getName().equals(b.getScope())) c = c.getExtendedClass().get(position);
					else {
						int marker = position;
						
						do{
							position = (position+1) % c.getExtendedClass().size();
						}while(!c.getExtendedClass().get(position).getName().equals(b.getScope()));
						
						if(position == marker) {
							// TODO ERROR escopo nao encontrado!
						}
						c = c.getExtendedClass().get(position);
					}
				}
				else {
					int position = b.getScope().hashCode() % c.getMethods().size();
					if(position < 0) position *= -1;
					
					if(c.getMethods().get(position).getName().equals(b.getScope())) return c.getMethods().get(position);
					else {
						int marker = position;
						
						do{
							position = (position+1) % c.getMethods().size();
						}while(!c.getMethods().get(position).getName().equals(b.getScope()));
						
						if(position == marker) {
							// TODO ERROR escopo nao encontrado!
						}
						return c.getMethods().get(position);
					}
				}
			}
		}
		return null;
	}
	
	public Object getObject(VarSymbol nameOfId) {
		Class actualClass = null;
		Iterator<Class> classIter = table.iterator();
		
		while(classIter.hasNext()) {
			actualClass = classIter.next();
			
			if(actualClass.getName().equals(nameOfId)) return actualClass;
			else {
				int position = nameOfId.hashCode() % actualClass.getMethods().size();
				if(position < 0) position *= -1;
				
				if(actualClass.getMethods().get(position).getName().equals(nameOfId)) return actualClass.getMethods().get(position);
				else {
					int marker = position;
					
					do{
						position = (position+1) % actualClass.getMethods().size();
					}while(!actualClass.getMethods().get(position).getName().equals(nameOfId) && marker != position);
					
					if(position != marker) return actualClass.getMethods().get(position);
					else {
						Iterator<Class> extendedClassIter = actualClass.getExtendedClass().iterator();
						
						while(extendedClassIter.hasNext()) {
							actualClass = extendedClassIter.next();
							
							if(actualClass.getName().equals(nameOfId)) return actualClass;
							
							Object found = searchOfObject(actualClass, nameOfId);
							if(found != null) return found;
						}
					}
				}
			}
		}
		return null;
	}
	
	public Object searchOfObject(Class actualClass, VarSymbol nameOfId) {
		int position = nameOfId.hashCode() % actualClass.getMethods().size();
		if(position < 0) position *= -1;
		
		if(actualClass.getMethods().get(position).getName().equals(nameOfId)) return actualClass.getMethods().get(position);
		else {
			int marker = position;
			
			do{
				position = (position+1) % actualClass.getMethods().size();
			}while(!actualClass.getMethods().get(position).getName().equals(nameOfId) && marker != position);
			
			if(position != marker) return actualClass.getMethods().get(position);
			else {
				Iterator<Class> extendedClassIter = actualClass.getExtendedClass().iterator();
				
				while(extendedClassIter.hasNext()) {
					actualClass = extendedClassIter.next();
					
					if(actualClass.getName().equals(nameOfId)) return actualClass;
					
					Object found = searchOfObject(actualClass, nameOfId);
					if(found != null) return found;
				}
			}
		}
		return null;
	}
	
	@Override
	public void enterScope(TId id) {
		VarSymbol nameOfId = VarSymbol.symbol(id);
		Object actualEscope = getObjectOfActualScope();

		if(actualEscope instanceof Method) {
			// TODO ERROR escopos de metodos aninhados ou classe contida em um metodo
		}
		else if(actualEscope instanceof Class) {

			int position = nameOfId.hashCode() % ((Class) actualEscope).getMethods().size();
			if(position < 0) position *= -1;
			
			if(((Class) actualEscope).getMethods().get(position).getName().equals(nameOfId)) {
				BindingOfScope b = new BindingOfScope(((Class) actualEscope).getMethods().get(position).getName(), 1);
				stakeOfScope.push(b);
			}
			else {
				int marker = position;
				
				do{
					position = (position+1) % ((Class) actualEscope).getMethods().size();
				}while(!((Class) actualEscope).getMethods().get(position).getName().equals(nameOfId) && position != marker);
				
				if(marker != position) {
					BindingOfScope b = new BindingOfScope(((Class) actualEscope).getMethods().get(position).getName(), 1);
					stakeOfScope.push(b);
				}
				else {
					position = nameOfId.hashCode() % ((Class) actualEscope).getExtendedClass().size();
					if(position < 0) position *= -1;
					
					if(((Class) actualEscope).getExtendedClass().get(position).getName().equals(nameOfId)) {
						BindingOfScope b = new BindingOfScope(((Class) actualEscope).getExtendedClass().get(position).getName(), 0);
						stakeOfScope.push(b);
					}
					else {
						marker = position;
						
						do{
							position = (position+1) % ((Class) actualEscope).getExtendedClass().size();
						}while(!((Class) actualEscope).getExtendedClass().get(position).getName().equals(nameOfId) && position != marker);
						
						if(marker != position) {
							BindingOfScope b = new BindingOfScope(((Class) actualEscope).getExtendedClass().get(position).getName(), 0);
							stakeOfScope.push(b);
						}
						else {
							// TODO ERROR objeto nao encontrado!
						}
					}
				}
			}
		}
	}

	@Override
	public void exitScope() {
		if(!stakeOfScope.isEmpty()) stakeOfScope.pop();
	}

	/* O escopo de um método engloba os parâmetros dele?
	 * E de uma classe, engloba todas as variáveis dos métodos e subclasses?*/
	@Override
	public TypeSymbol getType(TId id) {
		VarSymbol nameOfId = VarSymbol.symbol(id);
		Object actualEscope = getObjectOfActualScope();
		
		if(actualEscope instanceof Method) {
			int position = nameOfId.hashCode() % ((Method) actualEscope).getLocalVariables().size();
			if(position < 0) position *= -1;
			
			if(((Method) actualEscope).getLocalVariables().get(position).getVarSymbol().equals(nameOfId)) {
				return ((Method) actualEscope).getLocalVariables().get(position).getTypeSymbol();
			}
			else {
				int marker = position;
				
				do{
					position = (position+1) % ((Method) actualEscope).getLocalVariables().size();
				}while(!((Method) actualEscope).getLocalVariables().get(position).getVarSymbol().equals(nameOfId) && position != marker);
				
				if(marker != position) return ((Method) actualEscope).getLocalVariables().get(position).getTypeSymbol();
				else {
					
					position = nameOfId.hashCode() % ((Method) actualEscope).getParameters().size();
					if(position < 0) position *= -1;
					
					if(((Method) actualEscope).getParameters().get(position).getVarSymbol().equals(nameOfId)) {
						return ((Method) actualEscope).getParameters().get(position).getTypeSymbol();
					}
					else {
						marker = position;
						
						do{
							position = (position+1) % ((Method) actualEscope).getParameters().size();
						}while(!((Method) actualEscope).getParameters().get(position).getVarSymbol().equals(nameOfId) && position != marker);
						
						if(marker != position) return ((Method) actualEscope).getParameters().get(position).getTypeSymbol();
						else return null;
					}
				}
			}
		}
		else if(actualEscope instanceof Class){
			int position = nameOfId.hashCode() % ((Class) actualEscope).getLocalVariables().size();
			if(position < 0) position *= -1;
			
			if(((Class) actualEscope).getLocalVariables().get(position).getVarSymbol().equals(nameOfId)) {
				return ((Class) actualEscope).getLocalVariables().get(position).getTypeSymbol();
			}
			else {
				int marker = position;
				
				do{
					position = (position+1) % ((Class) actualEscope).getLocalVariables().size();
				}while(!((Class) actualEscope).getLocalVariables().get(position).getVarSymbol().equals(nameOfId) && position != marker);
				
				if(marker != position) return ((Class) actualEscope).getLocalVariables().get(position).getTypeSymbol();
				else {
					
					position = nameOfId.hashCode() % ((Class) actualEscope).getMethods().size();
					if(position < 0) position *= -1;
					
					if(((Class) actualEscope).getMethods().get(position).getName().equals(nameOfId)) {
						return ((Class) actualEscope).getMethods().get(position).getType();
					}
					else {
						marker = position;
						
						do{
							position = (position+1) % ((Class) actualEscope).getMethods().size();
						}while(!((Class) actualEscope).getMethods().get(position).getName().equals(nameOfId) && position != marker);
						
						if(marker != position) return ((Class) actualEscope).getMethods().get(position).getType();
						else return null;
					}
				}
			}
		}
		return null;
	}

	@Override
	public TypeSymbol getType(TypeSymbol cname, TId id) {
		VarSymbol className = VarSymbol.symbolFromString(cname.toString());
		Object objectClass = getObject(className);
		
		if(objectClass != null && !(objectClass instanceof Method)) {
			
			VarSymbol method = VarSymbol.symbol(id);
			int position = method.hashCode() % ((Class) objectClass).getMethods().size();
			if(position < 0) position *= -1;
			
			if(((Class) objectClass).getMethods().get(position).getName().equals(method)) {
				return ((Class) objectClass).getMethods().get(position).getType();
			}
			else {
				int marker = position;
				
				do{
					position = (position+1) % ((Class) objectClass).getMethods().size();
				}while(!((Class) objectClass).getMethods().get(position).getName().equals(method) && position != marker);
				
				if(marker != position) return ((Class) objectClass).getMethods().get(position).getType();
				else return null;
			}
		}
		return null;
	}

	@Override
	public boolean isClass(TId cname) {
		VarSymbol className = VarSymbol.symbol(cname);
		Object objectClass = getObject(className);
		
		if(objectClass != null && !(objectClass instanceof Method)) return true;
		return false;
	}

	@Override
	public boolean isClass(TypeSymbol type) {
		VarSymbol className = VarSymbol.symbolFromString(type.toString());
		Object objectClass = getObject(className);
		
		if(objectClass != null && !(objectClass instanceof Method)) return true;
		return false;
	}

	@Override
	public boolean isMethod(TypeSymbol obj, TId mname, List<TypeSymbol> params) {
		VarSymbol className = VarSymbol.symbolFromString(obj.toString());
		Object objectClass = getObject(className);
		
		if(objectClass != null && !(objectClass instanceof Method)) {
			
			VarSymbol method = VarSymbol.symbol(mname);
			int position = method.hashCode() % ((Class) objectClass).getMethods().size();
			if(position < 0) position *= -1;
			
			if(((Class) objectClass).getMethods().get(position).getName().equals(method)) {
				
				if(params.size() == ((Class) objectClass).getMethods().get(position).getParameters().size()){
					
					Iterator<Binding> parametersOfMethod = ((Class) objectClass).getMethods().get(position).getParameters().iterator();
					Iterator<TypeSymbol> elementsOfList = params.iterator();
					TypeSymbol type = null;
					
					while(parametersOfMethod.hasNext()) {
						type = parametersOfMethod.next().getTypeSymbol();
						if(!type.equals(elementsOfList.next())) return false;
					}
				}
				else return false;
			}
			else {
				int marker = position;
				do{
					position = (position+1) % ((Class) objectClass).getMethods().size();
				}while(!((Class) objectClass).getMethods().get(position).getName().equals(method) && position != marker);
				
				if(marker != position && params.size() == ((Class) objectClass).getMethods().get(position).getParameters().size()) {
					Iterator<Binding> parametersOfMethod = ((Class) objectClass).getMethods().get(position).getParameters().iterator();
					Iterator<TypeSymbol> elementsOfList = params.iterator();
					TypeSymbol type = null;
					
					while(parametersOfMethod.hasNext()) {
						type = parametersOfMethod.next().getTypeSymbol();
						if(!type.equals(elementsOfList.next())) return false;
					}
				}
				else return false;
			}
		}
		return false;
	}

	@Override
	public boolean isObject(TId id) {
		Object obj = getObjectOfActualScope();
		
		if(obj != null) {
			if(obj instanceof Method) {
				
				VarSymbol var = VarSymbol.symbol(id);
				
				int position = var.hashCode() % ((Method) obj).getLocalVariables().size();
				if(position < 0) position *= -1;
				
				if(((Method) obj).getLocalVariables().get(position).getVarSymbol().equals(var)) {
					VarSymbol typeOfVar = VarSymbol.symbolFromString(((Method) obj).getLocalVariables().get(position).getTypeSymbol().toString());
					Object object = getObject(typeOfVar);
					
					if(object != null && object instanceof Class) return true;
					return false;
				}
				else {
					int marker = position;
					do{
						position = (position+1) % ((Method) obj).getLocalVariables().size();
					}while(!((Method) obj).getLocalVariables().get(position).getVarSymbol().equals(var) && position != marker);
					
					if(marker != position) {
						VarSymbol typeOfVar = VarSymbol.symbolFromString(((Method) obj).getLocalVariables().get(position).getTypeSymbol().toString());
						Object object = getObject(typeOfVar);
						
						if(object != null && object instanceof Class) return true;
						return false;
					}
					else return false;
				}
			}
			else if(obj instanceof Class) {
				VarSymbol var = VarSymbol.symbol(id);
				
				int position = var.hashCode() % ((Class) obj).getLocalVariables().size();
				if(position < 0) position *= -1;
				
				if(((Class) obj).getLocalVariables().get(position).getVarSymbol().equals(var)) {
					VarSymbol typeOfVar = VarSymbol.symbolFromString(((Class) obj).getLocalVariables().get(position).getTypeSymbol().toString());
					Object object = getObject(typeOfVar);
					
					if(object != null && object instanceof Class) return true;
					return false;
				}
				else {
					int marker = position;
					do{
						position = (position+1) % ((Class) obj).getLocalVariables().size();
					}while(!((Class) obj).getLocalVariables().get(position).getVarSymbol().equals(var) && position != marker);
					
					if(marker != position) {
						VarSymbol typeOfVar = VarSymbol.symbolFromString(((Class) obj).getLocalVariables().get(position).getTypeSymbol().toString());
						Object object = getObject(typeOfVar);
						
						if(object != null && object instanceof Class) return true;
						return false;
					}
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isSubclassOf(TypeSymbol supcname, TypeSymbol subcname) {
		VarSymbol nameOfsuperClass = VarSymbol.symbolFromString(supcname.toString());
		Object superClass = getObject(nameOfsuperClass);
		
		if(superClass != null && !(superClass instanceof Method)) {
			VarSymbol nameOfSubClass = VarSymbol.symbolFromString(subcname.toString());
			
			int position = nameOfSubClass.hashCode() % ((Class) superClass).getExtendedClass().size();
			if(position < 0) position *= -1;
			
			if(((Class) superClass).getExtendedClass().get(position).getName().equals(nameOfSubClass)) return true;
			else {
				int marker = position;
				
				do{
					position = (position+1) % ((Class) superClass).getExtendedClass().size();
				}while(!((Class) superClass).getExtendedClass().get(position).getName().equals(nameOfSubClass) && position != marker);
				
				if(marker != position) return true;
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isVar(TId id) {
		Object actualScope = getObjectOfActualScope();
		if(actualScope != null){
			if(actualScope instanceof Method) {
				VarSymbol var = VarSymbol.symbol(id);

				int position = var.hashCode() % ((Method) actualScope).getLocalVariables().size();
				if(position < 0) position *= -1;
				
				if(((Method) actualScope).getLocalVariables().get(position).getVarSymbol().equals(var)) return true;
				else {
					int marker = position;
					
					do{
						position = (position+1) % ((Method) actualScope).getLocalVariables().size();
					}while(!((Method) actualScope).getLocalVariables().get(position).getVarSymbol().equals(var) && position != marker);
					
					if(marker != position) return true;
					else {
						
						position = var.hashCode() % ((Method) actualScope).getParameters().size();
						if(position < 0) position *= -1;
						
						if(((Method) actualScope).getParameters().get(position).getVarSymbol().equals(var)) return true;
						else {
							marker = position;
							
							do{
								position = (position+1) % ((Method) actualScope).getParameters().size();
							}while(!((Method) actualScope).getParameters().get(position).getVarSymbol().equals(var) && position != marker);
							
							if(marker != position) return true;
							return false;
						}
					}
				}
			}
			else if(actualScope instanceof Class) {
				VarSymbol var = VarSymbol.symbol(id);
				
				int position = var.hashCode() % ((Class) actualScope).getLocalVariables().size();
				if(position < 0) position *= -1;
				
				if(((Class) actualScope).getLocalVariables().get(position).getVarSymbol().equals(var)) return true;
				else {
					int marker = position;
					
					do{
						position = (position+1) % ((Class) actualScope).getLocalVariables().size();
					}while(!((Class) actualScope).getLocalVariables().get(position).getVarSymbol().equals(var) && position != marker);
					
					if(marker != position) return true;
					return false;
				}
			}
		}
		return false;
	}

}
