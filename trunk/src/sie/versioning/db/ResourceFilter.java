package sie.versioning.db;

import java.util.Set;

import sie.db.entity.ChangedResource;

/**
 * Interfaccia che descrive un generico filtro per individuare specifiche
 * risorse da un insieme dato.
 * 
 * @author Santolo Tubelli.
 * */
public interface ResourceFilter {

	/**
	 * Prende in input un insieme di risorse e ne restituisce un insieme
	 * filtrato.
	 * 
	 * @param resources
	 *            le risorse a cui applicare il filtro.
	 * @return un insieme di dimensione minore o uguale a quello di partenza in
	 *         cui ci sono solo le risorse che corrispondono allo specifico
	 *         filtro
	 * */
	Set<ChangedResource> filter(Set<ChangedResource> resources);
}
