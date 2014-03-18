package sie.bugtracking;

import sie.versioning.VersioningExtractorFactory;

/**
 * Questa classe implementa {@link HttpBugExtractorFactory}
 * 
 * @author Davide De Chiara
 *
 */
public class HttpBugExtractorFactory implements BugExtractorFactory {

	private HttpBugExtractorFactory() {
	}
	/**
	 * Istanzia e restituisce una factory
	 * @return
	 */
	public static HttpBugExtractorFactory getDefault() {
		if (builder == null)
			builder = new HttpBugExtractorFactory();
		return builder;
	}

	/**
	 * Restituisce un estrattore di Issue per Bugzilla.
	 * */
	@Override
	public BugExtractor getBugzillaExtractor() {
		return new BugzillaHttpExtractor();
	}

	/**
	 * Restituisce un estrattore di Issue per Jira.
	 * (Non implementato)
	 * */
	@Override
	public BugExtractor getJiraExtractor() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private static HttpBugExtractorFactory builder;
}
