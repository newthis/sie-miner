package sie.parser.java.searchengine;

import java.util.HashSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;

import sie.db.entity.Method;
import sie.db.entity.SType;
import sie.db.entity.SourceContainer;
import sie.parser.cache.MethodCache;
import sie.parser.utils.MethodFullyQualifiedName;

/**
 * Classe che implementa un motore di ricerca per metodi
 * */
public class MethodSearchRequestor extends SearchRequestor {
	private Method target;

	public MethodSearchRequestor(Method target) {
		this.target = target;
	}

	@Override
	public void acceptSearchMatch(SearchMatch match) throws CoreException {
		Object el = match.getElement();
		Method mb = null;

		if (!(el instanceof IMethod)) {
			return;
		}
		IMethod im = (IMethod) el;
		ICompilationUnit icu = im.getCompilationUnit();
		String[] typeParam = im.getParameterTypes();
		if (icu == null || icu.getCorrespondingResource() == null) {
			String clName = im.getDeclaringType().getElementName();
			String pkg = im.getAncestor(IJavaElement.PACKAGE_FRAGMENT)
					.getElementName();
			SType cl = new SType(new SourceContainer(pkg), clName);

			mb = new Method(cl, im.getElementName(), typeParam);

		} else {
			String clName = icu.getPath().removeFileExtension().lastSegment();
			String pkgName = icu.getAncestor(IJavaElement.PACKAGE_FRAGMENT)
					.getElementName();

			MethodFullyQualifiedName mfqn = new MethodFullyQualifiedName(
					pkgName, clName, im.getElementName(), typeParam);

			mb = MethodCache.getCache().get(mfqn);
			if (mb == null) { // CU with error
				mb = new Method();
				mb.setName(mfqn.getMethod());
				mb.setBelongingClass(new SType(new SourceContainer(mfqn
						.getPkg()), mfqn.getType()));
			}
		}

		if (target.getMethodCalls() == null) {
			target.setMethodCalls(new HashSet<Method>());
		}
		if (!target.equals(mb) && !target.getMethodCalls().contains(mb)) {
			target.addInvocation(mb);
			if (mb instanceof Method) {
				if (mb.getExtRefToThis() == null) {
					mb.setExtRefToThis(new HashSet<Method>());
				}
				mb.addExternalInvocation(target);
			}
		}
	}
}
