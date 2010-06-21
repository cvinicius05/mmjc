class Dummy {
	public static void main(String[] args) {
		System.out.println(new Exec().loop(true));
	}
}
class Exec {
	Exec e;
	int v;
	boolean x;
	
	public int loop(boolean b) {
		while(b) {
			b = this.negate(b);
		}
		
		return 22-this.loop(!b);
	}
	
	public boolean negate(boolean b) {
		boolean c;
		if(b && true) {
			c = false;
		} else {
			c = true && b;
		}
		
		return c;
	}
}
