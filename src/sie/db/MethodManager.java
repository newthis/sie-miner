package sie.db;

import java.util.Collection;
import java.util.HashSet;

import org.hibernate.Query;

import sie.db.entity.Method;

public class MethodManager {
	static public Collection<Method> getByName(String name) {

		String hql = "from Method type " + "where type.name = :name";

		Query q = DAOHibernate.getInstance().getSession().createQuery(hql);
		q.setString("name", name);
		Collection<Method> ret = new HashSet<>();
		for (Object o : q.list()) {
			if (o instanceof Method)
				ret.add((Method) o);
		}
		return ret;
	}

	static public Collection<Method> getByNameInProject(String pName,
			String mName) {

		String hql = "from Method as met " + "where met.name = :mName AND "
				+ "met.belongingClass.belongingPackage.project.name = :pName";

		Query q = DAOHibernate.getInstance().getSession().createQuery(hql);
		q.setString("name", mName);
		q.setString("pName", pName);
		Collection<Method> ret = new HashSet<>();
		for (Object o : q.list()) {
			if (o instanceof Method)
				ret.add((Method) o);
		}
		return ret;
	}

	static public Collection<Method> getByNameInClass(String cName, String mName) {

		String hql = "from Method as met" + "where met.name = :mName AND"
				+ "met.belongingClass.name = :cName";

		Query q = DAOHibernate.getInstance().getSession().createQuery(hql);
		q.setString("mName", mName);
		q.setString("cName", cName);
		Collection<Method> ret = new HashSet<>();
		for (Object o : q.list()) {
			if (o instanceof Method)
				ret.add((Method) o);
		}
		return ret;

	}

	static public Method getById(int id) {
		return (Method) DAOHibernate.getInstance().getSession()
				.get(Method.class, new Integer(id));
	}
}