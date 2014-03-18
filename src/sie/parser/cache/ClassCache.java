package sie.parser.cache;

import sie.db.entity.SType;

/**
 * Classi cache per salvare le classi appena create, per non doverle ricreare in seguito.
 * */
public class ClassCache {
	private ClassCache() {
		cache = new BeanCache<>();
	}

	public static BeanCache<SType> getCache() {
		if (cc == null)
			cc = new ClassCache();
		return cc.cache;
	}

	private static ClassCache cc;
	private BeanCache<SType> cache;
}
