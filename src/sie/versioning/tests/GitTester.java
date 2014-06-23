package sie.versioning.tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import sie.db.entity.Change;
import sie.db.entity.Project;
import sie.versioning.UnixVersioningExtractorsFactory;
import sie.versioning.VersioningExtractor;
import sie.versioning.db.ChangeManager;

public class GitTester {

	public static void main(String[] args) {
		VersioningExtractor ext = UnixVersioningExtractorsFactory.getFactory()
				.getGitExtractor();
		ChangeManager m = new ChangeManager();

		try {

			// String url =
			// "https://github.com/BrandroidTools/OpenExplorer.git";
			String url = "https://github.com/apache/subversion.git/";
			Collection<Change> c = ext.extract(url);
			Project p = new Project("test");
			p.setVersioningUrl(url);
			m.save(c, p);
			System.out.println("Done");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
