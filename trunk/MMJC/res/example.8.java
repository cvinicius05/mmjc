/* vala porque */
// Pode não é?
class Factorial {
	public static void main(String[] args) {
		System.out.println(new Fac().ComputeFac(10));
	}
}
/* Passei por aquí, com acento no i */
// oh nóis aqui também
// E de novo
/* chega! **/

class Fac {
	public int ComputeFac(int num) {
/* Funiona entre código */
// Assim também
		int num_aux;
		if(num < 1)
			num_aux = 1;
// Assim não funciona /* lálálá /* pirumpixi */ */
		else
			num_aux = num * (this.ComputeFac(num-1));
		return num_aux;
	}
}

/* Acabou!
 * 
 * */
// Foi meu fi!
/* acabou mesmo? */