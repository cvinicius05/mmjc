package ufc.ck017.mmjc.translate.tree;

abstract public class Stm {

	abstract public ExpList kids();
	abstract public Stm build(ExpList kids);
}
