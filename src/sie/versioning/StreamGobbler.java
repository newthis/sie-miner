package sie.versioning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Questa classe ha l'utilità di analizzare gli stream di un processo in un
 * thread separato in modo da non bloccare l'esecuzione. Chiamando il metodo
 * Process.waitingFor() infatti può accadere il verificarsi di un deadlock sul
 * buffer di lettura, problema risolvibile affidandolo ad un thread separato.
 * */
public class StreamGobbler extends Thread {
	private InputStream is;
	private String diff;

	/**
	 * Instanzia un nuovo {@link StreamGobbler} con l'input stream passato.
	 * 
	 * @param is
	 *            è l'inputStream da analizzare
	 * */
	public StreamGobbler(InputStream is) {
		if (is == null)
			throw new IllegalArgumentException("InputStream must not be null");
		this.is = is;
		this.diff = "";
	}

	/**
	 * Restituisce l'output dell'inputStream memorizzato e trasformato in
	 * stringa.
	 * 
	 * @return il contenuto dell'inputStream passato nel costruttore.
	 * */
	public String getDiff() {
		return this.diff;
	}

	/**
	 * Questo metodo legge tutto il contenuto dell'inputStream memorizzato e lo
	 * scrive su una stringa.
	 * */
	public void run() {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		try {
			while ((line = br.readLine()) != null)
				diff += line + "\n";
		} catch (IOException e) {
			UnixGitExtractor.logger.severe(e.getMessage());
		}
	}
}