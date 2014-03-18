package sie.parser.java;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import sie.db.entity.Field;
import sie.db.entity.SType;
import sie.db.entity.Method;
import sie.db.entity.Variable;
import sie.db.entity.MethodParameter;
import sie.db.entity.SourceContainer;
import sie.parser.cache.BeanCache;
import sie.parser.cache.MethodCache;
import sie.parser.java.comments.CommentParser;
import sie.parser.java.helper.BindingUtils;
import sie.parser.utils.MethodFullyQualifiedName;

/**
 * Raccoglie informazioni da un metodo in una classe di un progetto java.
 * */
public class MethodExtractor {

	/**
	 * Effettua il parse di un metodo e ne restituisce il Bean con le
	 * informazioni trovate.
	 * 
	 * @param pMethodNode
	 *            il nodo che rappresenta il metodo cercato.
	 * @param cb
	 *            la classe in cui � dichiarato il metodo.
	 * @param cp
	 *            il Parser che contiene i commenti al metodo.
	 * @return {@link SType} con le informazioni trovate.
	 * */
	public static Method parse(MethodDeclaration pMethodNode, SType cb,
			CommentParser cp) {

		String[] typeParam = getParamsSimple(pMethodNode);

		MethodFullyQualifiedName mfqn = new MethodFullyQualifiedName(cb,
				pMethodNode.getName().toString(), typeParam);

		BeanCache<Method> cache = MethodCache.getCache();
		Method mb = cache.get(mfqn);

		if (mb != null && cache.isComplete(mfqn)) {
			// Metodo in cache, restituisco
			return mb;
		} else if (mb == null) {
			mb = new Method();
			mb.setName(mfqn.getMethod());
			mb.setBelongingClass(cb);

			cache.put(mfqn, mb, false);
		}

		mb.setComments(cp.getMethodComments(pMethodNode.getName().toString()));

		mb.setLocalVariables(getLocalVariables(pMethodNode));

		// Filtra i nomi trovati alla ricerca delle variabili di intanza
		mb.setUsedInstanceVariables(getUsedInstanceVariable(
				getNames(pMethodNode), cb.getInstanceVariables()));

		mb.setParameters(getParameters(pMethodNode));

		mb.setLinesCount(getNumLinee(pMethodNode));

		mb.setThrowedException(getThrowedExceptions(pMethodNode));

		mb.setCatchedException(getCachedException(pMethodNode));

		// Salva contenuto metodo
		mb.setTextContent(pMethodNode.toString());
		
		// Salva contenuto metodo
		mb.setReturnType(getReturnType(pMethodNode));
	
		cache.put(mfqn, mb, true);

		return mb;
	}

