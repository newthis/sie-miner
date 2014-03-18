package sie.db.entity;

import java.util.Set;
import java.util.Date;

/**
 * Classe che rappresenta il singolo cambiamento in un commit.
 * 
 * @author Santolo Tubelli
 * */
public class Change {

	public Change() {
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getCommitMsg() {
		return commitMsg;
	}

	public void setCommitMsg(String commitMsg) {
		this.commitMsg = commitMsg;
	}

	public Set<ChangedResource> getModifiedMethods() {
		return modifiedMethods;
	}

	public void setModifiedMethods(Set<ChangedResource> modifiedFiles) {
		this.modifiedMethods = modifiedFiles;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project proj) {
		this.project = proj;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		String modified = "";
		for(ChangedResource c : modifiedMethods)
			modified += c + " ";
			
		return getClass().getName() + "[data=" + data + ", email=" + email
				+ ", devId=" + devId + ", commitMsg=" + commitMsg
				+ ", modifiedFiles=" + modified + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Change other = (Change) obj;
		if (commitMsg == null) {
			if (other.commitMsg != null)
				return false;
		} else if (!commitMsg.equals(other.commitMsg))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (devId == null) {
			if (other.devId != null)
				return false;
		} else if (!devId.equals(other.devId))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		if (id != other.id)
			return false;
		if (modifiedMethods == null) {
			if (other.modifiedMethods != null)
				return false;
		} else if (!modifiedMethods.equals(other.modifiedMethods))
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		return true;
	}

	private int id;
	private String hash;
	private Date data;
	private String email;
	private String devId;
	private String commitMsg;
	private Set<ChangedResource> modifiedMethods;
	private Project project;
}
