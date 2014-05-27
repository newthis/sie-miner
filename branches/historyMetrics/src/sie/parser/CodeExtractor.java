package sie.parser;

import org.eclipse.core.runtime.CoreException;

import sie.db.entity.Project;

public interface CodeExtractor {
	void extractCode(String projName) throws CoreException;

	Project getProjectRoot();
}