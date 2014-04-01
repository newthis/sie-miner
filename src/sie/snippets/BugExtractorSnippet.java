package sie.bugtracking;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import sie.db.entity.Issue;

public class BugExtractorSnippet {

	public static void main(String args[]) {
		BugzillaHttpExtractor extractor = new BugzillaHttpExtractor();
		URL url;
		try {
			url = new URL("https://bugzilla.mozilla.org/");
			Collection<Issue> issues = extractor.extractBug(url, "firefox");
			
			for(Issue issue: issues) {
				System.out.println(issue.getAssignee());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}