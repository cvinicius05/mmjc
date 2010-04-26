package ufc.ck017.mmjc.symbolTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ufc.ck017.mmjc.node.TId;
import ufc.ck017.mmjc.semantic.SymbolTableInterface;

/**
 * Classe que implementa a gera&ccedil;&atilde;o da Tabela de
 * S&iacute;mbolos, a partir de uma Tabela de S&iacute;mbolos
 * Abstrata, como em {@link AbstractSymbolTable}, para a fase
 * de an&aacute;lise sem&acirc;ntica e seus m&eacute;todos de
 * acesso segundo a interface {@link SymbolTableInterface}. 
 * 
 * @author vinicius
 * @see AbstractSymbolTable
 * @see SymbolTableInterface
 */
public class SymbolTable implements SymbolTableInterface{

	private static ArrayList<Class> table = null;
	private static LinkedList<Object> stakeOfScope = null;

	/**
	 * Construtor que recebe uma lista de Class representada
	 * por uma {@link AbstractSymbolTable}.
	 * 
	 * &Eacute; gerada a Tabela de S&iacute;mbolos como um
	 * ArrayList de Class para otimizar as buscas na tabela.
	 * &Eacute; inicializada tamb&eacute;m uma pilha de escopos
	 * para indicar a qual classe ou m&eacute;todo est&aacute;
	 * sendo feita a an&aacute;lise neste momento.  
	 * 
	 * @param list Uma {@link LinkedList} de Class gerada pela
	 * {@link AbstractSymbolTable}.
	 */
	public SymbolTable(LinkedList<Class> list) {
		if(list != null) {
			stakeOfScope = new LinkedList<Object>();
			table = new ArrayList<Class>(list.size());

			int index = 0;
			Iterator<Class> iter = list.iterator();
			Class c = null;

			while(iter.hasNext()) {
				c = iter.next();
				index = c.getName().hashCode() % list.size();
				if(index < 0 ) index = -index;

				while(table.get(index) != null) index = (index+1) % list.size();
				table.set(index, c);
			}
		}
		else {
			// TODO ERROR Existem classes cuja classe pai nao foi declarada.
		}
		
	}

	/**
	 * Retorna a Tabela de S&iacute;mbolos.
	 * 
	 * @return Retorna a Tabela de S&iacute;mbolos.
	 */
	public ArrayList<Class> getTable() {
		return table;
	}

	/**
	 * Retorna um {@link Object} que representa o objeto 
	 * ao qual o escopo atual se referencia, podendo este
	 * ser uma classe ou um m&eacute;todo.
	 * 
	 * @return {@link Object} do escopo atual.
	 */
	public Object getActualScope() {
		if(!stakeOfScope.isEmpty()) return stakeOfScope.peek();
		return null;
	}

	/**
	 * Retorna uma pilha de objetos alcan&ccedil;ados
	 * at&eacute; o escopo atual.
	 * 
	 * @return {@link LinkedList} de {@link Object}
	 */
	public LinkedList<Object> getStackOfScope() {
		return stakeOfScope;
	}

	/**
	 * Retorna um {@link VarSymbol} representando o escopo global do
	 * escopo atual. Esse escopo global &eacute; uma classe que n&atilde;o
	 * &eacute; extendida por nehuma outra.
	 * 
	 * @return {@link VarSymbol} da classe que representa o escopo global
	 * em rela&ccedil;&atilde;o ao escopo atual.
	 */
	public Object getGlobalScope() {
		return stakeOfScope.peekLast();
	}

	/**
	 * Retorna o objeto cujo nome &eacute; representado
	 * por <i>nameOfId</i> que &eacute; passado como entrada,
	 * podendo ser uma classe ou m&eacute;todo. 
	 * 
	 * @param nameOfId {@link VarSymbol} de um identificador.
	 * @return Se o objeto referente ao {@link VarSymbol} de 
	 * entrada existe na Tabela, &eacute; retornado o objeto,
	 * classe ou m&eacute;todo referente ao mesmo e null caso
	 * n&atilde;o seja encontrado.
	 */
	public Object getObject(VarSymbol nameOfId) {
		if(!stakeOfScope.isEmpty()) stakeOfScope.clear();

		Class actualTableClass = null;
		Iterator<Class> tableIter = table.iterator();

		while(tableIter.hasNext()) {
			actualTableClass = tableIter.next();
			stakeOfScope.push(actualTableClass);
			if(actualTableClass.getName().equals(nameOfId))	return actualTableClass;
			
			Method method = actualTableClass.getMethod(nameOfId);
			if(method != null) return method;

			Iterator<Class> extendedClassIter = actualTableClass.getExtendedClass().iterator();
			while(extendedClassIter.hasNext()) {
				actualTableClass = extendedClassIter.next();
				stakeOfScope.push(actualTableClass);

				if(actualTableClass.getName().equals(nameOfId))	return actualTableClass;

				Object found = searchObject(actualTableClass, nameOfId);
				if(found != null) return found;
				stakeOfScope.pop();
			}
			stakeOfScope.pop();
		}
		return null;
	}
	
