package sie.parser.java;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.*;

import sie.db.entity.Project;
import sie.db.entity.SType;
import sie.db.entity.SourceContainer;
import sie.parser.cache.BeanCache;
import sie.parser.cache.PackageCache;
import sie.parser.utils.Name;

public class PackageExtractor {

	public static SourceContainer parse(IPackageFragment ipf, Project project)
			throws JavaModelException {

		if (ipf.getCorrespondingResource() == null)
			throw new IllegalArgumentException(
					"The package fragment must be an user defined package "
							+ ipf.getElementName());

		String name = ipf.getElementName();

		// If no packages are used
		if (name == "")
			name = "default";
		BeanCache<SourceContainer> pc = PackageCache.getCache();

		Name n = new Name(name);
		SourceContainer pb = pc.get(n);
		if (pb != null && pc.isComplete(n))
			return pb;
		else if (pb == null) {
			pb = new SourceContainer();
			pb.setName(name);
			pc.put(n, pb, false);
		}

		int numLinee = 0;
		Set<SType> classi = new HashSet<>();

		ICompilationUnit[] icu = ipf.getCompilationUnits();

		if (icu.length == 0)
			return null;

		for (ICompilationUnit ic : icu) {
			Set<SType> coll = ClassExtractor.extractClasses(ic, pb);
			if (coll != null) {
				numLinee += getNumLinee(coll);
				classi.addAll(coll);
			}
		}

		pb.setClasses(classi);
		pb.setNumLines(numLinee);
		pb.setProject(project);

		pc.put(n, pb, true);
		return pb;
	}

	private static int getNumLinee(Set<SType> coll) {
		int num = 0;
		for(SType c : coll)
			num  += c.getNumLinee();
		return num;
	}
}