	private static String[] getParamsSimple(MethodDeclaration pMethodNode) {
		List<?> param = pMethodNode.parameters();
		String[] typeParam = new String[param.size()];
		int i = 0;
		for (Object o : pMethodNode.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				typeParam[i] = svd.getType().toString();
				i++;
			}
		}
		return typeParam;
	}

	private static int getNumLinee(MethodDeclaration method) {
		int ret = method.toString().split("\n").length;
		if (method.getJavadoc() == null)
			return ret;
		else {
			int jdLenght = method.getJavadoc().toString().split("\n").length;
			return ret - jdLenght;
		}
	}

	private static Set<SType> getCachedException(MethodDeclaration pMethodNode) {
		Set<SType> ret = new HashSet<>();

		CatchedExceptionVisitor cev = new CatchedExceptionVisitor();
		pMethodNode.accept(cev);
		Set<CatchClause> cc = cev.getExceptions();
		for (CatchClause c : cc) {
			SType cb = BindingUtils.castITypeBindingToClassBean(c
					.getException().getType());
			if (!ret.contains(cb))
				ret.add(cb);
		}
		return ret;
	}

	/**
	 * Recupera le eccezione lanciate dalla classe
	 * */
	private static Set<SType> getThrowedExceptions(MethodDeclaration pMethodNode) {

		Set<SType> throwedException = new HashSet<>();
		// Recupera le eccezioni lanciate dichiarate nella firma del metodo
		// (clasola throws)
		List<?> exc = pMethodNode.thrownExceptions();

		for (Object sn : exc) {
			if (sn instanceof SimpleName) {
				SimpleName n = (SimpleName) sn;
				throwedException.add(BindingUtils
						.castITypeBindingToClassBean(n));
			}
		}
		// Recupero le eccezioni lanciate all'interno del metodo e le aggiungo a
		// quelle trovate precedentemente
		ThrowedExceptionVisitor dev = new ThrowedExceptionVisitor();
		pMethodNode.accept(dev);
		Set<ThrowStatement> tmp = dev.getDeclaredException();
		// TODO Testare modifica
		if (tmp != null)
			for (ThrowStatement st : tmp) {
				SType cb = BindingUtils.castITypeBindingToClassBean(st
						.getExpression());
				if (!throwedException.contains(cb))
					throwedException.add(cb);
			}
		return throwedException;
	}

	/**
	 * Recupera il tipo del parametro restituito dal metodo.
	 * 
	 * @param pMethodNode
	 *            il nodo rappresentante il metodo nell'ast
	 * @return {@link SType} rappresentante il tipo del parametro restituito
	 * */
	private static SType getReturnType(MethodDeclaration pMethodNode) {
		SType ret = new SType();
		Type t = pMethodNode.getReturnType2();
		if (t == null) {
			// se il tipo � null, il metodo non restituisce nulla
			ret.setName("Void");
			ret.setBelongingPackage(new SourceContainer("java.lang"));
		} else if (t.isPrimitiveType() || t.isSimpleType()) {
			// Tipo primitivo (int, boolean ecc) e semplice (?)
			ret.setName(t.toString());
			ret.setBelongingPackage(new SourceContainer("java.lang"));
		} else {
			ret = BindingUtils.castITypeBindingToClassBean(t);
		}
		return ret;
	}

	/**
	 * Recupera i parametri in input al metodo
	 * 
	 * @param pMethodNode
	 *            il nodo rappresentante il metodo nell'ast
	 * */
	private static HashSet<MethodParameter> getParameters(
			MethodDeclaration pMethodNode) {
		HashSet<MethodParameter> listParameters = new HashSet<>();
		for (Object o : pMethodNode.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				SType cb = BindingUtils.castITypeBindingToClassBean(svd
						.getType());
				if (cb != null)
					listParameters.add(new MethodParameter(svd.getName()
							.toString(), cb));
			}
		}
		return listParameters;
	}

	/**
	 * Recupera i nomi all'interno del metodo (nomi di variabili, metodi
	 * invocati, classi..)
	 * */
	private static Set<String> getNames(MethodDeclaration pMethodNode) {
		NameVisitor nv = new NameVisitor();
		pMethodNode.accept(nv);
		Set<String> names = nv.getNames();
		return names;
	}

	/**
	 * Recupera le variabili di instanza usate
	 * */
	// TODO TESTARE MODIFICA
	private static Set<Field> getUsedInstanceVariable(Set<String> pNames,
			Set<Field> fields) {

		// Instantiate the Set to return
		Set<Field> fieldsBean = new HashSet<>();

		// Iterate over the instance variables defined in the class
		for (Field field : fields)

			// If there is a correspondence, add to the returned Set
			if (pNames.contains(field.getName()))
				fieldsBean.add(field);

		// Return the Set
		return fieldsBean;

	}

	/**
	 * Recupera le variabili locali del metodo
	 * */
	private static Set<Variable> getLocalVariables(MethodDeclaration pMethod) {
		Set<Variable> lv = new HashSet<>();

		VariableDeclarationVisitor v = new VariableDeclarationVisitor();
		pMethod.accept(v);
		for (VariableDeclarationStatement vds : v.getFields()) {
			Variable b = new Variable();
			String app[] = null;
			if (vds.fragments().get(0).toString().contains("=")) {
				// La variabile � inizializzata nel punto in cui � dichiarata
				app = vds.fragments().get(0).toString().split("=");
				b.setInitialization(app[1]);
			} else {
				b.setInitialization("");
			}
			// Recupera il nome della variabile
			b.setName(vds.fragments().get(0).toString().split("=")[0]);

			Type itb = vds.getType();
			SType cb = BindingUtils.castITypeBindingToClassBean(itb);
			if (cb != null)
				b.setType(cb);
			lv.add(b);
		}
		return lv;
	}

}
