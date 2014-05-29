package sie.snippets;

import java.util.Collection;
import java.util.Set;

import sie.db.entity.Change;
import sie.db.entity.ChangedResource;
import sie.versioning.db.ChangeManager;

public class ChangesPrinter {

	public static void main(String[] args) {
		ChangeManager cm = new ChangeManager();
		Collection<Change> changes = cm.getByProjectId(1);
		for (Change change : changes) {
			System.out.println("cambiamento commit: "+change.getCommitMsg());
			Set<ChangedResource> changedResources = change.getModifiedMethods();
			for (ChangedResource changedResource : changedResources) {
				System.out.println("FILENAME: "+changedResource.getFileName());
				for (String modifiedMethod : changedResource.getModifiedMethods()) {
					System.out.println("metodo modificato: "+modifiedMethod);
				}
			}
		}
	}

}
