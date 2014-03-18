package sie.parser.java.helper;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;

import sie.db.entity.SType;
import sie.db.entity.SourceContainer;
import sie.parser.cache.ClassCache;
import sie.parser.utils.FullyQualifiedName;

/**
 * Classe con metodi di servizio per convertire i binding degli oggetti java in
 * oggetti Bean
 * */
public class BindingUtils {

	/**
	 * Risolve il binding di una classe e ne restituisce il {@link SType}
	 * associato
	 * */
	public static SType castITypeBindingToClassBean(Type itb) {
		ITypeBinding b = itb.resolveBinding();

		if (b == null) {
			// Possibili problemi nei sorgenti (classe non compilata)
			// Provo a recuperarlo con una ricerca nel codice sorgente.
			try {
				return searchStatic(itb.toString());
			} catch (JavaModelException e) {
				// TODO gestire eccezione
				e.printStackTrace();
			}
		}

		return castITypeBindingToClassBean(b);
	}

	/**
	 * Tenta di risolvere il binding di una classe a partire dall'espressione in
	 * cui e' dichiarata
	 * */
	public static SType castITypeBindingToClassBean(Expression itb) {
		ITypeBinding b = itb.resolveTypeBinding();

		if (b == null) {
			// Possibili problemi nei sorgenti (classe non compilata)
			// Provo a recuperarlo con una ricerca nel codice sorgente.
			try {
				return searchStatic(itb.toString());
			} catch (JavaModelException e) {
				// TODO gestire eccezione
				e.printStackTrace();
			}
		}
		return castITypeBindingToClassBean(b);
	}

	/**
	 * Tenta di risolvere il binding di una classe a partire dalla sua
	 * dichiarazione
	 * */
	public static SType castITypeBindingToClassBean(SimpleName itb) {
		ITypeBinding b = itb.resolveTypeBinding();

		if (b == null) {
			// Possibili problemi nei sorgenti (classe non compilata)
			// Provo a recuperarlo con una ricerca nel codice sorgente.
			try {
				return searchStatic(itb.toString());
			} catch (JavaModelException e) {
				// TODO gestire eccezione
				e.printStackTrace();
			}
		}
		return castITypeBindingToClassBean(b);
	}

	/**
	 * Metodo che converte un ITypeBinding in un ClassBean
	 * */

	public static SType castITypeBindingToClassBean(ITypeBinding itb) {
		if (!itb.isFromSource()) {
			// Classe di sistema.
			SType cb = new SType();
			IPackageBinding ipb = itb.getPackage();
			SourceContainer pb = new SourceContainer();

			if (ipb == null) {
				pb.setName("java.lang");
			} else {
				pb.setName(ipb.getName());
			}

			cb.setName(itb.getName());
			cb.setBelongingPackage(pb);
			cb.setInterf(itb.isInterface());
			ClassCache.getCache().put(
					new FullyQualifiedName(cb.getBelongingPackage().getName(),
							cb.getName()), cb, true);
			return cb;
		}
		// Classe Utente.
		FullyQualifiedName fqn = null;
		if (itb.isMember()) {
			String pkg = itb.getPackage().getName();
			String name = itb.getName();
			// Se e' una classe interna recupero il nome completo.
			while (itb.isMember()) {
				itb = itb.getDeclaringClass();
				name = itb.getName() + "." + name;
			}
			fqn = new FullyQualifiedName(pkg, name);
		} else {
			String pkg = "";
			if (itb.getPackage() == null)
				pkg = "Parametrized";
			else
				pkg = itb.getPackage().getName();
			fqn = new FullyQualifiedName(pkg, itb.getName());
		}
		return Finder.findClassInCache(fqn);
	}

	/**
	 * Metodo che ricerca una classe a partire dal nome analizzando staticamente
	 * il codice sorgente.
	 * */
	private static SType searchStatic(String className)
			throws JavaModelException {
		FullyQualifiedName fqn = null;
		for (IPackageFragment pf : Env.getInstance().getPackageFragments()) {
			ICompilationUnit icu = pf.getCompilationUnit(className + ".java");
			if (icu != null)
				fqn = new FullyQualifiedName(pf.getElementName(), className);
		}

		if (fqn == null)
			return null;

		return Finder.findClassInCache(fqn);
	}
}