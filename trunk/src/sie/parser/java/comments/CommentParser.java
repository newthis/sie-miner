package sie.parser.java.comments;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import sie.db.entity.CodeComment;

/**
 * Analizza sorgenti di un progetto alla ricerca di commenti
 * */
public class CommentParser {

	/**
	 * Lancia il parser per la compilation unit icu.
	 * 
	 * @param icu
	 *            è la compilation unit da cui recuperare i commenti
	 * @throws JavaModelException
	 * @throws IllegalArgumentException
	 *             se la compilation unit non è valida o null
	 * */
	public CommentParser(ICompilationUnit icu) throws JavaModelException {
		this();

		// Se la icu non rappresenta una classe definita dall'utente
		if (icu == null || !icu.exists())
			throw new IllegalArgumentException("not user defined class");

		// Lancia il parse vero e proprio
		parse(icu);
	}

	/**
	 * Costruttore vuoto, non lanca il parser, usato solo a scopi di test
	 * */
	public CommentParser() {
		classComments = new CommentsMultiMap();
		methodComments = new CommentsMultiMap();
		fieldComments = new CommentsMultiMap();
	}

	/**
	 * Lancia il parse vero e proprio
	 * */
	private void parse(ICompilationUnit cu) throws JavaModelException {

		// Recupero tutti i tipi dal file
		IType[] allTypes = cu.getAllTypes();
		ArrayList<IMethod[]> listMethod = new ArrayList<>();
		ArrayList<IField[]> listField = new ArrayList<>();

		for (IType type : allTypes) {
			listMethod.add(type.getMethods());
			listField.add(type.getFields());
		}

		extractClassComments(cu.getPath().removeFileExtension().lastSegment(),
				cu);
		extractMethodComments(listMethod);
		extractFieldComments(listField);
	}

	/**
	 * Ricerca tutti i commenti alle variabili di instanza
	 * */
	private void extractFieldComments(ArrayList<IField[]> listField)
			throws JavaModelException {
		for (IField[] fields : listField) {
			for (IField f : fields) {
				// Extract source code from field
				match(f);
				String s = "";
				if (matcher != null) {
					while (matcher.find())
						s = s + matcher.group() + "\n";
				}

				for (String s1 : s.split("\n"))
					fieldComments.put(f.getElementName(), new CodeComment(s1));
			}
		}

	}

	/**
	 * Ricerca tutti i commenti ai metodi
	 * */
	private void extractMethodComments(ArrayList<IMethod[]> listMethod)
			throws JavaModelException {

		for (IMethod[] meth : listMethod) {
			for (IMethod m : meth) {
				String s = "";
				// Extract source code from method
				match(m);
				if (matcher != null) {
					while (matcher.find())
						s = s + matcher.group() + "\n";
					// if you need this to be an int:
				}
				for (String s1 : s.split("\n"))
					methodComments.put(m.getElementName(), new CodeComment(s1));
			}
		}
	}

	/**
	 * Ricerca tutti i commenti alla classe
	 * */
	private void extractClassComments(String cName, ICompilationUnit cu)
			throws JavaModelException {
		String s = "";

		match(cu);
		if (matcher != null) {
			while (matcher.find())
				s = s + matcher.group() + "\n";
		}
		for (String s1 : s.split("\n")) {
			classComments.put(cName, new CodeComment(s1));
		}
	}

	/**
	 * Metodo che rimpiazza tutte le stringhe all'interno di un file e ne
	 * recupera tutti i commenti. La necessit� di rimpiazzare tutte stringhe
	 * proviene dai falsi positivi generati da esempio dagli url "http://"
	 * */
	private void match(ISourceReference f) throws JavaModelException {
		matcher = null;
		String source = f.getSource();
		// Rimuovo tutte le stringhe in modo da evitare falsi commenti
		// "es. in caso di url"
		source = source.replaceAll(REGEX_1, "");
		// Ricerco i commenti veri e prori
		matcher = Pattern.compile(REGEX_2).matcher(source);
	}

	public Set<CodeComment> getClassComments(String name) {
		return classComments.get(name);
	}

	public Set<CodeComment> getMethodComments(String name) {
		return methodComments.get(name);
	}

	public Set<CodeComment> getFieldComments(String name) {
		return fieldComments.get(name);
	}

	private CommentsMultiMap classComments;
	private CommentsMultiMap methodComments;
	private CommentsMultiMap fieldComments;
	private Matcher matcher;
	// Regex per individuare tutte le stringhe in un file sorgente.
	private static final String REGEX_1 = "\"(.*?)\"";
	// Regex per individuare tutti i commenti in un file sorgente
	private static final String REGEX_2 = "//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/";

}