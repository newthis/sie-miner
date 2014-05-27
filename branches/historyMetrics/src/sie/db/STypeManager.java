package sie.db;

import java.util.Collection;
import java.util.HashSet;

import org.hibernate.Query;

import sie.db.entity.Import;
import sie.db.entity.SType;

public class STypeManager {
	static public Collection<SType> getByName(String name) {

		String hql = "from SType type " + "where type.name = :name";

		Query q = DAOHibernate.getInstance().getSession().createQuery(hql);
		q.setString("name", name);
		Collection<SType> ret = new HashSet<>();
		for (Object o : q.list()) {
			if (o instanceof SType)
				ret.add((SType) o);
		}
		return ret;
	}

	static public Collection<SType> getByNameInProject(String pName, String cName) {

		String hql = "from SType as type " + "where type.name = :cName AND "
				+ "type.belongingPackage.project.name = :pName";

		Query q = DAOHibernate.getInstance().getSession().createQuery(hql);
		q.setString("name", cName);
		q.setString("pName", pName);
		Collection<SType> ret = new HashSet<>();
		for (Object o : q.list()) {
			if (o instanceof SType)
				ret.add((SType) o);
		}
		return ret;
	}

	static public Import getById(int id) {
		return (Import) DAOHibernate.getInstance().getSession()
				.get(SType.class, new Integer(id));
	}
}