class MainClass {
	public static void main(String[] args) {
		System.out.println(new Child().returnInt());
	}
}

class Child extends Parent {
	int i;
	Parent parent;
	GrandParent g;
	
	public Child returnItself() {
		while(!b) {
			i = i*this.returnInt();
		}
		return this;
	}
	
	public GrandParent getGrandParent() {
		return g.returnSelf(g);
	}
	
	public boolean setParent(Parent p) {
		parent = p;
		return true;
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
		int i;
		i = c.getIntOfParent();
		return new GrandParent();
	}
	
	public int returnInt() {
		return intarray[this.returnArray(true)[6]+15*9];
	}
}

class GrandParent {
	int[] intarray;
	Child c;
	
	public int[] returnArray(boolean b) {
		int[] intarray;
		
		intarray = new int[ 20+3*5 ];
		
		while(b && 22 < 3) {
			b = !b;
			intarray[12] = c.getIntOfParent() + this.returnSelf(this).returnInt();
		}
		
		return intarray; 
	}
	
	public int returnInt() {
		return this.returnSelf(this).returnInt()*intarray[this.returnArray(true)[c.getGrandParent().returnInt()]+15*c.getIntOfParent()];
	}
	
	public GrandParent returnSelf(GrandParent o) {
		return this;
	}
}
