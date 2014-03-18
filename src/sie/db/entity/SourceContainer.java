package sie.db.entity;

import java.util.Set;

public class SourceContainer extends Import {

	public SourceContainer() {
	}

	public SourceContainer(String name) {
		this.name = name;
	}

	public int getNumLines() {
		return numLines;
	}

	public void setNumLines(int numLines) {
		this.numLines = numLines;
	}

	public Set<SType> getClasses() {
		return this.classes;
	}

	public void setClasses(Set<SType> pClasses) {
		this.classes = pClasses;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [name = " + name + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof SourceContainer))
			return false;
		SourceContainer pb = (SourceContainer) obj;
		return pb.getName().equals(getName());
	}

	private Project project;
	private Set<SType> classes;
	private int numLines;
}