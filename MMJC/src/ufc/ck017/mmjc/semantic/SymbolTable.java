package ufc.ck017.mmjc.semantic;

public interface SymbolTable {
	// TODO Substituir os Object por Symbol e defini-la
	public boolean isMethod(Object c, Object m);
	public boolean isClass(Object s);
	// Deve retornar o tipo INVALID se nao existir
	public Object getType(Object c, Object m, Object s);
}
