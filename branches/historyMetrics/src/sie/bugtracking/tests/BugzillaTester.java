package sie.bugtracking.tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import sie.bugtracking.BugExtractor;
import sie.bugtracking.HttpBugExtractorFactory;

public class BugzillaTester {

	public static void main(String[] args) {
		System.out.println("Started");
		long start = System.currentTimeMillis();
		Logger.getLogger("global").setLevel(Level.SEVERE);
		BugExtractor ext = HttpBugExtractorFactory.getDefault()
				.getBugzillaExtractor();
		try {
			ext.extractBug(new URL(
					"https://bugzilla.mozilla.org/"),
					"firefox");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		long elapsed = (end - start) / 1000 ;
		System.out.println("Elapsed: " + elapsed + "s");
	}
}
