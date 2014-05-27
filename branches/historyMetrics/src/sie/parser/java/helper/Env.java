package sie.parser.java.helper;

import java.util.Set;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;

/**
 * Classe che conserva informazioni sull'ambiente di esecuzione dell'applicazione
 * */
public class Env {
	private Env() {
	}

	public IJavaProject getProj() {
		return proj;
	}

	public void setProj(IJavaProject proj) {
		this.proj = proj;
	}

	public static Env getInstance() {
		if (sh == null)
			sh = new Env();

		return sh;
	}
	
	public void setPackageFragments(Set<IPackageFragment> cu) {
		this.cu = cu;
	}
	
	public Set<IPackageFragment> getPackageFragments() {
		return cu;
	}

	private static Env sh = null;
	private IJavaProject proj;
	private Set<IPackageFragment> cu;
	
}
