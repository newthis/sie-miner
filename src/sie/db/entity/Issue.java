package sie.db.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

public class Issue {
	public Issue() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getAffectedVersion() {
		return affectedVersion;
	}

	public void setAffectedVersion(String affectedVersion) {
		this.affectedVersion = affectedVersion;
	}

	public String getFixVersion() {
		return fixVersion;
	}

	public void setFixVersion(String fixVersion) {
		this.fixVersion = fixVersion;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getClosed() {
		return closed;
	}

	public void setClosed(Date closed) {
		this.closed = closed;
	}

	public Set<IssueComment> getComments() {
		return comments;
	}

	public void setComments(Set<IssueComment> comments) {
		this.comments = comments;
	}

	public void addComment(IssueComment ic) {
		comments.add(ic);
	}

	public Set<IssueAttachment> getAttach() {
		return attach;
	}

	public void setAttach(Set<IssueAttachment> attach) {
		this.attach = attach;
	}

	public void addAttach(IssueAttachment ia) {
		attach.add(ia);
	}

	@Override
	public String toString() {
		String c = "";
		if (comments != null) {
			for (IssueComment b : comments)
				c += b.toString() + ", ";
			c = c.substring(0, c.length() - 2);
		}

		String a = "";
		if (attach != null) {
			for (IssueAttachment b : attach)
				a += b.toString() + ", ";
			a = a.substring(0, a.length() - 2);
		}

		return getClass().getName() + " [type=" + type + ", priority="
				+ priority + ", status=" + status + ", resolution="
				+ resolution + ", affectedVersion=" + affectedVersion
				+ ", fixVersion=" + fixVersion + ", component=" + component
				+ ", assignee=" + assignee + ", reporter=" + reporter
				+ ", created=" + created + ", updated=" + updated + ", closed="
				+ closed + ", comments=" + c + ", attach=" + a + "] ";
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Issue other = (Issue) obj;
		if (affectedVersion == null) {
			if (other.affectedVersion != null)
				return false;
		} else if (!affectedVersion.equals(other.affectedVersion))
			return false;
		if (assignee == null) {
			if (other.assignee != null)
				return false;
		} else if (!assignee.equals(other.assignee))
			return false;
		if (attach == null) {
			if (other.attach != null)
				return false;
		} else if (!attach.equals(other.attach))
			return false;
		if (closed == null) {
			if (other.closed != null)
				return false;
		} else if (!closed.equals(other.closed))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (fixVersion == null) {
			if (other.fixVersion != null)
				return false;
		} else if (!fixVersion.equals(other.fixVersion))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (reporter == null) {
			if (other.reporter != null)
				return false;
		} else if (!reporter.equals(other.reporter))
			return false;
		if (resolution == null) {
			if (other.resolution != null)
				return false;
		} else if (!resolution.equals(other.resolution))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (updated == null) {
			if (other.updated != null)
				return false;
		} else if (!updated.equals(other.updated))
			return false;
		return true;
	}

	private Project project;
	private String type;
	private String priority;
	private String status;
	private String resolution;
	private String affectedVersion;
	private String fixVersion;
	private String component;
	private String assignee;
	private String reporter;
	private Date created;
	private Date updated;
	private Date closed;
	private Set<IssueComment> comments = new HashSet<>();
	private Set<IssueAttachment> attach = new HashSet<>();
	private int id;
}
