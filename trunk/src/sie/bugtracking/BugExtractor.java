package sie.bugtracking;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import sie.db.entity.Issue;
/**
 * Quest'interfaccia descrive il comportamente di un generico estrattore di
 * informazioni da un sistema di IssueTracking.
 * @author Davide De Chiara
 * */
public interface BugExtractor {
	/**
	 * Estrae issue da un sistema di IssueTracker dato l'url e il nome progetto.
	 * @return una collezione di Issue.
	 * */
	Collection<Issue> extractBug(URL url, String pName) throws IOException;
}