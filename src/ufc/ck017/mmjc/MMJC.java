package ufc.ck017.mmjc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PushbackReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.instructionSelection.assem.AMOVE;
import ufc.ck017.mmjc.instructionSelection.assem.Instr;
import ufc.ck017.mmjc.instructionSelection.assem.OPER;
import ufc.ck017.mmjc.lexer.Lexer;
import ufc.ck017.mmjc.node.Start;
import ufc.ck017.mmjc.parser.Parser;
import ufc.ck017.mmjc.semantic.TableVisitor;
import ufc.ck017.mmjc.semantic.TypeChecker;
import ufc.ck017.mmjc.translate.Frag;
import ufc.ck017.mmjc.translate.Translate;
import ufc.ck017.mmjc.translate.canonicalize.BasicBlocks;
import ufc.ck017.mmjc.translate.canonicalize.Canon;
import ufc.ck017.mmjc.translate.canonicalize.TraceSchedule;
import ufc.ck017.mmjc.util.ErrorLog;

public class MMJC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LinkedList<List<Instr>> ilistlist = new LinkedList<List<Instr>>();
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

			for(Frag f : t.getResult()) {
				ilistlist.add(
						f.getframe().procEntryExit3(
								f.getframe().procEntryExit2(f.getframe().codegen(
								new TraceSchedule(
										new BasicBlocks(
												Canon.linearize(f.getBody())
										)
								).stms
						)))
				);
			}
			
			for(List<Instr> list : ilistlist) {
				for(Instr instr : list) {
					System.out.print((instr.assem == "" ? "EMPTYYYYY\n" : instr.assem));
					if(instr instanceof AMOVE)
						System.out.println("\t["+((AMOVE)instr).dst+" # "+((AMOVE)instr).src+"]");
					else if(instr instanceof OPER) {
						System.out.print("\t[");
						if(instr.def() != null)
							for(Temp d : instr.def())
								System.out.print(d+", ");
						System.out.print(" # ");
						if(instr.use() != null)
							for(Temp s : instr.use())
								System.out.print(s+", ");
						if(instr.jumps() != null)
							for(Label j : instr.jumps())
								System.out.print(j+", ");
						System.out.println("]");
					}
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
