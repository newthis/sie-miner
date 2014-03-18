package sie.parser.java.searchengine;

import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

import sie.parser.java.helper.Env;

/**
 * Classe che implementa un motore di ricerca per raccogliere le relazioni tra le classi.
 * */
public class RefSearchEngine {

	/**
	 * Ricerca tutti gli oggetti (Metodi o Classi) che invocano l'oggetto target
	 * @param target l'oggetto invocato da oggetti esterni
	 * @param requestor il SearchRequestor che definisce la ricerca
	 * @param type il tipo dell'oggetto da ricercare. (Usare costanti in IJavaElement)
	 **/
	public static void searchExtRef(String target, SearchRequestor requestor,
			int type) throws CoreException {

		SearchPattern pattern = SearchPattern
				.createPattern(target, type, IJavaSearchConstants.REFERENCES,
						SearchPattern.R_CASE_SENSITIVE);

		boolean includeReferencedProjects = true;

		Collection<IPackageFragment> l = Env.getInstance()
				.getPackageFragments();

		IJavaSearchScope ijss = SearchEngine.createJavaSearchScope(
				l.toArray(new IPackageFragment[l.size()]),
				includeReferencedProjects);

		SearchEngine searchEngine = new SearchEngine();
		searchEngine.search(pattern, new SearchParticipant[] { SearchEngine
				.getDefaultSearchParticipant() }, ijss, requestor,
				new NullProgressMonitor());
	}

	public static void searchInternalRef(IJavaElement enc, SearchRequestor r,
			int type) throws JavaModelException {

		SearchEngine searchEngine = new SearchEngine();
		searchEngine.searchDeclarationsOfSentMessages(enc, r,
				new NullProgressMonitor());
	}
}