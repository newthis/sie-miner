package sie.db;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;

import sie.db.entity.Project;

@SuppressWarnings({ "unchecked" })
public class ProjectManager {
	public static void saveOrUpdate(Project p) {
		DAOHibernate dao = DAOHibernate.getInstance();
		Session s = dao.getSession();
		if (p.getId() == -1) {
			Project work = getByName(p.getName(), p.getVersioningUrl());
			if (work == null)
				s.save(p);
			else
				s.update(work);
		} else {
			s.update(p);
		}
		dao.beginTransaction();
		dao.commitTransation();
		dao.close();
	}

	public static Project getByName(String name, String versioningUrl) {
		Collection<Project> projects = getQueryResults(name).list();
		for (Project p : projects)
			if (p.getVersioningUrl().equals(versioningUrl))
				return p;
		return null;
	}

	public static Project getByName(String name) {
		return (Project) getQueryResults(name).uniqueResult();
	}

	private static Query getQueryResults(String name) {
		DAOHibernate dao = DAOHibernate.getInstance();
		Session s = dao.getSession();
		Query q = s
				.createQuery("from Project as proj where proj.name = :name");
		q.setParameter("name", name);
		return q;
	}

	public static void save(Project project) {
		DAOHibernate dao = DAOHibernate.getInstance();
		Session s = dao.getSession();
		s.save(project);
		dao.beginTransaction();
		dao.commitTransation();
		dao.close();
	}
}