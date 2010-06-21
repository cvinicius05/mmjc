package ufc.ck017.mmjc.activationRecords.frame;

import java.util.List;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.activationRecords.temp.TempMap;
import ufc.ck017.mmjc.instructionSelection.assem.Instr;
import ufc.ck017.mmjc.translate.tree.Exp;
import ufc.ck017.mmjc.translate.tree.Stm;
import ufc.ck017.mmjc.translate.tree.StmList;

public abstract class Frame implements TempMap{

	public Label name;
	public List<Access> formals;

	public abstract Temp RV();
	public abstract Temp FP();
	public abstract int wordSize();

	public abstract Temp[] registers();
	public abstract String tempMap(Temp temp);

	public abstract Frame newFrame(Label name, List<Boolean> formals);
	public abstract Access allocLocal(boolean escape);
	public abstract Exp externalCall(String func, List<Exp> args);
	
	public abstract List<Stm> procEntryExit1(List<Stm> body);
	public abstract List<Instr> procEntryExit2(List<Instr> body);
	public abstract List<Instr> procEntryExit3(List<Instr> body);
	public abstract List<Instr> codegen(StmList stms);
}
