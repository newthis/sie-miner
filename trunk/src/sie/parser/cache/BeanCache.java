package sie.parser.cache;

import java.util.HashMap;
import java.util.Map;

import sie.parser.utils.Name;

/**
 * Classe che funge da cache per salvare i bean creati
 * */
public class BeanCache<T> {
	public BeanCache() {
		map = new HashMap<>();
	}

	public void put(Name n, T pb, boolean isComplete) {
		CEntry ce = map.get(n);

		if (ce != null) {
			ce.isComplete = isComplete;
		} else {
			ce = new CEntry();
			ce.isComplete = isComplete;
			ce.upb = pb;
			ce.key = n;

			map.put(n, ce);
		}

		if (ce != null)
			last = ce;
	}

	public T get(Name n) {
		if (last != null && last.key.equals(n))
			return last.upb;
		CEntry tmp = map.get(n);
		if(tmp != null)
			last = tmp;
		return tmp != null? tmp.upb : null;
	}

	public boolean isComplete(Name n) {
		if (last.key.equals(n))
			return last.isComplete;
		last = map.get(n);
		return last == null ? null : last.isComplete;
	}

	public void clear() {
		map.clear();
		last = null;
	}

	private class CEntry {
		Name key;
		boolean isComplete;
		T upb;
	}

	private Map<Name, CEntry> map;
	private CEntry last;
}
