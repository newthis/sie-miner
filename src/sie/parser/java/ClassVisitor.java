package sie.parser.java;

import java.util.Set;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Recupera le classi dall'ast di un progetto java
 * */
public class ClassVisitor extends ASTVisitor {

	private Set<TypeDeclaration> classNodes;

	public ClassVisitor() {
		classNodes = new HashSet<>();
	}

	public boolean visit(TypeDeclaration pClassNode) {
		classNodes.add(pClassNode);
		return true;
	}

	public Set<TypeDeclaration> getClasses() {
		return classNodes;
	}
}
