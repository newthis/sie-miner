package sie.versioning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import sie.db.entity.Change;
import sie.db.entity.ChangedResource;

public class UnixGitExtractor extends UnixVersioningExtractor {

	/**
	 * Usa uno script bash (git_checkout.sh) per scaricare i commit dal
	 * repository git e serializzarli nel file log.xml.
	 * 
	 * @param url
	 *            l'url del repository.
	 * @throws IOException 
	 * */
	@Override
	protected void init(String url) throws IOException {
		try {
			setScriptPermissions();
		} catch (IOException e1) {
			logger.severe(e1.getMessage());
		} catch (InterruptedException e1) {
			logger.severe(e1.getMessage());
		}

		ProcessBuilder pb = new ProcessBuilder(START_SCRIPT, url);
		try {
			logger.info("Executing start script");
			Process p = pb.start();
			p.waitFor();
			if (p.exitValue() != 0)
				throw new IOException(getErrorMessage(p));
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
		} 
	}

	/**
	 * Esegue la pulizia dei file generati durante l'esecuzione. Più
	 * precisamente cancella il file log.xml e la cartella vers_tmp/ che
	 * contiene il repository scaricato.
	 * */
	@Override
	protected void execClear() throws IOException {
		ProcessBuilder pb;
		pb = new ProcessBuilder(CLEAR_SCRIPT);
		Process pr = pb.start();
		try {
			pr.waitFor();
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
		}
		if (pr.exitValue() != 0) {
			throw new IOException(getErrorMessage(pr));
		}
	}

	/**
	 * Estrae i cambiamenti a grana fine da ogni singolo commit. Più
	 * precisamente file e metodi modificati.
	 * 
	 * @throws IOException
	 *             se lo script git_get_changes.sh fallisce.
	 * */
	@Override
	protected void extractChanges() throws IOException {
		BufferedReader reader = null;

		if (res.size() == 0) {
			logger.info("no changes founded");
			return;
		}  //Commentata perchè il primo commit in alcuni casi è molto grande 
			//rallenta troppo.
		/*else if (res.size() == 1) {
			logger.info("only one change found");
			Change cb = res.get(0);
			try {
				reader = execChangesScript(cb);
			} catch (InterruptedException e) {
				logger.severe(e.getMessage());
			}
			getChangesInSnap(reader, cb);
			reader.close();
			

		} else {
			// Get first snap

			Change cb = res.get(0);

			/*try {
				reader = execChangesScript(cb);
			} catch (InterruptedException e) {
				logger.severe(e.getMessage());
			}
			getChangesInSnap(reader, cb);
			reader.close();
			 */
			for (int i = 1; i < res.size(); i++) {
				Change cb1 = res.get(i - 1);
				Change cb2 = res.get(i);
				try {
					logger.info("For commits: " + (i - 1) + " - " + i);
					reader = execChangesScript(cb1, cb2);
				} catch (InterruptedException e) {
					logger.severe(e.getMessage());
					e.printStackTrace();
				}
				getChangesInSnap(reader, cb2);
				reader.close();
			}
		}
	//}

