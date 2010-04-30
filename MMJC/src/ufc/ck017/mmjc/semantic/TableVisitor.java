package ufc.ck017.mmjc.semantic;

import java.util.LinkedList;
import java.util.Stack;

import ufc.ck017.mmjc.analysis.DepthFirstAdapter;
import ufc.ck017.mmjc.node.AExtNextclass;
import ufc.ck017.mmjc.node.ANonextNextclass;
import ufc.ck017.mmjc.node.AProgram;
import ufc.ck017.mmjc.node.Node;
import ufc.ck017.mmjc.node.PNextclass;
import ufc.ck017.mmjc.symbolTable.Class;

/**
 * 
 * @author Arthur Rodrigues
 * @author Carlos Vinicius
 *
 */
public class TableVisitor extends DepthFirstAdapter {
	private static LinkedList<Class> auxiliarTable = new LinkedList<Class>(); // Lista de classes que não extendem outras classes.
	private static LinkedList<Class> extendedNotDefined = new LinkedList<Class>(); // Lista de classes que extedem classes não declaradas.
	private static Stack<Node> scopeStack = new Stack<Node>();
	
	@Override
	public void inANonextNextclass(ANonextNextclass node) {
		Class newclass = null;
		int numVars = 0, numMethods = 0;
	
		numVars = node.getVar().size();
		numMethods = node.getMethod().size();
		
		newclass = new Class(null, node.getId(), numVars, numMethods);
		TableVisitor.auxiliarTable.add(newclass);
		TableVisitor.scopeStack.push(node);
	}
	
	public void manageExtList(Class c, Class p) {
		// TODO verificar se c esta na lista das ainda nao definidas
		// TODO se estiver, remover da lista e materializar a classe na tabela
	}
}
