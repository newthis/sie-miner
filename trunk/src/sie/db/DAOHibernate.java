package sie.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class DAOHibernate {

	protected DAOHibernate() {
	}

	public static DAOHibernate getInstance() {
		if (dao == null)
			dao = new DAOHibernate();
		return dao;
	}

	public Session getSession() {
		if (session == null) {
			session = sessionFactory.openSession();
		}
		return session;
	}

	public void beginTransaction() {
		getSession().beginTransaction();
	}

	public void commitTransation() {
		getSession().getTransaction().commit();
	}

	public void rollback() {
		getSession().getTransaction().rollback();
	}

	public void close() {
		getSession().close();
	}

	private static Configuration conf;
	private static ServiceRegistry serviceRegistry;
	private static SessionFactory sessionFactory;
	private static Session session;
	private static DAOHibernate dao;

	static {
		conf = new Configuration();
		conf.configure();

		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				conf.getProperties()).buildServiceRegistry();

		sessionFactory = conf.buildSessionFactory(serviceRegistry);
	}
}
