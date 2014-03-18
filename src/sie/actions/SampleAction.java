package sie.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import sie.parser.CodeExtractor;
import sie.parser.ExtractorFactory;
import sie.parser.EclipseExtractorFactory;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class SampleAction implements IWorkbenchWindowActionDelegate {
	@SuppressWarnings("unused")
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public SampleAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		long start = System.currentTimeMillis();
		IProject project = null;
		CodeExtractor p = null;
		project = getProject();
		ExtractorFactory factory = EclipseExtractorFactory.getFactory();
		p = factory.getJavaExtractor();

		try {
			System.out.println("Start parsing per progetto "
					+ project.getName());
			p.extractCode(project.getName());
		} catch (CoreException e) {
			// TODO gestire eccezione
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.out.println("Took: " + ((end - start) / 1000) + "s");
	}

	private IProject getProject() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window != null) {
			IStructuredSelection selection = (IStructuredSelection) window
					.getSelectionService().getSelection();
			Object firstElement = selection.getFirstElement();
			if (!(firstElement instanceof IAdaptable))
				return null;

			try {
				return (IProject) ((IAdaptable) firstElement)
						.getAdapter(IProject.class);
			} catch (Exception e) {
				// TODO gestire eccezione
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}