	/**
	 * Prende in input un singolo cambiamento ed esegue lo script
	 * git_get_changes.sh per estrarne le modifiche a grana fine.
	 * 
	 * @param cb
	 *            è il cambiamento di cui andare a calcolare le modifiche a
	 *            grana fine.
	 * @return il reader restituito dal processo in cui è stato eseguito lo
	 *         script. Contiene il risultato dell'esecuzione.
	 * */
/*	private BufferedReader execChangesScript(Change cb) throws IOException,
			InterruptedException {
		logger.info("Executing script for get changes");

		ProcessBuilder pb = new ProcessBuilder(GET_CHANGES_SCRIPT, cb.getHash());
		Process pr = pb.start();
		pr.waitFor();

		if (pr.exitValue() != 0) {
			throw new IOException(getErrorMessage(pr));
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				pr.getInputStream()));
		logger.info("script execution success");

		return reader;
	}
*/
	/**
	 * Prende in input due cambiamenti ed esegue lo script git_get_changes.sh
	 * per estrarne le modifiche a grana fine tra l'uno e l'altro.
	 * 
	 * @param cb1
	 *            è il cambiamento precedente con cui effettuare il confronto.
	 * @param cb2
	 *            è il cambiamento da confrontare con cb1 per estrarne le
	 *            modifiche.
	 * @return il reader restituito dal processo in cui è stato eseguito lo
	 *         script. Contiene il risultato dell'esecuzione.
	 * */
	private BufferedReader execChangesScript(Change cb1, Change cb2)
			throws IOException, InterruptedException {
		logger.info("Executing script for get changes");
		ProcessBuilder pb = new ProcessBuilder(GET_CHANGES_SCRIPT,
				cb1.getHash(), cb2.getHash());
		Process pr = pb.start();
		pr.waitFor();
		if (pr.exitValue() != 0) {
			throw new IOException(getErrorMessage(pr));
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				pr.getInputStream()));
		logger.info("script execution success");
		return reader;
	}

	/**
	 * Calcola le modifiche a grana fine per un singolo change, dato l'output
	 * dello script git_get_changes.sh.
	 * 
	 * @param r
	 *            è il reader contenente l'output dello script
	 *            git_get_canges.sh.
	 * @param cb
	 *            è il ChangeBean in cui andare ad inserire le modifiche a grana
	 *            fine individuate.
	 * @throws IOException
	 *             se la lettura del reader r fallisce.
	 * */
	private void getChangesInSnap(BufferedReader r, Change cb)
			throws IOException {
		logger.info("get changed files from " + cb.getId());
		String line;
		Set<ChangedResource> toRet = new HashSet<>();
		String fileName = "";
		ChangedResource newRes = null;
		while ((line = r.readLine()) != null) {
			line = line.replaceAll("\\+|\\-", "").trim();
			if (line.startsWith("index")) { // Trovato nome file
				fileName = r.readLine();
				
				/*
				 * Quando si cancella un file da git sembra che lo metta a
				 * /dev/null
				 */
				while (fileName.contains("/dev/null"))
					fileName = r.readLine();
				// Elimino dal path il branch
				fileName = fileName.substring(fileName.indexOf("/"));
				newRes = new ChangedResource();
				newRes.setFileName(fileName);
				toRet.add(newRes);
			} else { // cerco un metodo
				if (testLine(line)) {	//metodo trovato
					if(newRes.getModifiedMethods() == null) {
						newRes.setModifiedMethods(new HashSet<String>());
					}
					String methodName = line.substring(0,
							line.lastIndexOf(")") + 1);
					newRes.getModifiedMethods().add(methodName);
				}
			}
		}
		cb.setModifiedMethods(toRet);
		logger.info("get changed files success");
	}

	/**
	 * Verifica se la riga analizzata è la dichiarazione di un metodo (al
	 * momento funziona solo con i linguaggi c-like)
	 * */
	private static boolean testLine(String s) {
		if (s.startsWith("}"))
			return false;
		if (!s.contains("(") || !s.contains("{"))
			return false;
		if (s.contains(" class "))
			return false;
		if (s.contains("=") || s.contains("==") || s.contains("<")
				|| s.contains(">"))
			return false;
		if (s.contains("."))
			return false;

		s = s.substring(s.indexOf("("), s.indexOf(")"));
		if (s.length() > 1 && !s.contains(" "))
			return false;
		return true;
	}

	/**
	 * Imposta i permessi di esecuzione agli script clear.sh git_checkout.sh e
	 * git_get_changes.sh
	 * 
	 * @throws IOException
	 *             se l'esecuzione del comando chmod fallisce.
	 * */
	private void setScriptPermissions() throws IOException,
			InterruptedException {
		logger.info("set permission to scripts");
		Process pr = null;
		Runtime ra = Runtime.getRuntime();

		pr = ra.exec("chmod +x " + GET_CHANGES_SCRIPT);
		pr.waitFor();
		if (pr.exitValue() != 0) {
			throw new IOException(getErrorMessage(pr));
		}

		pr = ra.exec("chmod +x " + CLEAR_SCRIPT);
		pr.waitFor();
		if (pr.exitValue() != 0) {
			throw new IOException(getErrorMessage(pr));
		}

		pr = ra.exec("chmod +x " + START_SCRIPT);
		pr.waitFor();
		if (pr.exitValue() != 0) {
			throw new IOException(getErrorMessage(pr));
		}
	}

	/**
	 * Restituisce il messaggio di errore lasciato dal processo prima di
	 * terminare l'esecuzione.
	 * 
	 * @param p
	 *            il processo la cui esecuzione ha incontrato errori.
	 * @return il messaggio di errore del processo
	 * */
	private String getErrorMessage(Process p) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));

		String errorMsg = "";
		String tmp;
		while ((tmp = r.readLine()) != null)
			errorMsg += tmp + "\n";

		r.close();
		return errorMsg;
	}

	private static final String SCRIPT_DIR = "script/";
	private static final String GET_CHANGES_SCRIPT = SCRIPT_DIR
			+ "/git_get_changes.sh";
	private static final String CLEAR_SCRIPT = SCRIPT_DIR + "/clear.sh";
	private static final String START_SCRIPT = SCRIPT_DIR + "./git_checkout.sh";
}