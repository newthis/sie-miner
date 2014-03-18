package sie.parser.java;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;

public class CatchedExceptionVisitor extends ASTVisitor {

	private Set<CatchClause> ret = new HashSet<>();

	@Override
	public boolean visit(CatchClause cc) {
		ret.add(cc);
		return super.visit(cc);
	}

	public Set<CatchClause> getExceptions() {
		return ret;
	}
}
