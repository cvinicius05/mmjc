package ufc.ck017.mmjc.translate;

import ufc.ck017.mmjc.node.*;
import ufc.ck017.mmjc.translate.tree.Exp;

public interface TranslateVisitor{

	public Exp visit(Start node);
	public Exp visit(AProgram node);
	public Exp visit(AMainclass node);
	public Exp visit(ANonextNextclass node);
	public Exp visit(AExtNextclass node);
	public Exp visit(AVar node);
	public Exp visit(AMethod node);
	public Exp visit(AIfStatement node);
	public Exp visit(AVatbStatement node);
	public Exp visit(AAtbStatement node);
	public Exp visit(AWhileStatement node);
	public Exp visit(APrintStatement node);
	public Exp visit(AManyStatement node);
	public Exp visit(AMcallExpression node);
	public Exp visit(APlusExpression node);
	public Exp visit(AMinusExpression node);
	public Exp visit(AMultExpression node);
	public Exp visit(AAndExpression node);
	public Exp visit(AGthanExpression node);
	public Exp visit(ALthanExpression node);
	public Exp visit(AVectorExpression node);
	public Exp visit(ALengthExpression node);
	public Exp visit(ANotExpression node);
	public Exp visit(ANewobjExpression node);
	public Exp visit(ANewvecExpression node);
	public Exp visit(AVarExpression node);
	public Exp visit(ANumberExpression node);
	public Exp visit(ABtrueExpression node);
	public Exp visit(ABfalseExpression node);
	public Exp visit(ASelfExpression node);
	public Exp visit(AInttType node);
	public Exp visit(AIntvType node);
	public Exp visit(ABoolType node);
	public Exp visit(AClassType node);
}
