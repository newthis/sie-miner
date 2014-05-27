package sie.parser.cache;

import sie.db.entity.SourceContainer;

/**
 * Classe che funge da cache per salvare i pacchetti creati
 * */
public class PackageCache {
	private PackageCache() {
		cache = new BeanCache<>();
	}

	public static BeanCache<SourceContainer> getCache() {
		if (pc == null)
			pc = new PackageCache();
		return pc.cache;
	}

	private static PackageCache pc;
	private BeanCache<SourceContainer> cache;
}
