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
		String s = null;
		if (res.size() == 0) {
			logger.info("No changes founded");
			return;
		}
		if (res.size() == 1) {
			logger.info("Only one change founded");
		}
		for (int i = 1; i < res.size(); i++) {
			Change cb1 = res.get(i - 1);
			Change cb2 = res.get(i);
			try {
				logger.info("For commits: " + cb1.getHash() + "(" + (i - 1)
						+ ")" + " - " + cb2.getHash() + "(" + i + ")");
				s = getDiff(cb1, cb2);
			} catch (InterruptedException e) {
				logger.severe(e.getMessage());
				e.printStackTrace();
			}
			getChangesInSnap(s, cb2);
			s = null;
		}
	}

	/**
	 * Prende in input due cambiamenti ed esegue lo script git_get_changes.sh
	 * per estrarne le modifiche a grana fine tra l'uno e l'altro.
	 * 
	 * @param cb1
	 *            è il cambiamento precedente con cui effettuare il confronto.
	 * @param cb2
	 *            è il cambiamento da confrontare con cb1 per estrarne le
	 *            modifiche.
	 * @return una stringa cotenente il diff tra cb1 e cb2.
	 * */
	private String getDiff(Change cb1, Change cb2) throws IOException,
			InterruptedException {
		logger.info("Executing script for get changes");
		ProcessBuilder pb = new ProcessBuilder(GET_CHANGES_SCRIPT,
				cb1.getHash(), cb2.getHash());
		Process pr = pb.start();
		StreamGobbler output = new StreamGobbler(pr.getInputStream());
		output.start();
		pr.waitFor();

		if (pr.exitValue() != 0) {
			throw new IOException(getErrorMessage(pr));
		}

		logger.info("script execution success");
		return output.getDiff();
	}

	/**
	 * Estrae le modifiche a grana fine per un singolo change, dato l'output
	 * dello script git_get_changes.sh.
	 * 
	 * @param r
	 *            è la stringa contenente il diff tra il change cb e il commit
	 *            precedente
	 * @param cb
	 *            è il ChangeBean in cui andare ad inserire le modifiche a grana
	 *            fine individuate.
	 * @throws IOException
	 *             se la lettura del reader r fallisce.
	 * */
	private void getChangesInSnap(String r, Change cb) throws IOException {
		logger.info("get changed files from " + cb.getHash());
		String[] lines = r.split("\n");
		Set<ChangedResource> toRet = new HashSet<>();
		String fileName = "";
		ChangedResource newRes = null;
		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].replaceAll("\\+|\\-", "").trim();
			if (lines[i].startsWith("index") && i < lines.length - 1) { // Trovato
																		// nome
																		// file
				fileName = lines[++i];
				/*
				 * Quando si cancella un file da git sembra che lo metta a
				 * /dev/null
				 */
				while (fileName.contains("/dev/null") && i < lines.length - 1)
					fileName = lines[++i];
				// Elimino dal path il branch
				int index = fileName.indexOf("/");
				if (index >= 0)
					fileName = fileName.substring(index);
				newRes = new ChangedResource();
				newRes.setFileName(fileName);
				toRet.add(newRes);
			} else { // cerco un metodo
				if (testLine(lines[i])) { // metodo trovato
					if (newRes.getModifiedMethods() == null) {
						newRes.setModifiedMethods(new HashSet<String>());
					}
					String methodName = lines[i].substring(0,
							lines[i].lastIndexOf(")") + 1);
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
	
	//TODO Al posto di questo metodo ci vorrebbe una bella regEx...
	private static boolean testLine(String s) {
		if (s.startsWith("}") || s.startsWith("{"))
			return false;
		if (s.startsWith("//") || s.startsWith("/*") || s.startsWith("*"))
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

		int open = s.indexOf("(");
		int close = s.indexOf(")");
		if (open < 0 || close < 0)
			return false;

		if (close < open)
			return false;

		s = s.substring(open, close);
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