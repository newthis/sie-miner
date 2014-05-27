package sie.parser.cache;

import sie.db.entity.Method;

/**
 * Classe cache per salvare i MethodBean gi� creati
 * */
public class MethodCache {
	private MethodCache() {
		cache = new BeanCache<>();
	}

	public static BeanCache<Method> getCache() {
		if (pc == null)
			pc = new MethodCache();
		return pc.cache;
	}

	private static MethodCache pc;
	private BeanCache<Method> cache;
}
