package sie.parser.java;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sie.db.entity.Field;
import sie.db.entity.Import;
import sie.db.entity.SType;
import sie.db.entity.Method;
import sie.db.entity.SourceContainer;
import sie.parser.cache.ClassCache;
import sie.parser.java.comments.CommentParser;
import sie.parser.java.helper.BindingUtils;
import sie.parser.java.helper.Finder;
import sie.parser.utils.FullyQualifiedName;

public class ClassExtractor {
	public static Set<SType> extractClasses(ICompilationUnit icu,
			SourceContainer ib) throws JavaModelException {

		Set<SType> toRet = new HashSet<>();

		if (icu == null || !icu.exists()) {
			throw new IllegalArgumentException("not user class");
		}

		CommentParser cp = null;
		try {
			cp = new CommentParser(icu);
		} catch (IllegalArgumentException e) {
			//TODO loggare..
		}

		// Instanzia il visitor per trovare le classi dichiarate
		ClassVisitor cv = new ClassVisitor();
		getAstRoot(icu).accept(cv);

		String path = icu.getPath().toOSString()
				.substring(1);// rimuove il primo /

		// In caso di classi interne effettua il parse della classi interne
		for (TypeDeclaration td : cv.getClasses()) {
			SType ucb = parse(td, ib, cp, path);
			toRet.add(ucb);
		}

		return toRet;
	}

	private static SType parse(TypeDeclaration pClassNode, SourceContainer pb,
			CommentParser cp, String path) throws JavaModelException {

		checkArgs(pClassNode, pb, cp);

		String nodeName = getClassName(pClassNode);

		String pkgName = pb.getName();
		FullyQualifiedName fqn = new FullyQualifiedName(pkgName, nodeName);

		SType cb = Finder.findClassInCache(fqn);

		cb.setBelongingPackage(pb);

		cb.setSrcPath(path);

		cb.setComments(cp.getClassComments(cb.getName()));

		// Specifica se e' un'interfaccia o una classe
		cb.setInterf(pClassNode.isInterface());

		setInstanceVariables(pClassNode, cp, cb);

		setMethodsAndConstructors(pClassNode, cb, cp);

		// Salva le classi importate
		cb.setImported(getImported(pClassNode));

		// Salva le interfacce implementate
		cb.setImplemented(getImplemented(pClassNode));

		// Salva la superclasse
		cb.setSuperclasses(getSuperClass(pClassNode));

		// Salva contenuto classe
		cb.setTextContent(pClassNode.toString());

		// Salva il numero di righe di codice
		cb.setNumLinee(getNumLinee(pClassNode));

		// Invoco manualmente il garbage collector per ripulire la memoria
		System.gc();

		// Salvo la classe nella cache
		ClassCache.getCache().put(fqn, cb, true);
		return cb;

	}

	private static CompilationUnit getAstRoot(ICompilationUnit icu) {
		AstParser p = new AstParser();
		return p.createParser(icu);
	}

	private static String getClassName(TypeDeclaration pClassNode) {
		String nodeName;
		if (pClassNode.isMemberTypeDeclaration()) {
			// Se e' una classe interna cerco di risalire alla classe padre,
			// per recuperare il nome completo.
			CompilationUnit icu = (CompilationUnit) pClassNode.getRoot();
			nodeName = icu.getJavaElement().getPath().removeFileExtension()
					.lastSegment()
					+ "." + pClassNode.getName();
		} else {
			nodeName = pClassNode.getName().toString();
		}
		return nodeName;
	}

	private static void checkArgs(TypeDeclaration pClassNode,
			SourceContainer pb, CommentParser cp) {
		if (pb == null)
			throw new IllegalArgumentException("Package is null");
		if (pClassNode == null)
			throw new IllegalArgumentException("TypeDeclaration is null");
		if (cp == null)
			throw new IllegalArgumentException("Comment parser is null");

	}

	private static int getNumLinee(TypeDeclaration pClassNode) {
		int ret = pClassNode.toString().split("\n").length;
		if (pClassNode.getJavadoc() == null)
			return ret;
		else {
			int jdLenght = pClassNode.getJavadoc().toString().split("\n").length;
			return ret - jdLenght;
		}
	}

