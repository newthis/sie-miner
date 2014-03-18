package sie.versioning.tests;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

import org.hibernate.Session;

import sie.db.DAOHibernate;
import sie.db.entity.Change;
import sie.db.entity.Project;
import sie.versioning.UnixVersioningExtractorsFactory;
import sie.versioning.VersioningExtractor;
import sie.versioning.db.ChangeManager;
import sie.versioning.db.JavaFIleFilter;

public class GitTester {

	public static void main(String[] args) {
		VersioningExtractor ext = UnixVersioningExtractorsFactory.getFactory()
				.getGitExtractor();
		ChangeManager m = new ChangeManager();
		/*
		 * Collection<Change> c = m.getByProjectId(2);
		 * System.out.println(c.size()); for(Change c1 : c) {
		 * System.out.println(m.getModifiedResource(c1, new JavaFIleFilter()));
		 * }
		 */
		try {

			String url = "https://github.com/BrandroidTools/OpenExplorer.git";
			Collection<Change> c = ext.extract(url);
			Project p = new Project("test");
			p.setVersioningUrl(url);
			//m.save(c, p);
			System.out.println("Done");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
