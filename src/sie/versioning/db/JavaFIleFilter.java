package sie.versioning.db;

import java.util.HashSet;
import java.util.Set;

import sie.db.entity.ChangedResource;

/**
 * Filtro che seleziona tutti i file .java da una lista di risorse.
 * 
 * @author Santolo Tubelli.
 * */
public class JavaFIleFilter implements ResourceFilter {

	/**
	 * Prende in input un {@link Set} di {@link ChangedResource} e restituisce
	 * una collezione formata dalle sole risorse .java
	 * 
	 * @param res
	 *            è l'insieme da cui filtrare i file .java
	 * @return la collezione di file java filtrati dall'insieme res.
	 * */
	@Override
	public Set<ChangedResource> filter(Set<ChangedResource> res) {
		Set<ChangedResource> toRet = new HashSet<>();
		for (ChangedResource s : res) {
			if (s.getFileName().endsWith(".java")
					&& !toRet.contains(s.getFileName()))
				toRet.add(s);
		}
		return toRet;
	}

}
