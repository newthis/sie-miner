package sie.parser.utils;

import sie.db.entity.SType;

public class FullyQualifiedName extends Name{
	public FullyQualifiedName(String pkg, String className) {
		super(pkg);
		name = className;
	}

	public FullyQualifiedName(SType cl) {
		super(cl.getBelongingPackage().getName());
		name = cl.getName();
	}

	public FullyQualifiedName(String s) {
		super(s.substring(0, s.lastIndexOf(".")));
		if (!s.contains("."))
			throw new IllegalArgumentException(
					"The string must be in 'package.class' form");
		name = s.substring(s.lastIndexOf(".") + 1, s.length());
	}
	
	public String getType() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getFQN() {
		return super.getFQN() + "." + name;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!super.equals(o))
			return false;
		FullyQualifiedName fqn = (FullyQualifiedName) o;
		return fqn.getType().equals(name);
	}

	public String toString() {
		return getFQN();
	}

	private String name;
}
