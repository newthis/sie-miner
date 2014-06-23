package sie.db;

import java.util.Collection;
import java.util.HashSet;

import org.hibernate.Query;
import org.hibernate.Session;

import sie.db.entity.Import;
import sie.db.entity.Method;
import sie.db.entity.Project;
import sie.db.entity.SType;
import sie.db.entity.SourceContainer;

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
		
		//TODO recuperare riferimenti tra classi e metodi.
		return ret;
	}

	static public Collection<SType> getByNameInProject(String pName,
			String cName) {

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

	public static void save(Project proj) {
		DAOHibernate dao = DAOHibernate.getInstance();
		Session s = dao.getSession();

		dao.beginTransaction();
		s.save(proj);
		Query typeQuery = s
				.createSQLQuery("insert into type_invocations(invoker_class, invoked_class) values(:caller, :callee)");

		Query methodQuery = s
				.createSQLQuery("insert into method_invocations(invoker_method, invoked_method) values(:caller, :callee)");

		for (SourceContainer pack : proj.getContainers()) {
			if (pack.getClasses() != null) {
				for (SType type : pack.getClasses()) {
					if (type.getSrcPath() == null) {
						continue;
					}
					if (type.getId() < 0)
						throw new IllegalArgumentException("Class "
								+ type.getName()
								+ " must be saved into database");
					if (type.getClassiInvocate() != null)
						for (SType invoked : type.getClassiInvocate()) {
							if (invoked.getId() <= 0) {
								System.out.println(invoked + " id " + invoked.getId());
								continue;
							}
							typeQuery.setParameter("caller", type.getId());
							typeQuery.setParameter("callee", invoked.getId());

							try {
								typeQuery.executeUpdate();
							} catch (Exception e) {
								System.out.println(e.getMessage());
								System.out.println(type + " - "
										+ invoked.getSrcPath());
								System.out.println(type.getId() + " - "
										+ invoked.getId());
								System.out.println(typeQuery);
							}
						}

					if (type.getMethods() != null) {
						for (Method m : type.getMethods()) {
							if (m.getId() < 0)
								throw new IllegalArgumentException("Method "
										+ m.getName()
										+ " must be saved into database");
							if (m.getMethodCalls() != null) {
								for (Method invoked : m.getMethodCalls()) {
									if(invoked.getId() <= 0) {
										System.out.println(invoked + " id " + invoked.getId());
										continue;
									}
									methodQuery.setParameter("caller",
											m.getId());
									methodQuery.setParameter("callee",
											invoked.getId());
									try {
										methodQuery.executeUpdate();
									} catch (Exception e) {
										System.out.println(e.getMessage());
										System.out.println(m + " - "
												+ invoked);
										System.out.println(m.getId() + " - "
												+ invoked.getId());
										System.out.println(typeQuery);
									}
								}
							}
						}
					}

				}
			}
		}
		dao.commitTransation();

	}

	static public Import getById(int id) {
		return (Import) DAOHibernate.getInstance().getSession()
				.get(SType.class, new Integer(id));
	}
}