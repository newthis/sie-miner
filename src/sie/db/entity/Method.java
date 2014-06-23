package sie.db.entity;

import java.util.Set;

import sie.parser.utils.MethodFullyQualifiedName;

public class Method {
	public Method() {
	}

	public Method(SType cl, String elementName, String[] typeParam) {
		belongingClass=cl;
		name=elementName;
	}

	public Method(String methodName) {
		name=methodName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SType getBelongingClass() {
		return belongingClass;
	}

	public void setBelongingClass(SType belongingClass) {
		this.belongingClass = belongingClass;
	}

	public Set<CodeComment> getComments() {
		return comments;
	}

	public void setComments(Set<CodeComment> Set) {
		this.comments = Set;
	}

	public Set<Field> getUsedInstanceVariables() {
		return usedInstanceVariables;
	}

	public void setUsedInstanceVariables(Set<Field> pUsedInstanceVariables) {
		usedInstanceVariables = pUsedInstanceVariables;
	}

	public void addUsedInstanceVariables(Field pInstanceVariable) {
		usedInstanceVariables.add(pInstanceVariable);
	}

	public void removeUsedInstanceVariables(Field pInstanceVariable) {
		usedInstanceVariables.remove(pInstanceVariable);
	}

	public Set<Method> getMethodCalls() {
		return methodCalls;
	}

	public void setMethodCalls(Set<Method> pMethodCalls) {
		methodCalls = pMethodCalls;
	}

	public SType getReturnType() {
		return returnType;
	}

	public void setReturnType(SType returnType) {
		this.returnType = returnType;
	}

	public Set<MethodParameter> getParameters() {
		return parameters;
	}

	public void setParameters(Set<MethodParameter> parameters) {
		this.parameters = parameters;
	}

	public void addInvocation(Method mb) {
		methodCalls.add(mb);
	}

	public void addExternalInvocation(Method mb) {
		extRefToThis.add(mb);
	}

	public Set<Variable> getLocalVariables() {
		return localVariables;
	}

	public void setLocalVariables(Set<Variable> localVariables) {
		this.localVariables = localVariables;
	}

	public Set<SType> getThrowedException() {
		return throwedException;
	}

	public void setThrowedException(Set<SType> throwedException) {
		this.throwedException = throwedException;
	}

	public Set<SType> getCatchedException() {
		return catchedException;
	}

	public void setCatchedException(Set<SType> catchedException) {
		this.catchedException = catchedException;
	}

	public Set<Method> getExtRefToThis() {
		return extRefToThis;
	}

	public void setExtRefToThis(Set<Method> metodiCheInvocanoIlMetodo) {
		this.extRefToThis = metodiCheInvocanoIlMetodo;
	}

	public int getLinesCount() {
		return linesCount;
	}

	public void setLinesCount(int linesCount) {
		this.linesCount = linesCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isConstructor() {
		return constructor;
	}

	public void setConstructor(boolean isConstructor) {
		this.constructor = isConstructor;
	}
	
	public String getTextContent() {
		return TextContent;
	}

	public void setTextContent(String getTextContent) {
		this.TextContent = getTextContent;
	}
	
	public Set<MethodMetric> getMetriche() {
		return metriche;
	}

	public void setMetriche(Set<MethodMetric> metriche) {
		this.metriche = metriche;
	}
	
	@Override
	public String toString() {
		return new MethodFullyQualifiedName(this).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()
				|| !super.equals(obj))
			return false;
		Method mb = (Method) obj;
		return mb.getBelongingClass().equals(belongingClass);
	}

	private boolean constructor;
	private SType belongingClass;
	private String name;
	private SType returnType;
	private Set<MethodParameter> parameters;
	private Set<Field> usedInstanceVariables;
	private Set<Method> methodCalls;
	private Set<Variable> localVariables;
	private Set<SType> throwedException;
	private Set<SType> catchedException;
	private Set<Method> extRefToThis;
	private int linesCount;
	private Set<CodeComment> comments;
	private int id;
	private String TextContent;
	private Set<MethodMetric> metriche;	
}