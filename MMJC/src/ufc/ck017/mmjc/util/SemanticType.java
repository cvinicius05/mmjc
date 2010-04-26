package ufc.ck017.mmjc.util;

public enum SemanticType {
	NONE("none", 0),           // Para producoes que nao possuem tipo (como DECLARACOES de classes, metodos e variaveis)
	INVALID("invalid", 1),     // Para nos cuja declaracao do tipo eh invalida ou torna-se invalida (como expressoes incorretas)
	INT("integer", 2),
	INTV("integer array", 3),
	BOOL("boolean", 4),
	CLASS("class", 5),        // Para DECLARACOES de classes
	OBJECT("object", 6);      // Para INSTANCIAMENTOS de classes, ou seja, objetos de uma classe
	
	private String type = null;
	private int code = -1;
	
	private SemanticType(String texttype, int c) {
		type = texttype;
		code = c;
	}
	
	public boolean equals(SemanticType st) {
		return this.code == st.code; 
	}
	
	public String toString() {
		return type;
	}
}