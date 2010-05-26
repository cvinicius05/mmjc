package ufc.ck017.mmjc.activationRecords.temp;

public class Temp {

	private static int count;
	private int num;
	public boolean spillTemp;

	public String toString() {
		return "t" + num;
	}

	public Temp() { 
		num=count++;
	}
}
