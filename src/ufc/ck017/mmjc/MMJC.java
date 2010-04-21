package ufc.ck017.mmjc;

import java.io.*;
import java.util.Scanner;

import ufc.ck017.mmjc.parser.*;
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

		      ASTDisplay ad = new ASTDisplay ();
		      start.apply (ad);

		    } catch (Exception e) {
		      e.printStackTrace();
		    }

	}

}