	/**
	 * M&eacute;todo que auxilia na busca pelo objeto cujo
	 * nome &eacute; representado pelo {@link VarSymbol}
	 * <i>nameOfId</i> na classe <i>actualExtendedClass</i>.
	 * 
	 * @param actualExtendedClass Classe em &eacute; feita a
	 * busca pelo objeto de nome representado por <i>nameOfId</i>.
	 * @param nameOfId {@link VarSymbol} que representa o nome do
	 * objeto procurado na classe <i>actualExtendedClass</i>.
	 * @return O objeto referenciado por <i>nameOfId</i> se existir
	 * ou null caso contr&aacute;rio.
	 */
	public Object searchObject(Class actualExtendedClass, VarSymbol nameOfId) {
		Method method = actualExtendedClass.getMethod(nameOfId);
		if(method != null) return method;

		Iterator<Class> extendedClassIter = actualExtendedClass.getExtendedClass().iterator();
		while(extendedClassIter.hasNext()) {
			actualExtendedClass = extendedClassIter.next();
			stakeOfScope.push(actualExtendedClass);

			if(actualExtendedClass.getName().equals(nameOfId))
				return actualExtendedClass;

			Object found = searchObject(actualExtendedClass, nameOfId);
			if(found != null) return found;
			stakeOfScope.pop();
		}
		return null;
	}
	
	@Override
	public void enterScope(TId id) {
		VarSymbol nameOfId = VarSymbol.symbol(id);
		Object actualEscope = getObject(nameOfId);

		if(actualEscope != null) stakeOfScope.push(actualEscope);
		else {
			// TODO ERROR Escopo nao encontrado para o indetificador id.
		}
	}

	@Override
	public void exitScope() {
		if(!stakeOfScope.isEmpty()) stakeOfScope.pop();
	}

	@Override
	public TypeSymbol getType(TId id) {
		VarSymbol nameOfId = VarSymbol.symbol(id);
		Object actualEscope = getActualScope();
		
		if(actualEscope != null) {
			if(actualEscope instanceof Method) {
				TypeSymbol type = ((Method) actualEscope).getTypeOfParameter(id);
				if(type != null) return type;
				
				type = ((Method) actualEscope).getTypeOfLocalVariable(id);
				if(type != null) return type;
			}
			else if(actualEscope instanceof Class) {
				Binding b = ((Class) actualEscope).getLocalVariable(nameOfId);
				if(b != null) return b.getTypeSymbol();
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
			Method m = ((Class) objectClass).getMethod(method);
			if(m != null) return m.getTypeOfReturn();
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
			Method m = ((Class) objectClass).getMethod(method);

			if(m != null) {
				Iterator<Binding> parametersOfMethod = m.getParameters().iterator();
				Iterator<TypeSymbol> elementsOfList = params.iterator();
				
				while(parametersOfMethod.hasNext()) {
					TypeSymbol type = parametersOfMethod.next().getTypeSymbol();
					if(!type.equals(elementsOfList.next())) return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isObject(TId id) {
		Object actualScope = getActualScope();
		if(actualScope != null) {
			if(actualScope instanceof Method) {

				TypeSymbol type = ((Method) actualScope).getTypeOfLocalVariable(id);
				if(type != null && !type.equals(TypeSymbol.getBoolSymbol()) && !type.equals(TypeSymbol.getIntTSymbol()))
					return true;
			}
			else if(actualScope instanceof Class) {

				VarSymbol nameOfID = VarSymbol.symbol(id);
				Binding b = ((Class) actualScope).getLocalVariable(nameOfID);
				if(b != null) {
					TypeSymbol type = b.getTypeSymbol();
					if(type != null && !type.equals(TypeSymbol.getBoolSymbol()) && !type.equals(TypeSymbol.getIntTSymbol()))
						return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isSubclassOf(TypeSymbol supcname, TypeSymbol subcname) {
		VarSymbol nameOfSubClass = VarSymbol.symbolFromString(subcname.toString());
		Object subClass = getObject(nameOfSubClass);

		if(subClass != null && !(subClass instanceof Method)) {
			VarSymbol nameOfSuperClass = VarSymbol.symbolFromString(supcname.toString());
			VarSymbol parentClass = ((Class) subClass).getParentClass();
			if(parentClass.equals(nameOfSuperClass)) return true;
		}
		return false;
	}

	@Override
	public boolean isVar(TId id) {
		Object actualScope = getActualScope();
		if(actualScope != null){
			if(actualScope instanceof Method) {

				TypeSymbol typeOfMethod = ((Method) actualScope).getTypeOfLocalVariable(id);
				if(typeOfMethod != null) return true;
				
				typeOfMethod = ((Method) actualScope).getTypeOfParameter(id);
				if(typeOfMethod != null) return true;
			}
			else if(actualScope instanceof Class) {

				VarSymbol nameOfVariable = VarSymbol.symbol(id);
				Binding b = ((Class) actualScope).getLocalVariable(nameOfVariable);
				if(b != null) return true;
			}
		}
		return false;
	}

}