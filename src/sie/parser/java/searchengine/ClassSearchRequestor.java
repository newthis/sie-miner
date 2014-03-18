package sie.parser.java.searchengine;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;

import sie.db.entity.SType;
import sie.db.entity.SourceContainer;
import sie.parser.cache.ClassCache;
import sie.parser.utils.FullyQualifiedName;

/**
 * Classe che implementa un motore di ricerca per trovare Classi all'interno di
 * un progetto
 * */
public class ClassSearchRequestor extends SearchRequestor {
	private SType target;

	public ClassSearchRequestor(SType cb) {
		target = cb;
	}

	@Override
	public void acceptSearchMatch(SearchMatch match) throws CoreException {
		Object element = match.getElement();
		String pkg = null;
		String clName = null;
		if (element instanceof IImportDeclaration) {
			IImportDeclaration id = (IImportDeclaration) element;
			pkg = id.getAncestor(IJavaElement.PACKAGE_FRAGMENT)
					.getElementName();
			clName = id.getPath().removeFileExtension().lastSegment();
		} else if (element instanceof IJavaElement) {
			IJavaElement el = (IJavaElement) element;
			pkg = el.getAncestor(IJavaElement.PACKAGE_FRAGMENT)
					.getElementName();
			clName = el.getParent().getElementName();
		} else {
			throw new IllegalArgumentException("Element is of class "
					+ element.getClass());
		}

		FullyQualifiedName fqn = new FullyQualifiedName(pkg, clName);

		SType cb = ClassCache.getCache().get(fqn);
		if (cb == null) {
			cb = new SType(new SourceContainer(pkg), clName);
			ClassCache.getCache().put(fqn, cb, true);
		}
		if (target.getClassiInvocate() != null) {// TODO
			if (!target.equals(cb) && !target.getClassiInvocate().contains(cb)) {
				target.addInvocation(cb);
				if (cb instanceof SType) {
					cb.addExternalInvocation(target);
				}
			}
		}
	}
}