class MainClass {
	public static void main(String[] args) {
		System.out.println(new Child().returnInt());
	}
}

class Child extends Parent {
	int i;
	Parent parent;
	GrandParent g;
	
	public Child(int number, Parent p, GrandParent g) {
		this.i = number;
		parent = p;
		this.g = g;
	}
	
	public Child returnItself() {
		while(!b) {
			i = i*this.returnInt();
		}
		return this;
	}
	
	public GrandParent getGrandParent() {
		return g.returnSelf(g);
	}
	
	public int getIntOfParent() {
		return g.returnInt() + p.returnInt();
	}
	
	public int dummy() {
		return p.returnParent().returnSelf(this).returnInt();
	}
}

class Parent extends GrandParent{
	boolean b;
	Parent p;
	Child c;
	
	public GrandParent returnParent() {
		c.getIntOfParent();
		return new GrandParent();
	}
	
	public Parent(boolean b, Parent p, Child c) {
		this.b = b;
		this.p = p;
		this.c = c;
	}
	
	public int returnInt() {
		return intarray[this.returnArray(true)[6]+15*9];
	}
}

class GrandParent {
	int[] intarray;
	Child c;

	public GrandParent(int[] v, Child c) {
		intarray = v;
		this.c = c;
	}
	
	public int[] returnArray(boolean b) {
		int[] intarray;
		
		intarray = new int[ 20+3*5 ];
		
		while(b && 22 < 3) {
			b = !b;
			c.getIntOfParent() = this.c.getIntOfParent() + returnSelf(this).returnInt();
		}
		
		return intarray; 
	}
	
	public int returnInt() {
		return returnSelf(this).intarray[this.returnArray(true)[c.getGrandParent().returnInt()]+15*this.c.getIntOfParent()];
	}
	
	public GrandParent returnSelf(GrandParent o) {
		return this;
	}
}
