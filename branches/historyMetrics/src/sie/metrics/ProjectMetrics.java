package sie.metrics;

import sie.db.entity.Project;

/**
 * This class gives the ability to calculate some metrics related to the
 * project.
 * 
 * @author mattmezza@gmail.com
 * 
 */
public class ProjectMetrics {

	/**
	 * This method calculates the number of revision of a project
	 * 
	 * @param pProject
	 *            The project.
	 * @return The calculated metric.
	 */
	public static int getNumberOfRevision(Project pProject) {
		if (pProject.getChanges() != null) {
			return pProject.getChanges().size();
		} else {
			return -1;
		}
	}

}
