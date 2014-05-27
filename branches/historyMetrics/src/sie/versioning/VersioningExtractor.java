package sie.versioning;

import java.io.IOException;
import java.util.Collection;

import sie.db.entity.Change;

/**
 * Quest'interfaccia descrive il comportamente di un generico estrattore di
 * informazioni da repository di codice sorgente.
 * */
public interface VersioningExtractor {
	/**
	 * Estrae i cambiamenti da un repository di sorgenti software dato l'url.
	 * @return una collezione di cambiamenti.
	 * */
	Collection<Change> extract(String url) throws IOException;
}
