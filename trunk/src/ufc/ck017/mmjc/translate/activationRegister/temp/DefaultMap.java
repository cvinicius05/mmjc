package ufc.ck017.mmjc.translate.activationRegister.temp;

public class DefaultMap implements TempMap{

	@Override
	public String tempMap(Temp t) {
		return t.toString();
	}

	public DefaultMap() {}
}
