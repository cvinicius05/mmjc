package ufc.ck017.mmjc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PushbackReader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import ufc.ck017.mmjc.activationRecords.frame.JouetteFrame;
import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.emitting.regalloc.RegAlloc;
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
			String infilename = new Scanner(System.in).nextLine();
			String outfilename = new String(infilename.substring(0, infilename.lastIndexOf('.'))+".S");
			Writer out = new BufferedWriter(new FileWriter(outfilename), 4096);
			Lexer l = new Lexer (new PushbackReader (new BufferedReader(new FileReader(infilename)), 4096));
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
				List<Instr> instr = f.getframe().procEntryExit2(
						f.getframe().codegen(
								new TraceSchedule(
										new BasicBlocks(
												Canon.linearize(f.getBody())
										)
								).stms
						)
				);
				
				new RegAlloc(f.getframe(), instr);
				
				instr = f.getframe().procEntryExit3(instr);
				
				for(Instr i : instr)
					out.write(i.format(f.getframe()));
			}
			
			/*for(List<Instr> list : ilistlist) {
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
			}*/
				
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
