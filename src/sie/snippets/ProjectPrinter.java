package sie.snippets;

import java.util.Set;

import sie.db.ProjectManager;
import sie.db.entity.Project;
import sie.db.entity.SType;
import sie.db.entity.SourceContainer;

public class ProjectPrinter {

	public static void main(String[] args) {
		Project p = ProjectManager.getByName("minerTest", "https://github.com/mattmezza/minerTest.git");
		System.out.println(p.getName());
		Set<SourceContainer> scs = p.getContainers();
		for (SourceContainer sourceContainer : scs) {
			System.out.println(sourceContainer.getName());
			Set<SType> classes = sourceContainer.getClasses();
			for (SType sType : classes) {
				System.out.println(sType.getName()+"  --  "+sType.getSrcPath());
			}
		}
	}
	
}
