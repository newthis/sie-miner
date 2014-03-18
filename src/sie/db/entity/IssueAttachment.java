package sie.db.entity;

import java.util.Date;

public class IssueAttachment {
	public IssueAttachment() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getPubl() {
		return publ;
	}

	public void setPubl(Date publ) {
		this.publ = publ;
	}

	@Override
	public String toString() {
		return getClass().getName() + " [name=" + name + ", publ=" + publ
				+ "] ";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IssueAttachment other = (IssueAttachment) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (publ == null) {
			if (other.publ != null)
				return false;
		} else if (!publ.equals(other.publ))
			return false;
		return true;
	}



	private String name;
	private Date publ;
	private int id;
}