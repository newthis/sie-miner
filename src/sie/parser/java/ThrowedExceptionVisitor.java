package sie.parser.java;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ThrowStatement;

/**
 * Classe che recupera tutte le eccezioni lanciate all'interno di un metodo
 * */
public class ThrowedExceptionVisitor extends ASTVisitor {
	private Set<ThrowStatement> ret = new HashSet<>();
	
	@Override
	public boolean visit(ThrowStatement st) {
		ret.add(st);
		return super.visit(st);
	}
	
	public Set<ThrowStatement> getDeclaredException() {
		return ret;
	}
}
