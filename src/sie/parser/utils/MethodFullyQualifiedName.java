package sie.parser.utils;

import sie.db.entity.Method;
import sie.db.entity.SType;

public class MethodFullyQualifiedName extends FullyQualifiedName {
	public MethodFullyQualifiedName(String pkg, String cl, String method,
			String[] params) {
		super(pkg, cl);
		String s = makeParams(params);
		this.method = method + s;
	}

	public MethodFullyQualifiedName(SType b, String method, String[] params) {
		super(b);
		String s = makeParams(params);
		this.method = method + s;
	}

	public MethodFullyQualifiedName(Method mb) {
		super(mb.getBelongingClass());
		this.method = mb.getName();
	}

	@Override
	public String getFQN() {
		return super.getFQN() + "." + method;
	}

	@Override
	public boolean equals(Object o) {
		if (!super.equals(o))
			return false;
		MethodFullyQualifiedName m = (MethodFullyQualifiedName) o;
		return m.getMethod().equals(getMethod());
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String toString() {
		return getFQN();
	}

	private String makeParams(String[] params) {
		if(params == null || params.length == 0)
			return "()";
		String s = "(";
		for (String n : params)
			s += n + ",";
		s = s.substring(0, s.length() - 1);
		s += ")";
		return s;
	}

	private String method;
}
