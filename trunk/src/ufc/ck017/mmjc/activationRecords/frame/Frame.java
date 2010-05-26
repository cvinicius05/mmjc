package ufc.ck017.mmjc.activationRecords.frame;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.activationRecords.util.BoolList;

public abstract class Frame {
	  public AccessList formals;
	  public Label name;
	  public abstract Frame newFrame(Label name, BoolList formals);
	  public abstract Access allocLocal(boolean escape);
	  abstract public Temp FP();
	  abstract public int wordSize();
	  //abstract public Tree.Exp externalCall(String func, Tree.ExpList args);
	  abstract public Temp RV();
	  //abstract public Tree.Stm procEntryExit1(Tree.Stm body);
}
