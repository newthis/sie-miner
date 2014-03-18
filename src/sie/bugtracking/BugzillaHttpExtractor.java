package sie.bugtracking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sie.db.entity.Issue;
import sie.db.entity.IssueAttachment;
import sie.db.entity.IssueComment;
 /**
  * Questa classe implementa un estrattore di Issue 
  *
  * @author Davide De Chiara
  */
public class BugzillaHttpExtractor implements BugExtractor {

	/**
	 * Il metodo extractBug prende il input l'url dove è contenuto il progetto e il nome del progetto
	 * @param url url dove è contenuto il progetto
	 * @param pName Nome del progetto
	 * Il metodo init istanzia le variabile della classe, setta l'url del progetto e il nome 
	 * e crea la cartella temporaneo dove inizialmente tenere le informazioni. 
	 * Ricava il path del file tramite Wget e genera la lista degli id per ogni bug,
	 * successivamente crea gli Issue.
	 */
	@Override
	public Collection<Issue> extractBug(URL url, String pName) throws IOException {
		if(pName==null || pName.equals("") || url == null) throw new IllegalArgumentException();
		init(url, pName);
		URL query = null;
		try {
			query = new URL(base + BUGLIST + name);
		} catch (MalformedURLException e) {
			logger.severe(e.getMessage());
		}
		Path csv = null;
		csv = WGet.get(query, dir.toPath(), name);
		if (csv == null)
			throw new IOException("File csv is null");
		Set<Integer> ids = null; // lista id
		try {
			logger.info("Tryng to get id list");
			ids = getIdList(csv);
		} catch (ParseException e) {
			logger.severe(e.getMessage());
		}

		logger.info("Id list succefully created");
		for (Integer i : ids)
			try {
				logger.info("Starting to create Issue( " + i + ")");
				res.add(parse(i));
			} catch (DOMException e) {
				logger.severe(e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.severe(e.getMessage());
			} catch (SAXException e) {
				logger.severe(e.getMessage());
			} catch (ParseException e) {
				logger.severe(e.getMessage());
			}
		dir.delete();
		return res;
	}


	/**
	 * Inizializza le variabili della classe base e name con l'url del progetto e il nome del progetto
	 * @param url url del progetto
	 * @param pName nome progetto
	 */
	private void init(URL url, String pName) {
		this.base = url.toExternalForm();
		if (!base.endsWith("/"))
			base += "/";

		this.name = pName;
		df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		res = new HashSet<>(); // lista bug

		dir = new File(TMP_DIR);
		dir.mkdir();
		logger.info("tmp dir created");
	}
	
	/**
	 * Restituisce la lista di id di tutti i bug
	 * @param cvs path per arrivare al file contenente tutte le informazioni
	 * @return lista ID
	 * @throws IOException
	 * @throws ParseException
	 */
	private Set<Integer> getIdList(Path cvs) throws IOException, ParseException {
		Set<Integer> ret = new HashSet<>();

		BufferedReader r = new BufferedReader(new InputStreamReader(
				new FileInputStream(cvs.toFile())));

		boolean first_l = true;

		String line;
		while ((line = r.readLine()) != null) {
			if (!first_l) {
				int id = Integer.parseInt(line.split(",")[0].replaceAll("\"",
						""));
				if(!ret.contains(id))
					ret.add(id);
			} else {
				first_l = false;
			}
		}

		System.out.println("Numero di bugs: " + ret.size());
		r.close();
		Files.delete(cvs);
		return ret;
	}

	/**
	 * Crea un nuovo Issue, setta l'id e tramite WGet scarica il file associato all'id 
	 * dei parametri, dal file estrapoliamo tutte le informazioni per creare l'issue
	 * tramite metodi in xml.
	 * @param id id dell'issue che dobbiamo scaricare
	 * @return oggetto issue settato
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws DOMException
	 * @throws ParseException
	 */
	private Issue parse(int id) throws IOException,
			ParserConfigurationException, SAXException, DOMException,
			ParseException {

		Issue ib = new Issue();
		ib.setId(id);

		URL url = new URL(base + SHOW_BUG + id);
		Path file = WGet.get(url, dir.toPath(), "" + id);
		if (file == null)
			throw new IOException("Error while downloading bug " + id);

		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		File xml = file.toFile();
		Document doc = builder.parse(xml);
		String version = doc.getElementsByTagName("version").item(0)
				.getTextContent();
		ib.setAffectedVersion(version);

		ib.setAssignee(doc.getElementsByTagName("assigned_to").item(0)
				.getTextContent());

		Date lastChange = df.parse(doc.getElementsByTagName("delta_ts").item(0)
				.getTextContent());

		Date created = df.parse(doc.getElementsByTagName("creation_ts").item(0)
				.getTextContent());

		ib.setCreated(created);

		ib.setStatus(doc.getElementsByTagName("bug_status").item(0)
				.getTextContent());

		ib.setComponent(doc.getElementsByTagName("component").item(0)
				.getTextContent());

		if (ib.getStatus().contains("Resolved")) {
			ib.setClosed(lastChange);
			ib.setFixVersion(version);
		}

		ib.setUpdated(lastChange);

		ib.setPriority(doc.getElementsByTagName("priority").item(0)
				.getTextContent());

		ib.setReporter(doc.getElementsByTagName("reporter").item(0)
				.getTextContent());

		ib.setType("Bug"); // bugzilla support only Bug type

		ib.setResolution(doc.getElementsByTagName("resolution").item(0)
				.getTextContent());

		NodeList comments = doc.getElementsByTagName("long_desc");
		ib.setComments(getComments(comments));

		NodeList attachments = doc.getElementsByTagName("attachment");
		ib.setAttach(getAttachments(attachments));

		Files.delete(file);
		return ib;
	}
/**
 * Restituisce una lista di attachment fa una conversione da Nodelist a set
 * @param attachments lista di nodi degli attachments
 * @return lista Attachment
 * @throws DOMException
 * @throws ParseException
 */
	private Set<IssueAttachment> getAttachments(NodeList attachments)
			throws DOMException, ParseException {
		Set<IssueAttachment> ret = new HashSet<>();

		for (int i = 0; i < attachments.getLength(); i++) {
			NodeList child = attachments.item(i).getChildNodes();
			IssueAttachment ab = new IssueAttachment();

			Node name = null;
			Node data = null;
			for (int k = 0; k < child.getLength(); k++) {
				String s = child.item(k).getNodeName();
				if (s.equals("filename"))
					name = child.item(k);
				else if (s.equals("date"))
					data = child.item(k);
			}

			ab.setName(name.getTextContent());
			try {
				ab.setPubl(df.parse(data.getTextContent()));
			} catch (ParseException e) {
				logger.warning(e.getMessage() + " data: "
						+ data.getTextContent());
			}
			ret.add(ab);
		}

		return ret;
	}
/**
 * Restituisce una lista dei commenti convertendo da nodelista a Set
 * @param comments lista di nodi dei commenti
 * @return lista di set di commenti
 * @throws DOMException
 */
	private Set<IssueComment> getComments(NodeList comments)
			throws DOMException {
		Set<IssueComment> ret = new HashSet<>();
		for (int i = 0; i < comments.getLength(); i++) {
			IssueComment cb = new IssueComment();
			NodeList child = comments.item(i).getChildNodes();

			Node text = null;
			Node data = null;
			Node user = null;

			for (int k = 0; k < child.getLength(); k++) {
				String s = child.item(k).getNodeName();
				if (s.equalsIgnoreCase("who"))
					user = child.item(k);
				else if (s.equalsIgnoreCase("bug_when"))
					data = child.item(k);
				else if (s.equalsIgnoreCase("thetext"))
					text = child.item(k);
			}

			cb.setDevId(user.getTextContent());
			try {
				cb.setData(df.parse(data.getTextContent()));
			} catch (ParseException e) {
				logger.warning(e.getMessage() + " data: "
						+ data.getTextContent());
			}
			cb.setText(text.getTextContent());

			ret.add(cb);
		}
		return ret;
	}

	private String base;
	private SimpleDateFormat df;
	private String name;
	private Set<Issue> res;
	private static File dir;
	private static Logger logger = Logger.getLogger("global");
	private static final String TMP_DIR = "bugzilla_tmp";
	private static final int LIMIT = 100;
    // Query per prendere i bug cgi ( richiesta get) 
	private static final String BUGLIST = "buglist.cgi?"
			+ "query_format=specific" + "&limit=" + LIMIT
			+ "&ctype=csv&product=";

	private static final String SHOW_BUG = "show_bug.cgi?" + "ctype=xml"
			+ "&id=";
}
