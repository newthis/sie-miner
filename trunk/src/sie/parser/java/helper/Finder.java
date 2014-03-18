package sie.parser.java.helper;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import sie.db.entity.Import;
import sie.db.entity.SType;
import sie.db.entity.SourceContainer;
import sie.parser.java.AstParser;
import sie.parser.utils.FullyQualifiedName;
import sie.parser.utils.MethodFullyQualifiedName;
import sie.parser.utils.Name;
import sie.parser.cache.*;
import sie.parser.java.MethodVisitor;

/**
 * Classe con metodi di servizio per ricercare oggetti all'interno del progetto
 * */
public class Finder {

	/**
	 * Ricerca una {@link ICompilationUnit} a partire dal node completo della
	 * classe
	 * */
	public static ICompilationUnit findCUbyName(FullyQualifiedName fqn)
			throws JavaModelException {
		ICompilationUnit icu = null;

		for (IPackageFragment p : Env.getInstance().getPackageFragments()) {
			if (p.getElementName().equals(fqn.getPkg())) {
				// Se e' una classe interna cerca di risalire al nome del file
				// che la contiene
				String name = fqn.getType().contains("\\.") ? fqn.getType()
						+ ".java" : fqn.getType().split("\\.")[0] + ".java";
				icu = p.getCompilationUnit(name);
				if (icu.exists()) {
					return icu;
				}
			}
		}

		return null;
	}

	/**
	 * Ricerca un metodo a partire dal suo nome completo.
	 * */
	public static MethodDeclaration findMIbyName(MethodFullyQualifiedName fqn)
			throws JavaModelException {
		ICompilationUnit icu = findCUbyName(fqn);
		if (icu == null) {
			return null;
		}
		MethodVisitor mv = new MethodVisitor();
		AstParser p = new AstParser();
		CompilationUnit root = p.createParser(icu);
		root.accept(mv);
		for (MethodDeclaration md : mv.getMethods()) {
			String meth = fqn.getMethod();
			if (md.getName().toString()
					.equals(meth.substring(0, meth.lastIndexOf("("))))
				return md;
		}

		return null;
	}

	public static Import findPackageByName(String name)
			throws JavaModelException {
		for (IPackageFragment ipg : Env.getInstance().getPackageFragments())
			if (ipg.getElementName().equals(name)) {
				Name n = new Name(name);
				SourceContainer upb = PackageCache.getCache().get(n);
				if (upb == null) {
					upb = new SourceContainer();
					upb.setName(name);
					PackageCache.getCache().put(n, upb, false);
				}

				return upb;
			}
		// No packages found, it means that name is a System package.
		return new SourceContainer(name);
	}

	public static SType findClassInCache(FullyQualifiedName fqn) {
		BeanCache<SType> cache = ClassCache.getCache();
		SType cb = cache.get(fqn);
		if (cb == null) {
			cb = new SType();
			cb.setName(fqn.getType());
			cb.setBelongingPackage(new SourceContainer(fqn.getPkg()));
			cache.put(fqn, cb, false);
		}
		return cb;
	}
}
