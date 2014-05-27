package sie.parser.java;

import java.util.Iterator;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;

import sie.db.entity.Field;
import sie.db.entity.SType;
import sie.parser.java.comments.CommentParser;
import sie.parser.java.helper.BindingUtils;

/**
 * Analizza tutte le variabili di instanza di una classe.
 * */
public class FieldExtractor {

	/**
	 * Effettua il parse di una {@link FieldDeclaration} per recupare tutte le
	 * informazioni da inserire in un {@link Field}.
	 * 
	 * @param pInstanceVariableNode
	 *            il nodo che rappresenta la variabile di instanza
	 * @param cp
	 *            il parser che contiene i commenti alla variabile di instanza
	 * @return il {@link Field} per rappresentare.
	 * */
	public static Field parse(FieldDeclaration pInstanceVariableNode,
			CommentParser cp, SType st) {

		if(pInstanceVariableNode == null)
			throw new IllegalArgumentException("InstanceVariableNode non puo essere null");
		if(cp == null)
			throw new IllegalArgumentException("CommentParser non puo essere null");
		if(st == null)
			throw new IllegalArgumentException("SType non puo essere null");
		
		Field fld = new Field();

		fld.setVisibility(getVisibilityModifier(pInstanceVariableNode));

		Type itb = pInstanceVariableNode.getType();
		if (itb == null) {
			throw new IllegalArgumentException(pInstanceVariableNode
					+ " non ha un tipo associato.");
		} else
			// Recupero il Bean della classe che rappresenta il tipo della
			// variabile
			fld.setType(BindingUtils.castITypeBindingToClassBean(itb));
		String[] fragments = pInstanceVariableNode.fragments().get(0)
				.toString().split("=");
		fld.setName(fragments[0]);

		// Se fragments.length == 1 la variabile non � inizializzata
		if (fragments.length == 1)
			fld.setInitialization("");
		else
			fld.setInitialization(fragments[1]);

		fld.setOwner(st);
		fld.setComments(cp.getFieldComments(fld.getName()));

		return fld;

	}

	/**
	 * Recupera il modificatore della variabile
	 * */
	private static String getVisibilityModifier(
			FieldDeclaration pInstanceVariableNode) {

		// Recupera la lista di modificatori (?) dalla variabile
		Iterator<?> it = pInstanceVariableNode.modifiers().iterator();

		// Recupera il modificatore
		while (it.hasNext()) {
			String modifier = it.next().toString();
			if (modifier.equals("private"))
				return "private";
			else if (modifier.equals("protected"))
				return "protected";
			else if (modifier.equals("public"))
				return "public";
		}

		// Nessun modificatore, la visibilita' e' package
		return "packge";

	}

}
