package sie.db.entity;

public class Variable {

	public Variable() {
	}

	public Variable(String varName) {
		name = varName;
	}

	public Import getType() {
		return type;
	}

	public void setType(Import type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitialization() {
		return initialization;
	}

	public void setInitialization(String value) {
		this.initialization = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [name = " + name + ", type = "
				+ type + ", initialization = " + initialization + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	private Import type;
	private String name;
	private String initialization;
	private int id;
}
