package sie.db.entity;

import java.util.Set;

public class Project {
	public Project() {
	}

	public Project(String projName) {
		name = projName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Issue> getIssues() {
		return issues;
	}

	public void setIssues(Set<Issue> issues) {
		this.issues = issues;
	}

	public Set<Change> getChanges() {
		return changes;
	}

	public void setChanges(Set<Change> changes) {
		for(Change c : changes)
			c.setProject(this);
		this.changes = changes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<SourceContainer> getContainers() {
		return containers;
	}

	public void setContainers(Set<SourceContainer> containers) {
		this.containers = containers;
	}

	public String getVersioningUrl() {
		return versioningUrl;
	}

	public void setVersioningUrl(String versioningUrl) {
		this.versioningUrl = versioningUrl;
	}

	public String getBugtrackerUrl() {
		return bugtrackerUrl;
	}

	public void setBugtrackerUrl(String bugtrackerUrl) {
		this.bugtrackerUrl = bugtrackerUrl;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (bugtrackerUrl == null) {
			if (other.bugtrackerUrl != null)
				return false;
		} else if (!bugtrackerUrl.equals(other.bugtrackerUrl))
			return false;
		if (changes == null) {
			if (other.changes != null)
				return false;
		} else if (!changes.equals(other.changes))
			return false;
		if (containers == null) {
			if (other.containers != null)
				return false;
		} else if (!containers.equals(other.containers))
			return false;
		if (issues == null) {
			if (other.issues != null)
				return false;
		} else if (!issues.equals(other.issues))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (versioningUrl == null) {
			if (other.versioningUrl != null)
				return false;
		} else if (!versioningUrl.equals(other.versioningUrl))
			return false;
		return true;
	}

	private String name;
	private Set<SourceContainer> containers;
	private Set<Issue> issues;
	private Set<Change> changes;
	private String versioningUrl;
	private String bugtrackerUrl;
	private int id = -1;
}
