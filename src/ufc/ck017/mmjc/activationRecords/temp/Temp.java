package ufc.ck017.mmjc.activationRecords.temp;

public class Temp implements Comparable<Temp> {

	private static int count;
	private int num;
	public boolean spillTemp;

	public String toString() {
		return "t" + num;
	}

	public Temp() { 
		num=count++;
	}

	@Override
	public int compareTo(Temp t) {
		return this.num - t.num;
	}
	
	@Override
	public boolean equals(Object t) {
		return super.equals(t) || ((t instanceof Temp) && (this.num == ((Temp)t).num));
	}
	
}
