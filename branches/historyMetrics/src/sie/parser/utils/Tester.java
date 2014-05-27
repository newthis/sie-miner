package sie.parser.utils;

public class Tester {

	public static void main(String[] args) {
		String pkg = "pkg";
		String cl = "class";
		String me = "method()";
		
		MethodFullyQualifiedName n = new MethodFullyQualifiedName(pkg, cl, me, new String[]{});
		MethodFullyQualifiedName nn = new MethodFullyQualifiedName(pkg, cl, "method", null);
		System.out.println(n.equals(nn));
	}

}
