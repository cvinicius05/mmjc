package ufc.ck017.mmjc.instructionSelection.assem;

import java.util.List;
import java.util.ListIterator;

import ufc.ck017.mmjc.activationRecords.temp.Label;
import ufc.ck017.mmjc.activationRecords.temp.LabelList;
import ufc.ck017.mmjc.activationRecords.temp.Temp;
import ufc.ck017.mmjc.activationRecords.temp.TempMap;

public abstract class Instr {
	public String assem;

	public abstract List<Temp> use();
	public abstract List<Temp> def();
	public abstract Targets jumps();

	private Label nthLabel(LabelList l, int i) {
		while(i > 0 && l != null) {
			l = l.tail;
			i--;
		}
		
		return (l == null ? null : l.head);
	}

	public void replaceUse(Temp olduse, Temp newuse) {
		ListIterator<Temp> src = use().listIterator();
		
		while(src.hasNext())
			if (src.next() == olduse) src.set(newuse);
	}
	
	public void replaceDef(Temp olddef, Temp newdef) {
		ListIterator<Temp> dst = def().listIterator();
		
		while(dst.hasNext())
			if (dst.next() == olddef) dst.set(newdef);
	}

	public String format(TempMap m) {
		List<Temp> dst = def();
		List<Temp> src = use();
		Targets j = jumps();
		LabelList jump = (j==null) ? null : j.labels;
		
		StringBuffer s = new StringBuffer();
		
		int len = assem.length();
		
		for(int i=0; i<len; i++)
			if (assem.charAt(i) == '`') {
				int n;
				switch (assem.charAt(++i)) {
					case 's':
						n = Character.digit(assem.charAt(++i), 10);
						s.append(m.tempMap(src.get(n)));
						break;
					case 'd':
						n = Character.digit(assem.charAt(++i), 10);
						s.append(m.tempMap(dst.get(n)));
						break;
					case 'j':
						n = Character.digit(assem.charAt(++i), 10);
						s.append(nthLabel(jump, n).toString());
						break;
					case '`':
						s.append('`');
						break;
					default:
						throw new Error("bad Assem format");
				}
			} else s.append(assem.charAt(i));

		return s.toString();
	}
}
