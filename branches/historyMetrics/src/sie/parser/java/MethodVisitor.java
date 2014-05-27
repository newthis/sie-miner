package sie.parser.java;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Recupera tutti i metodi dall'ast di una ICompilationUnit
 * */
public class MethodVisitor extends ASTVisitor {
	
	private Collection<MethodDeclaration> methodNodes;
	
	public MethodVisitor() {
		methodNodes = new ArrayList<>();
	}
	
	public MethodVisitor(Collection<MethodDeclaration> md) {
		methodNodes = md;
	}
	
	public boolean visit(MethodDeclaration pMethodNode) { 
		methodNodes.add(pMethodNode);
		return super.visit(pMethodNode);
	}	
	
	public Collection<MethodDeclaration> getMethods() {
		return methodNodes;
	}
}
