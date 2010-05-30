package ufc.ck017.mmjc.activationRecords.frame;

import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.translate.assem.Instr;
import ufc.ck017.mmjc.translate.tree.Exp;
import ufc.ck017.mmjc.translate.tree.Stm;
import ufc.ck017.mmjc.util.Symbol;

public abstract class Frame {
	  public List<Access> formals;
	  public Label name;
	  public abstract Frame newFrame(Symbol name, List<Boolean> formals);
	  public abstract Access allocLocal(boolean escape);
	  public abstract Temp FP();
	  public abstract int wordSize();
	  public abstract Exp externalCall(String func, List<Exp> args);
	  public abstract Temp RV();
	  public abstract void procEntryExit1(List<Stm> body);
	  public abstract void procEntryExit2(List<Instr> body);
	  public abstract void procEntryExit3(List<Instr> body);
}
