package sie.db.entity;

import java.util.Set;

public class SType extends Import {
	public SType() {
		super();
	}

	public SType(SourceContainer ib, String clName) {
		this.name = clName;
		setBelongingPackage(ib);
	}

	public SType(String nameSType) {
		super(nameSType);
	}

	public SourceContainer getBelongingPackage() {
		return belongingPackage;
	}

	public void setBelongingPackage(SourceContainer belongingPackage) {
		this.belongingPackage = belongingPackage;
	}

	public Set<Field> getInstanceVariables() {
		return instanceVariables;
	}

	public void setInstanceVariables(Set<Field> instanceVariables) {
		this.instanceVariables = instanceVariables;
	}

	public void addInvocation(SType cb) {
		classiInvocate.add(cb);
	}

	public void addExternalInvocation(SType cb) {
		externalRefToThis.add(cb);
	}

	public Set<Method> getMethods() {
		return methods;
	}

	public void setMethods(Set<Method> methods) {
		this.methods = methods;
	}

	public int getNumLinee() {
		return numLinee;
	}

	public void setNumLinee(int numLinee) {
		this.numLinee = numLinee;
	}

	public Set<Import> getImported() {
		return imported;
	}

	public void setImported(Set<Import> imported) {
		this.imported = imported;
	}

	public Set<SType> getImplemented() {
		return implemented;
	}

	public void setImplemented(Set<SType> implemented) {
		this.implemented = implemented;
	}

	public Set<SType> getClassiInvocate() {
		return classiInvocate;
	}

	public void setClassiInvocate(Set<SType> classiInvocate) {
		this.classiInvocate = classiInvocate;
	}

	public Set<SType> getExternalRefToThis() {
		return externalRefToThis;
	}

	public void setExternalRefToThis(Set<SType> externalRefToThis) {
		this.externalRefToThis = externalRefToThis;
	}

	public Set<Method> getConstructors() {
		return constructors;
	}

	public void setConstructors(Set<Method> constructors) {
		this.constructors = constructors;
	}

	public Set<CodeComment> getComments() {
		return comments;
	}

	public void setComments(Set<CodeComment> Set) {
		this.comments = Set;
	}

	public Set<SType> getSuperclasses() {
		return superclasses;
	}

	public void setSuperclasses(Set<SType> superclasses) {
		this.superclasses = superclasses;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public boolean isInterf() {
		return interf;
	}

	public void setInterf(boolean interf) {
		this.interf = interf;
	}

	public String getHeaderFile() {
		return headerFile;
	}

	public void setHeaderFile(String headerFile) {
		this.headerFile = headerFile;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public Set<TypeMetric> getMetriche() {
		return metriche;
	}

	public void setMetriche(Set<TypeMetric> metriche) {
		this.metriche = metriche;
	}
	
	@Override
	public String toString() {
		return super.toString() + "[belongingPackage = " + belongingPackage
				+ ", isInterface= " + interf + "] ] ";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof SType))
			return false;
		SType cb = (SType) obj;
		return super.equals(obj)
				&& cb.getBelongingPackage().equals(belongingPackage);
	}

	private int id;
	private boolean interf;
	private SourceContainer belongingPackage;
	private Set<Field> instanceVariables;
	private Set<Method> methods;
	private int numLinee;
	private Set<Import> imported;
	// compatibility with multiple inheritance
	private Set<SType> superclasses;
	private Set<SType> implemented;
	private Set<SType> classiInvocate;
	private Set<SType> externalRefToThis;
	private Set<Method> constructors;
	private Set<CodeComment> comments;
	private String srcPath;
	// compatibility with c/c++
	private String headerFile;
	private String textContent;
	private Set<TypeMetric> metriche;
	
}