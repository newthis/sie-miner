package sie.versioning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sie.db.entity.Change;

/**
 * Classe astratta che implementa un generico estrattore di informazioni. Il
 * metodo extract prende in input l'url di un repository software e ne
 * restituisce i cambiamenti trovati. Nell'estendere questa classe sarà
 * necessario implementare i metodi init, execClear ed extractChanges.
 * 
 * @author Santolo Tubelli.
 * */
public abstract class UnixVersioningExtractor implements VersioningExtractor {

	/**
	 * Restituisce una collezione di cambiamenti dato l'url del repository.
	 * Questo metodo chiama inizialmente il metodo init(), che dovrà essere
	 * implementato nelle sue sottoclassi, in cui vengono eseguite tutte le
	 * operazioni preliminari all'esecuzione, tra cui la preparazione del file
	 * log.xml in cui saranno memorizzate le informazioni di alto livello di
	 * vari cambiamenti (identificativo, commenti, data ecc..). Successivamente
	 * esegue il parse del file e infine chiama il metodo extractChanges(),
	 * implementato anch'esso nelle sue sottoclassi che si occupa di estrarre i
	 * cambiamenti a grana fine dal repository. Infine chiama il metodo
	 * execClear() per ripulire eventuali file temporanei lasciati
	 * dall'esecuzione.
	 * 
	 * @param url
	 *            è l'ulr del repository
	 * 
	 * @return una collezione dei cambiamenti trovati nel repository.
	 * 
	 * @throws IOException
	 *             se l'esecuzione di extractChange() o execClear() fallisce.
	 * */
	@Override
	public Collection<Change> extract(String url) throws IOException {
		if (url == null || url.isEmpty())
			throw new IllegalArgumentException("Invalid url");
		init(url);
		try {
			parseLogXml();
			extractChanges();
		} catch (ParserConfigurationException e) {
			logger.severe(e.getMessage());
			return null;
		} catch (ParseException e) {
			logger.severe(e.getMessage());
			return null;
		} catch (SAXException e) {
			logger.severe(e.getMessage());
			return null;
		} finally {
			execClear();
		}

		return res;
	}

	/**
	 * Metodo che esegue il parse del file log.xml generato dall'esecuzione del
	 * checkout sul repository. Il file generato nel metodo init() dovrà avere
	 * il seguente formato per ogni change (entry): <root> <entry> <hash>
	 * </hash> <name> </name> <email> </email> <date> </date> <message>
	 * </message> </entry> </root>
	 * */

	private void parseLogXml() throws ParserConfigurationException,
			SAXException, IOException, ParseException {
		logger.info("Start xml parsing");
		File xml = new File(LOG_XML);

		clearFile(xml);
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.parse(xml);
		NodeList nl = doc.getElementsByTagName("entry");
		DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
		for (int i = 0; i < nl.getLength(); i++) {
			Change cb = new Change();

			NodeList nl1 = nl.item(i).getChildNodes();

			cb.setHash(nl1.item(0).getTextContent());
			cb.setDevId(nl1.item(1).getTextContent());
			cb.setEmail(nl1.item(2).getTextContent());
			String s = nl1.item(3).getTextContent();
			cb.setData(df.parse(s));
			cb.setCommitMsg(nl1.item(4).getTextContent());

			res.add(cb);
		}
		logger.info("Change creation completated, " + res.size() + " elements");
	}
	
	private static File clearFile(File xml) throws IOException {
		File tmpFile = new File("tmp.xml");
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(xml)));
		PrintWriter w = new PrintWriter(tmpFile);
		String line = null;
		String tmp = "";
		while((line = r.readLine()) != null) {
			if(line.startsWith("<entry>")) {
				tmp = line;
			} else {
				tmp += line;
				String res = clearLine(tmp);
				w.println(res);;
			}
		}
		r.close();
		w.close();
		xml.delete();
		tmpFile.renameTo(xml);
		return tmpFile;
	}

	private static String clearLine(String tmp) {
		if(tmp.contains("<root>") || tmp.contains("</root>"))
			return tmp;
		String newLine = "";
		int startMsg = tmp.lastIndexOf("<message>") + "<message>".length();
		int stopMsg = tmp.lastIndexOf("</message>");
		
		if(stopMsg < 0 || startMsg < 0)
			return null;
		
		String message = tmp.substring(startMsg, stopMsg);
		
		message = message.replaceAll("&", "and").replaceAll("<", "-").replaceAll(">", "-");
		
		newLine = tmp.substring(0, startMsg);
		newLine += message;
		newLine += tmp.substring(stopMsg);
		
		return newLine;
	}

	/**
	 * Questo metodo deve generare il file log.xml (path contenuto nella
	 * costante LOG_XML) necessario per l'estrazione dei cambiamenti nel metodo
	 * extractChanges() L'output di questo metodo deve NECESSARIAMENTE essere un
	 * file un file chiamato log.xml (contenuto nella constante LOG_XML)
	 * costruito nella seguente forma: <root> <entry> <hash> </hash> <name>
	 * </name> <email> </email> <date> </date> <message> </message> </entry>"
	 * </root>
	 * 
	 * @param url
	 *            è l'url del repository da cui scaricare il codice.
	 * @throws IOException
	 *             per problemi di rete, permessi e files
	 * */
	protected abstract void init(String url) throws IOException;

	/**
	 * Questo metodo può essere chiamato per ripulire eventuali file e tracce
	 * lasciate dall'estrattore.
	 * 
	 * @throws IOException
	 *             se la cancellazione fallisce.
	 * */

	protected abstract void execClear() throws IOException;

	/**
	 * Questo metodo è fondamentale in quanto si occupa dell'estrazione delle
	 * informazioni a grana fine dai singoli cambiamenti, come metodi o classi
	 * modificate in un commit.
	 * */
	protected abstract void extractChanges() throws IOException;

	/**
	 * Contiene la lista di tutti i cambiamenti.
	 * */
	protected ArrayList<Change> res = new ArrayList<>();

	protected static final Logger logger = Logger.getAnonymousLogger();
	protected static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss yyyy";
	protected static final String LOG_XML = "vers_tmp/log.xml";
}
