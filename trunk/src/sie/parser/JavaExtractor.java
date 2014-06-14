package sie.parser;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.search.IJavaSearchConstants;

import sie.db.DAOHibernate;
import sie.db.entity.SType;
import sie.db.entity.Method;
import sie.db.entity.SourceContainer;
import sie.db.entity.Project;
import sie.parser.cache.ClassCache;
import sie.parser.cache.MethodCache;
import sie.parser.cache.PackageCache;
import sie.parser.java.ClassMetricManager;
import sie.parser.java.PackageExtractor;
import sie.parser.java.helper.Env;
import sie.parser.java.helper.Finder;
import sie.parser.java.searchengine.ClassSearchRequestor;
import sie.parser.java.searchengine.MethodSearchRequestor;
import sie.parser.java.searchengine.RefSearchEngine;
import sie.parser.utils.FullyQualifiedName;
import sie.parser.utils.MethodFullyQualifiedName;

/**
 * Classe che effettua il parse di progetti java e ne estrae informazioni.
 * */
public class JavaExtractor implements CodeExtractor {

	/**
	 * Esegue il parse del progetto preso in input e ne restituisce la lista dei
	 * pacchetti trovati.
	 * 
	 * @param proj
	 *            il progetto da analizzare.
	 * */
	@Override
	public void extractCode(String projName) throws CoreException {
		DAOHibernate dao = DAOHibernate.getInstance();
		Set<SourceContainer> packages = new HashSet<>();
		Project project = new Project();
		project.setName(projName);

		Collection<IPackageFragment> c = null;
		try {
			c = getPackages(projName);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		}
		for (IPackageFragment cu : c) {
			SourceContainer pkg = PackageExtractor.parse(cu, project);
			if (pkg != null)
				packages.add(pkg);
		}

		project.setContainers(packages);
		
		Set<SType> listClasses= new HashSet<>();
		for(SourceContainer s : packages){
			listClasses.addAll(s.getClasses());
		}
		
		//calculateMetrics(listClasses);
		

		try {
			dao.beginTransaction();
			dao.getSession().save(project);
			dao.commitTransation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dao.close();

		setExternalRef(packages);
		clearCache();
		System.gc();
	}

	private void calculateMetrics(Set<SType> listClasses) {
		for(SType st: listClasses){
			ClassMetricManager.C3(st);
			ClassMetricManager.CBO(st);
			ClassMetricManager.LCOM(st);
			ClassMetricManager.LOC(st);
			ClassMetricManager.NOM(st);
			ClassMetricManager.WMC(st);
			ClassMetricManager.RFC(st);
			ClassMetricManager.WMC(st);
			ClassMetricManager.NOA(st,listClasses);
			ClassMetricManager.NOC(st, listClasses);
			ClassMetricManager.DIT(st, listClasses, 0);
			ClassMetricManager.NOO(st, listClasses);
			ClassMetricManager.SumCCBC(st, listClasses);
		}
	}

	@Override
	public Project getProjectRoot() {
		return project;
	}

	/*
	 * Ripulisce le tabelle usate come cache.
	 */
	private void clearCache() {
		MethodCache.getCache().clear();
		ClassCache.getCache().clear();
		PackageCache.getCache().clear();
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

	/* Trova tutte le relazioni tra classi e metodi trovati */
	private void setExternalRef(Set<SourceContainer> pb2)
			throws JavaModelException {

		for (SourceContainer p : pb2) {
			if (p.getClasses() != null) { // System package
				for (SType cl : p.getClasses()) {
					// Trova tutte le classi che invocano la classe in esame
					getInvokedClass(cl);
					for (Method mb : cl.getMethods()) {
						// Trova tutti i metodi che invocano il meetodo in esame
						getInvokedMethods(mb);
					}
				}
			}
		}
	}

	/**
	 * Trova tutte le classi invocate dalla classe cl.
	 * 
	 * @param cl
	 *            la classe in esame
	 * @return Set delle classi invocate
	 * */
	private void getInvokedClass(SType cl) throws JavaModelException {
		ClassSearchRequestor req = new ClassSearchRequestor(cl);
		FullyQualifiedName fqn = new FullyQualifiedName(cl);
		IJavaElement el = Finder.findCUbyName(fqn);
		// Se el e' null significa che la classe contiene errori.
		if (el == null) {
			return;
		}
		try {
			// Uso il search engine di eclipse per trovare i riferimenti
			RefSearchEngine.searchInternalRef(el, req,
					IJavaSearchConstants.CLASS);
		} catch (CoreException e) {
			// TODO gestire eccezione
			e.printStackTrace();
		}
	}

	/**
	 * Trova tutti i metodi invocati dal metodo in esame
	 * 
	 * @param mb
	 *            il metodo in esame da cui raccogliere le invocazioni
	 * @return Set di metodi usati
	 * */
	private void getInvokedMethods(Method mb) throws JavaModelException {
		MethodSearchRequestor req = new MethodSearchRequestor(mb);
		MethodFullyQualifiedName fqn = new MethodFullyQualifiedName(mb);
		MethodDeclaration md = Finder.findMIbyName(fqn);
		if (md == null) {
			return;
		}
		IMethodBinding itb = md.resolveBinding();
		if (itb == null)
			return;
		IJavaElement el = itb.getJavaElement();

		try {
			// Uso il ref search engine per trovare i metodi.
			RefSearchEngine.searchInternalRef(el, req,
					IJavaSearchConstants.METHOD);
		} catch (CoreException e) {
			// TODO Gestire eccezione
			e.printStackTrace();
			return;
		}
	}

	private Project project;
}