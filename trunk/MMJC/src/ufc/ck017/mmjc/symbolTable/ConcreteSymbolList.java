package ufc.ck017.mmjc.symbolTable;

import java.util.Iterator;
import java.util.LinkedList;

import ufc.ck017.mmjc.node.AExtNextclass;
import ufc.ck017.mmjc.node.ANonextNextclass;
import ufc.ck017.mmjc.node.AProgram;
import ufc.ck017.mmjc.node.PNextclass;
import ufc.ck017.mmjc.node.PProgram;
import ufc.ck017.mmjc.util.SemanticError;

/**
 * Classe que representa a tabela de s&iacute;mbolos.
 * Por&eacute;m, a tabela de s&iacute;mbolos &eacute;
 * dada como uma <i>LinkedList</i> de Class que n&atilde;o
 * extendem outras classes.
 * <p>
 * A classe possui uma lista auxiliar
 * de classes que extendem outras classes, cujas
 * declara&ccedil;&otilde;es n&atilde;o foram feitas 
 * at&eacute; o momento de suas declara&ccedil;&otilde;es.
 * 
 * @author vinicius
 *
 */
public class ConcreteSymbolList {

	private static LinkedList<Class> auxiliarTable; // Lista de classes que não extendem outras classes.
	private static LinkedList<Class> extendedNotDefined;// Lista de classes que extedem classes não declaradas.
	
	public ConcreteSymbolList() {
		auxiliarTable = new LinkedList<Class>();
		extendedNotDefined = new LinkedList<Class>();
	}

	/**
	 * M&eacute;todo que gera a tabela auxiliar a partir
	 * de uma produ&ccedil;&atilde;o do tipo <b>PProgram</b>.
	 * <p>
	 * A medida que s&atilde;o lidas as classes da lista de
	 * classes de PProgram, s&atilde;o gerados elementos do
	 * tipo {@link Class} com seus m&eacute;todos e vari&aacute;veis
	 * locais.
	 * <p>
	 * Se a classe a ser declarada extende outra classe,
	 * verificamos se esta j&aacute; est&aacute; declarada na
	 * tabela auxiliar. Se n&atilde;o estiver declarada, procuramos a
	 * classe extendida pela nova classe atav&eacute;s de uma busca
	 * em profundidade pelas classes e suas extens&otilde;es. Se
	 * encontramos a classe que a extende, a alocamos como classe que
	 * extende esta classe e buscamos na lista <i>extendedNotDefined</i>
	 * se h&aacute; alguma classe que extende a classe recentemente inserida.
	 * Se n&atilde;o encontramos a declara&ccedil;o da classe pai, inserimos
	 * a classe declarada em <i>extendedNotDefined</i>.
	 * 
	 * @param program produ&ccedil;&atilde;o do tipo <b>PProgram</b>.
	 * @return Lista de classes que representa a tabela de s&iacute;mbolos
	 * @see PProgram
	 * @see AProgram
	 */
	public LinkedList<Class> createAuxiliarList(PProgram program) {
		Iterator<PNextclass> iter = ((AProgram) program).getNextclass().iterator();
		PNextclass nextClass = null;

		while(iter.hasNext()) {
			nextClass = iter.next();
			Class newClass = new Class(nextClass);

			if(nextClass instanceof ANonextNextclass) {
				auxiliarTable.add(newClass);
				removeFromExtendedNotDefined(newClass);
			}

			else if(nextClass instanceof AExtNextclass) {
				if(!auxiliarTable.isEmpty()) {
					Iterator<Class> iter2 = auxiliarTable.iterator();
					boolean found = false;

					while(iter2.hasNext()) {
						Class auxClass = iter2.next();

						if(auxClass.getName().equals(newClass.getParentClass())) {
							auxClass.setExtendedList(newClass);
							removeFromExtendedNotDefined(newClass);
							found = true;
							break;
						}
						else if(setExtendedClass(auxClass, newClass)){
							found = true;
							break;
						}
					}
					if(!found) extendedNotDefined.add(newClass);
				}
				else extendedNotDefined.add(newClass);
			}
		}
		
		// TODO lançar o erro para a lista
		if(!extendedNotDefined.isEmpty()) return null; // Existe uma classe cuja classe pai nao foi declarada.
		return auxiliarTable;
	}
	
	/**
	 * M&eacute;todo que verifica se <code>newClass</code> &eacute; uma classe
	 * que extende a classe <code>aux</code>.
	 * &Eacute; feita uma busca em profundidade nas extens&otilde;es das
	 * classes. Se a classe pai &eacute; encontrada, alocamos a nova classe
	 * &agrave; lista de classes extensoras da classe pai e removemos as
	 * classes que extendem a nova classe e que estam na lista 
	 * <code>extendedNotDefined</code>. 
	 * 
	 * @param aux Classe a ser verificada como pai de <code>newClass</code>. 
	 * @param newClass Nova classe a ser inserida como extensora de outra classe;
	 * @return true se a classe pai &eacute; econtrada e false caso contr&aacute;rio.
	 */
	private boolean setExtendedClass(Class aux, Class newClass) {
		if(!aux.getExtendedClass().isEmpty()) {
			Iterator<Class> iter = aux.getExtendedClass().iterator();
			
			while(iter.hasNext()) {
				Class aux2 = iter.next();
				
				if(aux2.getName().equals(newClass.getParentClass())) {
					aux2.setExtendedList(newClass);
					removeFromExtendedNotDefined(newClass);
					return true;
				}
				else if(setExtendedClass(aux2, newClass)) return true;
			}
		}
		return false;
	}
	
	/**
	 * M&eacute;todo que remove as classes contidas em <i>extendedNotDefined</i>
	 * cuja classe pai &eacute; <i>newClass</i>. As classes removidas s&atilde;o
	 * inseridas na lista de classes extensoras de <i>newClass</i>.
	 * 
	 * @param newClass Classe rec&eacute;m adicionada a tabela de s&iacute;mbolos.
	 */
	private void removeFromExtendedNotDefined(Class newClass) {
		if(!extendedNotDefined.isEmpty()) {

			// Lista auxiliar de classes que extendem newClass e que estão em extendedNotDefined.
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
				while(iter.hasNext()) removeFromExtendedNotDefined(iter.next());					
			}
		}
	}
}