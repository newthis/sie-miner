package sie.parser.java;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 * Analizza una classe e ne registra la complessita ciclomatica.
 * */
public class CiclomaticVisitor extends ASTVisitor {
	private int cycleCompl = 0;
	
	@Override
	public boolean visit(WhileStatement st) {
		cycleCompl++;
		return true;
	}
	
	@Override
	public boolean visit(IfStatement st) {
		cycleCompl++;
		return true;
	}
	
	@Override
	public boolean visit(ForStatement st) {
		cycleCompl++;
		return true;
	}
	
	@Override
	public boolean visit(DoStatement st) {
		cycleCompl++;
		return true;
	}
	
	public int getCiclomaticComplexity() {
		return cycleCompl;
	}
}
