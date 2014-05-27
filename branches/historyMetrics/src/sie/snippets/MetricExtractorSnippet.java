package sie.snippets;

import java.util.Vector;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import sie.parser.JavaExtractor;

public class MetricExtractorSnippet extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveSite(event).getSelectionProvider().getSelection();
		
		if(selection == null)
			return null;
		
		Object firstElement = selection.getFirstElement();
		if(firstElement instanceof IJavaProject) {
			IJavaProject javaProject = (IJavaProject) firstElement;
			JavaExtractor javaExtractor = new JavaExtractor();
			
			try {
				javaExtractor.extractCode(javaProject.getElementName());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	


 }