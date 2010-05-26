package ufc.ck017.mmjc.translate.tree;

public class Nx extends Exp {

	//atributos da classe
	private Stm stm;

	//construtor
	public Nx(Stm stm){
		this.stm = stm;
	}

	@Override
	public Exp build(ExpList kids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpList kids() {
		// TODO Auto-generated method stub
		return null;
	}


	public Stm unNx(){
		return stm;
	}

	public Exp unEx(){
		return new ESEQ(stm, new CONST(0));
	}

}
