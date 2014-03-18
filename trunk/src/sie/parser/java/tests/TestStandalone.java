package sie.parser.java.tests;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TestStandalone {

	public static void main(String[] args) {
		String test = "package test;\n"
				+ "import org.eclipse.jdt.core.dom.AST;\n"
				+ "import org.eclipse.jdt.core.*;\n" + "import static Math;\n"
				+ "/*Class comment*/\n"
				+ "public class prova extends StoCazzo {\n"
				+ "class Inner1 {}\n" + "//variable comment\n"
				+ "private String cacca;\n" + "/** method comment*/\n"
				+ "public void test() {\n" + "//inside method comment\n"
				+ "System.out.println(\"Hello\");}\n"
				+ "private void getName() {/* boo */return name;}" + "}\n"
				+ "}\n";

		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(test.toCharArray());
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		System.out.println(cu.getProblems().length);
		cu.accept(new ASTVisitor() {

			@Override
			public boolean visit(TypeDeclaration node) {
				System.out.println(node);
				return true;
			}
			
		});
	}
}