	private static Set<SType> getSuperClass(TypeDeclaration pClassNode)
			throws JavaModelException {
		Type t = pClassNode.getSuperclassType();
		SType supType = null;
		if (t == null) {
			// Se non ha una superclasse allora estende java.lang.Object
			supType = new SType();
			SourceContainer pb = new SourceContainer();
			pb.setName("java.lang");

			supType.setName("Object");
			supType.setBelongingPackage(pb);
		} else {
			// Se non e' Object risolvo il binding per risalire alla classe
			// originale
			supType = BindingUtils.castITypeBindingToClassBean(t);
		}
		Set<SType> superClass = new HashSet<>();
		superClass.add(supType);
		return superClass;
	}

	private static Set<SType> getImplemented(TypeDeclaration pClassNode)
			throws JavaModelException {
		// Retrieve all the implemented Interfaces
		@SuppressWarnings("unchecked")
		List<Type> clt = pClassNode.superInterfaceTypes();

		Set<SType> implemented = new HashSet<>();
		for (Type t : clt) {
			Type t1 = (Type) t;
			SType cb = BindingUtils.castITypeBindingToClassBean(t1);
			if (cb != null)
				implemented.add(cb);
		}
		return implemented;
	}

	private static Set<Import> getImported(TypeDeclaration pClassNode)
			throws JavaModelException {
		Set<Import> importD = new HashSet<>();

		ImportVisitor impv = new ImportVisitor();
		pClassNode.getParent().accept(impv);
		Set<ImportDeclaration> importDec = impv.getImports();

		Import ib = null;
		for (ImportDeclaration d : importDec) {
			// Se il pacchetto e' nella forma import java.utils.*
			if (d.isOnDemand()) {
				ib = Finder.findPackageByName(d.getName()
						.getFullyQualifiedName());
				// Se l'import e' del tipo import static java.lang.Math.*;
			} else {
				// Import classico
				FullyQualifiedName fqn = new FullyQualifiedName(d.getName()
						.getFullyQualifiedName());
				// Recupero la CompilationUnit
				ICompilationUnit icu = Finder.findCUbyName(fqn);
				if (icu != null) {
					ib = Finder.findClassInCache(fqn);
				} else {
					// Se la classe non e' definita dall'utente, restituisco un
					// ClassBean generico
					ib = new SType(new SourceContainer(fqn.getPkg()),
							fqn.getType());
				}
			}
			// Eventuali problemi nella classe analizzata, probabile che
			// contenga errori di compilazione
			if (ib == null) {
				ib = new SourceContainer(d.getName().getFullyQualifiedName());
			}
			importD.add(ib);
		}
		return importD;
	}

	private static void setMethodsAndConstructors(TypeDeclaration pClassNode,
			SType cb, CommentParser cp) {

		Set<Method> methodBeans = new HashSet<Method>();
		Set<Method> constructorBeans = new HashSet<Method>();

		// Recupera tutti i metodi della classe
		MethodVisitor mv = new MethodVisitor();
		pClassNode.accept(mv);

		for (MethodDeclaration methodNode : mv.getMethods()) {
			// Filtra solo i metodi interni alla classe
			if (methodNode.getParent().equals(pClassNode)) {
				Method method = MethodExtractor.parse(methodNode, cb, cp);

				if (methodNode.isConstructor()) {
					constructorBeans.add(method);
					method.setConstructor(true);
				} else
					methodBeans.add(method);
			}
		}

		cb.setConstructors(constructorBeans);
		cb.setMethods(methodBeans);
	}

	private static void setInstanceVariables(TypeDeclaration pClassNode,
			CommentParser cp, SType cb) {

		Set<Field> ivb = new HashSet<>();

		for (FieldDeclaration ivn : pClassNode.getFields()) {
			Field f = FieldExtractor.parse(ivn, cp, cb);
			ivb.add(f);
		}
		cb.setInstanceVariables(ivb);
	}
}
