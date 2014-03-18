package sie.parser.java.comments;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map.Entry;

import sie.db.entity.CodeComment;

/**
 * Classe che implementa una mappa con chiavi duplicate
 * */

public class CommentsMultiMap {
	public CommentsMultiMap() {
		map = new HashMap<>();
	}

	public void put(String k, CodeComment v) {
		Set<CodeComment> ccb = map.get(k);
		if (ccb == null) {
			ccb = new HashSet<>();
			map.put(k, ccb);
		}
		ccb.add(v);
		size++;
	}

	public Set<CodeComment> get(String key) {
		return map.get(key);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(Entry<String, Set<CodeComment>> e : map.entrySet()) {
			String tmp = e.getKey() + " => ";
			for(CodeComment cb : e.getValue())
				tmp += cb.getComment() + " | ";
			s += tmp + "\n";
		}
		return s;
	}

	private HashMap<String, Set<CodeComment>> map;
	private int size;
}
