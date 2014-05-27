package sie.parser.utils;

public class Name {
	private String name;
	
	public Name(String name) {
		this.name = name;
	}
	
	public String getFQN() {
		return name;
	}
	
	public String getPkg() {
		return name;
	}
	
	public void setPkg(String n) {
		this.name = n;
	}
	
	@Override
	public String toString() {
		return getFQN();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Name))
			return false;
		Name n = (Name) o;
		return n.getPkg().equals(name);
	}

	@Override
	public int hashCode() {
		return getFQN().hashCode();
	}
	
	
}
