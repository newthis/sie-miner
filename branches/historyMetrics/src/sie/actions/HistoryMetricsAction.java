package sie.actions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import sie.db.entity.Change;
import sie.db.entity.Project;
import sie.db.entity.SourceContainer;
import sie.parser.java.PackageExtractor;
import sie.parser.java.PackageMetricManager;
import sie.parser.java.helper.Env;
import sie.versioning.UnixVersioningExtractorsFactory;
import sie.versioning.VersioningExtractor;
import sie.versioning.db.ChangeManager;

/**
 * This action mines data from git versioning and then calculates some history metrics
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class HistoryMetricsAction implements IWorkbenchWindowActionDelegate {
	@SuppressWarnings("unused")
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public HistoryMetricsAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		long start = System.currentTimeMillis();
		IProject eclipseProject = getProject();

		VersioningExtractor ext = UnixVersioningExtractorsFactory.getFactory()
				.getGitExtractor();
		ChangeManager changeManager = new ChangeManager();

		String projectName = this.getInput("Project name",
				"Please insert project name", "sieMiner");
		String url = this.getInput("Project git repository",
				"Please insert git repository url",
				"http://github.com/mattmezza/minerTest.git");

		try {
			Project project = new Project(projectName);
			project.setVersioningUrl(url);
			Collection<Change> changes = ext
					.extract(project.getVersioningUrl());
			Set<SourceContainer> packages = new HashSet<>();

			if (changes != null && !changes.isEmpty()) {
//				changeManager.save(changes, project);
				for (Change change : changes) {
					System.out.println(change.getCommitMsg());
				}
				Set<IPackageFragment> eclipsePackages = null;
				try {
					eclipsePackages = getPackages(eclipseProject.getName());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
					return;
				}
				for (IPackageFragment eclipsePackage : eclipsePackages) {
					SourceContainer pkg = PackageExtractor
							.parse(eclipsePackage);
					
					if (pkg != null)
						packages.add(pkg);
				}
				this.calculatePackageMetrics(packages, changes);
				System.out.println("Done");
			} else {
				System.out.println("No changes found!");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.out.println("Took: " + ((end - start) / 1000) + "s");
	}

	private void calculatePackageMetrics(Set<SourceContainer> listPackages, Collection<Change> pChanges) {
		for (SourceContainer pkg : listPackages) {
//			PackageMetricManager.MEAN_NCHANGE(pkg);
			PackageMetricManager.CHANGED_SET_SIZE(pkg, pChanges);
		}
	}

	private String getInput(String pTitle, String pMessage, String pInitialValue) {
		InputDialog inputDialog = new InputDialog(this.window.getShell(),
				pTitle, pMessage, pInitialValue, null);
		if (inputDialog.open() == IStatus.OK) {
			return inputDialog.getValue();
		} else {
			return null;
		}
	}

	/**
	 * Apre il progetto java e ne estrae la lista dei pacchetti
	 * 
	 * @param proj
	 *            il progetto da aprire
	 * @return lista di pacchetti {@link IPackageFragment}
	 * */
	private static Set<IPackageFragment> getPackages(String project)
			throws CoreException {
		IProject proj = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(project);
		if (!proj.exists()) {
			throw new IllegalArgumentException("Project " + proj.getName()
					+ " doesn't exists");
		}

		// Se il progetto non e' aperto lo apre
		if (!proj.isOpen()) {
			proj.open(new NullProgressMonitor());
		}

		// Crea la struttura del progetto in memoria
		IJavaProject javaProject = JavaCore.create(proj);

		javaProject.open(new NullProgressMonitor());
		Set<IPackageFragment> cu = new HashSet<>();
		// Raccoglie tutti i pacchetti del sistema
		for (IPackageFragment ipf : javaProject.getPackageFragments()) {
			// Filtra tutti i pacchetti che non sono stati definiti dall'utente
			if (ipf.getKind() == IPackageFragmentRoot.K_SOURCE
					&& ipf.containsJavaResources()
					&& ipf.getCorrespondingResource() != null) {
				cu.add(ipf);
			}
		}

		Env.getInstance().setPackageFragments(cu);
		Env.getInstance().setProj(javaProject);

		javaProject.close();
		return cu;
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