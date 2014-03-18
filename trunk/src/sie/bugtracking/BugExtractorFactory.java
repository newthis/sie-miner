package sie.bugtracking;


/**
 * Quest'interfaccia descrive una Factory generica per instanziare estrattori di
 * Issue per Bugzilla e Jira.
 * @author Davide De Chiara
 * */

public interface BugExtractorFactory {
	public BugExtractor getBugzillaExtractor();
	public BugExtractor getJiraExtractor();
}
