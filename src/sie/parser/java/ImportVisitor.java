package sie.parser.java;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;

/**
 * Recupera tutte le classi importate
 * */
public class ImportVisitor extends ASTVisitor {
	private Set<ImportDeclaration> dcs;
	
	public ImportVisitor() {
		this.dcs = new HashSet<>();
	}
	
	@Override
	public boolean visit(ImportDeclaration dc) {
		dcs.add(dc);
		
		return super.visit(dc);
	}
	
	public Set<ImportDeclaration> getImports() {
		return dcs;
	}
	
	
}
