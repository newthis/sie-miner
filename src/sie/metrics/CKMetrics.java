package sie.metrics;

import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sie.db.entity.Import;
import sie.db.entity.SType;
import sie.db.entity.Field;
import sie.db.entity.Method;

;

public class CKMetrics {

	public static int getLOC(SType cb) {
		return cb.getNumLinee();
	}

	public static int getWMC(SType cb) {

		int WMC = 0;

		Set<Method> methods = cb.getMethods();
		for (Method m : methods) {
			WMC += getMcCabeCycloComplexity(m);
		}

		return WMC;

	}

	public static int getDIT(SType cb, Set<SType> System, int inizialization) {

		int DIT = inizialization;

		if (cb.getSuperclasses() != null) {
			DIT++;
			for (SType cb2 : System) {
				if (cb2.getName().equals(cb.getSuperclasses())) {
					getDIT(cb2, System, DIT);
				}
			}
		} else {
			return DIT;
		}

		return DIT;
	}

	public static int getNOC(Import cb, Set<SType> System) {

		int NOC = 0;

		for (SType c : System) {
			if (c.getSuperclasses() != null
					&& c.getSuperclasses().equals(cb.getName())) {
				NOC++;
			}
		}

		return NOC;

	}

	public static int getRFC(SType cb) {

		int RFC = 0;

		Set<Method> methods = cb.getMethods();
		for (Method m : methods) {
			RFC += m.getMethodCalls().size();
		}

		return RFC;

	}

	public static int getCBO(SType cb) {

		return cb.getImported().size();

	}

	public static int getLCOM(SType cb) {

		int share = 0;
		int notShare = 0;

		Vector<Method> methods = new Vector<>(cb.getMethods());
		for (int i = 0; i < methods.size(); i++) {
			for (int j = i + 1; j < methods.size(); j++) {
				if (shareAnInstanceVariable(methods.elementAt(i),
						methods.elementAt(j))) {
					share++;
				} else {
					notShare++;
				}
			}
		}

		if (share > notShare) {
			return 0;
		} else {
			return (notShare - share);
		}

	}

	public static int getNOM(SType cb) {
		return cb.getMethods().size();
	}

	// Number of operations added by a subclass
	public static int getNOA(SType cb, Set<SType> System) {

		int NOA = 0;

		for (SType c : System) {
			if (c.getName().equals(cb.getSuperclasses())) {
				Set<Method> subClassMethods = cb.getMethods();
				Set<Method> superClassMethods = c.getMethods();
				for (Method m : subClassMethods) {
					if (!superClassMethods.contains(m)) {
						NOA++;
					}
				}
				break;
			}
		}

		return NOA;
	}

	// Number of operations overridden by a subclass
	public static int getNOO(SType cb, Set<SType> System) {

		int NOO = 0;

		if (cb.getSuperclasses() != null) {

			for (SType c : System) {
				if (c.getName().equals(cb.getSuperclasses())) {
					Set<Method> subClassMethods = cb.getMethods();
					Set<Method> superClassMethods = c.getMethods();
					for (Method m : subClassMethods) {
						if (superClassMethods.contains(m)) {
							NOO++;
						}
					}
					break;
				}
			}
		}

		return NOO;
	}

	public static int getMcCabeCycloComplexity(Method mb) {

		int mcCabe = 0;
		String code = mb.getTextContent();

		String regex = "return";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		regex = "if";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		regex = "else";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		regex = "case";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		regex = "for";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		regex = "while";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		regex = "break";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		regex = "&&";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		regex = "||";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		regex = "catch";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		regex = "throw";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(code);

		if (matcher.find()) {
			mcCabe++;
		}

		return mcCabe;
	}

	private static boolean shareAnInstanceVariable(Method m1, Method m2) {

		Set<Field> m1Variables = m1.getUsedInstanceVariables();
		Set<Field> m2Variables = m2.getUsedInstanceVariables();

		for (Field i : m1Variables) {
			if (m2Variables.contains(i)) {
				return true;
			}
		}

		return false;

	}
}