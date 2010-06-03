package ufc.ck017.mmjc;

import java.io.*;
import java.util.Scanner;

import ufc.ck017.mmjc.parser.*;
import ufc.ck017.mmjc.semantic.TableVisitor;
import ufc.ck017.mmjc.semantic.TypeChecker;
import ufc.ck017.mmjc.translate.Translate;
import ufc.ck017.mmjc.util.ErrorLog;
import ufc.ck017.mmjc.lexer.*;
import ufc.ck017.mmjc.node.*;

public class MMJC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			  String filename = new Scanner(System.in).nextLine();
		      Lexer l = new Lexer (new PushbackReader (new BufferedReader(new FileReader(filename)), 4096));
		      Parser p = new Parser (l);
		      Start start = p.parse ();
		      
		      ErrorLog err = ErrorLog.getInstance();
		      
		      start.apply (new TableVisitor());
		      start.apply(new TypeChecker());
		      
		      if(err.printErrors() > 0)
		    	  System.exit(1);
		      
		      // Tem que ficar aqui por conta da inicialização da tabela de símbolos
		      Translate t = new Translate();
		      start.apply(t);

		    } catch (Exception e) {
		      e.printStackTrace();
		    }

	}

}
