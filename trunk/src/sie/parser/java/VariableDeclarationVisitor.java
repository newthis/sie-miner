package sie.parser.java;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 * Classe che recupera tutte le variabili dichiarate all'interno di un nodo ast
 * */
public class VariableDeclarationVisitor extends ASTVisitor {
	private Collection<VariableDeclarationStatement> variables = new ArrayList<>();

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		variables.add(node);

		return super.visit(node);
	}

	/**
	 * This method allows to get all the FieldDeclaration for the Class on which
	 * it is;
	 * 
	 * @return a List of all FieldDeclaration;
	 */
	public Collection<VariableDeclarationStatement> getFields() {
		return variables;
	}
}
