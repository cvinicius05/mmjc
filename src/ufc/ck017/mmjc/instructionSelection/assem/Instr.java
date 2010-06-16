package ufc.ck017.mmjc.instructionSelection.assem;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.LabelList;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.activationRecords.temp.TempList;
import ufc.ck017.mmjc.activationRecords.temp.TempMap;

public abstract class Instr {
	public String assem;

	public abstract TempList use();
	public abstract TempList def();
	public abstract Targets jumps();

	private Temp nthTemp(TempList l, int i) {
		while(i > 0 && l != null) {
			l = l.tail;
			i--;
		}
		
		return (l == null ? null : l.head);
	}

	private Label nthLabel(LabelList l, int i) {
		while(i > 0 && l != null) {
			l = l.tail;
			i--;
		}
		
		return (l == null ? null : l.head);
	}

	public void replaceUse(Temp olduse, Temp newuse) {
		TempList src = use();
		while(src != null) {
			if (src.head == olduse) src.head = newuse;
			src = src.tail;
		}
	}
	public void replaceDef(Temp olddef, Temp newdef) {
		TempList dst = def();
		while(dst != null) {
			if (dst.head == olddef) dst.head = newdef;
			dst = dst.tail;
		}
	}

	public String format(TempMap m) {
		TempList dst = def();
		TempList src = use();
		Targets j = jumps();
		LabelList jump = (j==null)?null:j.labels;
		StringBuffer s = new StringBuffer();
		int len = assem.length();
		for(int i=0; i<len; i++)
			if (assem.charAt(i)=='`') {
				int n;
				switch(assem.charAt(++i)) {
				case 's': 
					n = Character.digit(assem.charAt(++i),10);
					s.append(m.tempMap(nthTemp(src,n)));
					break;
				case 'd': 
					n = Character.digit(assem.charAt(++i),10);
					s.append(m.tempMap(nthTemp(dst,n)));
					break;
				case 'j':
					n = Character.digit(assem.charAt(++i),10);
					s.append(nthLabel(jump,n).toString());
					break;
				case '`':
					s.append('`'); 
				break;
				default: throw new Error("bad Assem format");
				}
			} else s.append(assem.charAt(i));

		return s.toString();
	}
}
