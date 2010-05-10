class MainClass {
	public static void main(String[] args) {
		System.out.println(new Child().returnInt());
	}
}

class Parent extends GrandParent{
	boolean b;
	Parent p;
	
	public GrandParent returnParent() {
		return new GrandParent();
	}
	
	public int returnInt() {
		return intarray[this.returnArray(true)[6]+15*9];
	}
}

class Child extends Parent {
	int i;
	
	public Child returnItself() {
		while(!b) {
			i = i*this.returnInt();
		}
		return this;
	}
	
	public int dummy() {
		return p.returnParent().returnSelf(this).returnInt();
	}
}

class GrandParent {
	int[] intarray;

	public int[] returnArray(boolean b) {
		int[] intarray;
		
		intarray = new int[ 20+3*5 ];
		
		while(b && 22 < 3) {
			b = !b;
		}
		
		return intarray; 
	}
	
	public int returnInt() {
		return intarray[this.returnArray(true)[6]+15*9];
	}
	
	public GrandParent returnSelf(GrandParent o) {
		return this;
	}
}