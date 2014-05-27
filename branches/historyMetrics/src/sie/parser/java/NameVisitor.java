package sie.parser.java;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * Recupera i nomi all'interno di un nodo dell'ast
 * */
public class NameVisitor extends ASTVisitor {

	private Set<String> names;

	public NameVisitor() {
		names = new HashSet<>();
	}

	public boolean visit(SimpleName pNameNode) {
		String name = pNameNode.toString();
		if (!names.contains(name))
			names.add(name);
		return super.visit(pNameNode);
	}

	public Set<String> getNames() {
		return names;
	}
}
