package ufc.ck017.mmjc.semantic;

import java.util.Hashtable;
import java.util.LinkedList;

import ufc.ck017.mmjc.analysis.DepthFirstAdapter;
import ufc.ck017.mmjc.node.AExtNextclass;
import ufc.ck017.mmjc.node.AMethod;
import ufc.ck017.mmjc.node.ANonextNextclass;
import ufc.ck017.mmjc.node.AProgram;
import ufc.ck017.mmjc.node.AVar;
import ufc.ck017.mmjc.node.PVar;
import ufc.ck017.mmjc.symbolTable.Class;
import ufc.ck017.mmjc.symbolTable.Method;
import ufc.ck017.mmjc.symbolTable.SymbolTable;
import ufc.ck017.mmjc.symbolTable.TypeSymbol;
import ufc.ck017.mmjc.util.ErrorLog;
import ufc.ck017.mmjc.util.SemanticError;

/**
 * Classe que implementa o visitor de gera&ccedil;&atilde;o
 * da tabela de s&iacute;mbolos onde &eacute; feito o percurso
 * dos n&oacute;s da AST.
 * 
 * @author Arthur Rodrigues
 * @author Carlos Vinicius
 *
 */
public class TableVisitor extends DepthFirstAdapter {

	// Conjunto de classes que estedem outras ainda não declaradas.
	private static Hashtable<TypeSymbol, Class> extendedNotDefined = new Hashtable<TypeSymbol, Class>();

	private static Class currentclass = null;
	private static SymbolTable table = null;
	private static ErrorLog errors = ErrorLog.getInstance();

	@Override
	public void inAProgram(AProgram node) {
		SymbolTable.setSizeIfUndefined(node.getNextclass().size());
		table = SymbolTable.getInstance();
	}

	@Override
	public void outAProgram(AProgram node) {
		if(!extendedNotDefined.isEmpty()) {
			errors.addError(SemanticError.notDefinedClasses(extendedNotDefined.elements()));
		}
	}

	@Override
	public void inANonextNextclass(ANonextNextclass node) {
		Class newclass = null;
		int numVars = 0, numMethods = 0;

		numVars = node.getVar().size();
		numMethods = node.getMethod().size();

		newclass = new Class(null, node.getId(), numVars, numMethods);

		if(table.getClass(newclass.getName()) != null)
			errors.addError(SemanticError.alreadyDefinedClass(node.getId()));

		manageClassTables(newclass, node.getVar());
	}

	@Override
	public void inAExtNextclass(AExtNextclass node) {
		Class newclass = null, pclass = null;
		TypeSymbol psymbol = TypeSymbol.search(node.getExt());

		int numVars = node.getVar().size();
		int numMethods = node.getMethod().size();

		if(psymbol != null) {
			pclass = extendedNotDefined.get(psymbol);
			if(pclass == null) pclass = table.getClass(psymbol);
		}
		else {
			psymbol = TypeSymbol.symbol(node.getExt().getText());
			pclass = new Class(null, psymbol);
			extendedNotDefined.put(psymbol, pclass);
		}

		newclass = new Class(pclass, node.getName(), numVars, numMethods);

		if(table.getClass(newclass.getName()) != null)
			errors.addError(SemanticError.alreadyDefinedClass(node.getName()));

		manageClassTables(newclass, node.getVar());
	}

	@Override
	public void outAMethod(AMethod node) {
		Method newm = new Method(node.getId(), currentclass, node.getType(), node.getParam().size(), node.getLocal().size());

		for(PVar var : node.getLocal()) {
			if(!newm.addLocalVar((AVar) var))
				errors.addError(SemanticError.alreadyDefinedVar(((AVar)var).getId()));
		}

		for(PVar var : node.getParam()) {
			if(!newm.addParamater((AVar) var))
				errors.addError(SemanticError.alreadyDefinedVar(((AVar)var).getId()));
		}

		if(!currentclass.addMethod(newm)) {
			errors.addError(SemanticError.alreadyDefinedMethod(node.getId()));
		}
	}

	/**
	 * Se uma classe est&aacute; na lista de fantasmas, ela
	 * &eacute; removida e &eacute; inserida na tabela de
	 * s&iacute;mbolos como uma classe materializada e com
	 * suas vari&aacute;veis locais.
	 * 
	 * @param newclass classe atualmente declarada
	 * @param vars n&oacute; de vari&aacute;veis.
	 */
	private void manageClassTables(Class newclass, LinkedList<PVar> vars) {
		Class actualclass = null;

		for(PVar var : vars) {
			if(!newclass.addLocalVar((AVar) var))
				errors.addError(SemanticError.alreadyDefinedVar(((AVar)var).getId()));
		}

		actualclass = extendedNotDefined.get(newclass.getName());

		if(actualclass != null) {
			actualclass.materialize(newclass);
			extendedNotDefined.remove(actualclass.getName());
		} else actualclass = newclass;

		if(actualclass.getParent() != null) {
			actualclass.getParent().setChildrenClass(actualclass);
		}

		table.addClass(actualclass);
		currentclass = actualclass;
	}

}