package sie.versioning.db;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import sie.db.DAOHibernate;
import sie.db.ProjectManager;
import sie.db.entity.Change;
import sie.db.entity.ChangedResource;
import sie.db.entity.Project;

/**
 * Classe che consente di gestire il salvataggio e il recupero degli oggetti
 * change dal database.
 * 
 * @author Santolo Tubellli
 * */
public class ChangeManager {

	/**
	 * Salva la collezione di changes trovati nel database. In caso di errore il
	 * db viene riportato allo stato precedente, quindi il salvataggio non
	 * avviene e si dovrà ripetere l'operazione.
	 * 
	 * @param changes
	 *            è la collezione di cambiamenti da salvare.
	 * @param proj
	 *            è il progetto a cui appartiene la collezione di cambiamenti.
	 * */
	public void save(Collection<Change> changes, Project proj) {
		if (proj == null || proj.getName() == null)
			throw new IllegalArgumentException(
					"Il progetto non può essere null");

		Project work = null;

		if (proj.getId() == -1) {
			work = ProjectManager.getByName(proj.getName(),
					proj.getVersioningUrl());
		}

		if (work == null) {
			work = proj;
		}

		work.setChanges(new HashSet<>(changes));
		ProjectManager.saveOrUpdate(work);
	}

	/**
	 * Restituisce tutti i cambiamenti di un determinato progetto.
	 * 
	 * @param projectId
	 *            è l'id del progetto.
	 * */
	public Collection<Change> getByProjectId(int projectId) {
		Set<Change> ret = new HashSet<>();
		Session s = DAOHibernate.getInstance().getSession();

		Criteria c = s.createCriteria(Change.class);
		c.add(Restrictions.eq("project.id", new Integer(projectId)));
		List<?> res = c.list();
		for (Object o : res)
			ret.add((Change) o);
		return ret;
	}

	/**
	 * Restituisce tutte le risorse modificate all'interno di un commit, che
	 * corrispondano allo specifico filtro {@link ResourceFilter}
	 * 
	 * @param c
	 *            il change da cui filtrare le modifiche.
	 * @param filter
	 *            il filtro da applicare ai risultati trovati.
	 * 
	 * @return i risultati filtrati.
	 * */
	public Set<ChangedResource> getModifiedResource(Change c,
			ResourceFilter filter) {
		return filter.filter(c.getModifiedMethods());
	}
}