package sie.bugtracking;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
/**
 * Effettua connessione tramite url e scarica il flusso di dati all'interno di un file.
 * 
 * @author Davide De Chiara
 *
 */
public class WGet {

	/**
	 * 
	 * @param url url per la connessione
	 * @param base path dove salvare il file
	 * @param name nome del file
	 * @return path del file salvato
	 * @throws IOException
	 */
	public static Path get(URL url, Path base, String name) throws IOException {
		URLConnection con = null;

		Path p = Paths.get(base + "/" + name);

		logger.info("Connection starting with host: " + url);
		con = url.openConnection();
		con.setConnectTimeout(0);
		con.connect();

		logger.info("Connection success");
		byte[] buffer = new byte[4 * 1024];
		int read;

		OutputStream os = null;
		File f = p.toFile();
		os = new FileOutputStream(f);

		InputStream in = null;
		in = con.getInputStream();

		while ((read = in.read(buffer)) > 0) {
			os.write(buffer, 0, read);
		}

		os.close();
		in.close();

		return p;
	}

	private static final Logger logger = Logger.getLogger("global");
}
