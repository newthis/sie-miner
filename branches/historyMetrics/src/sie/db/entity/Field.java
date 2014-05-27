package sie.db.entity;

import java.util.HashSet;
import java.util.Set;

public class Field {

	public Field() {
	}

	public Field(String nameField) {
		name=nameField;
	}

	public Set<CodeComment> getComments() {
		return comments;
	}

	public void setComments(Set<CodeComment> comments) {
		this.comments = comments;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String pVisibility) {
		visibility = pVisibility;
	}

	public SType getOwner() {
		return owner;
	}

	public void setOwner(SType owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SType getType() {
		return type;
	}

	public void setType(SType type) {
		this.type = type;
	}

	public String getInitialization() {
		return initialization;
	}

	public void setInitialization(String initialization) {
		this.initialization = initialization;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (initialization == null) {
			if (other.initialization != null)
				return false;
		} else if (!initialization.equals(other.initialization))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (visibility == null) {
			if (other.visibility != null)
				return false;
		} else if (!visibility.equals(other.visibility))
			return false;
		return true;
	}


	private SType type;
	private String name;
	private String initialization;
	private SType owner;
	private String visibility;
	private Set<CodeComment> comments = new HashSet<>();
	private int id;
}
