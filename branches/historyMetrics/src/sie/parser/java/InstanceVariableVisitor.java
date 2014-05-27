package sie.parser.java;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;

/**
 * Recupera le variabili di instanza da un nodo dell'ast
 * */
public class InstanceVariableVisitor extends ASTVisitor {
	
	private Collection<FieldDeclaration> instanceVariableNodes;
	
	public InstanceVariableVisitor() {
		instanceVariableNodes = new ArrayList<>();
	}
	
	public boolean visit(FieldDeclaration pInstanceVariableNode) {
		instanceVariableNodes.add(pInstanceVariableNode);
		return super.visit(pInstanceVariableNode);
	} 
	
	public Collection<FieldDeclaration> getFields() {
		return instanceVariableNodes;
	}
	
}
