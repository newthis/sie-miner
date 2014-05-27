package sie.metrics;

import sie.db.entity.Project;
import sie.db.entity.SourceContainer;

public class PackageMetrics {

	public static int getNumberOfRevision(Project pProject) {
		if (pProject.getChanges() != null) {
			return pProject.getChanges().size();
		} else {
			return -1;
		}
	}

	/**
	 * This method calculates the mean_NCHANGE metric. This metric represents
	 * the mean number of changes in which files of a package have been
	 * modified.
	 * 
	 * @param pSourceContainer
	 *            The package.
	 * @return The calculated metric.
	 */
	public static float getMeanNumberOfChanges(SourceContainer pSourceContainer) {
		// TODO
		return -1;
	}

	/**
	 * This method calculates the mean number of refactoring operations made on
	 * a package.
	 * 
	 * @param pSourceContainer
	 *            The package.
	 * @return The calculated metric.
	 */
	public static float getMeanNumberOfRefactoring(
			SourceContainer pSourceContainer) {
		// TODO
		return -1;
	}

	/**
	 * This method calculates the mean number of bug fixing operations made on a
	 * package.
	 * 
	 * @param pSourceContainer
	 *            The package.
	 * @return The calculated metric.
	 */
	public static float getMeanNumberOfFixing(SourceContainer pSourceContainer) {
		// TODO
		return -1;
	}

	/**
	 * This method calculates the number of authors that committed some changes
	 * in a package.
	 * 
	 * @param pSourceContainer
	 *            The package.
	 * @return The calculated metric.
	 */
	public static int getNumberOfAuthors(SourceContainer pSourceContainer) {
		// TODO
		return -1;
	}

	/**
	 * This method calculates the sum, mean and maximum of added or deleted
	 * lines in all changes made in a package.
	 * 
	 * @param pSourceContainer
	 *            The package.
	 * @return The calculated metric: an array of float of size 3 where, at
	 *         position 0 you can find the sum value; at position 1 you can find
	 *         the mean value; at position 2 you can find the maximum value.
	 */
	public static float[] getAddedOrDeletedLines(
			SourceContainer pSourceContainer) {
		// TODO
		return null;
	}

	/**
	 * This method calculates the mean dimension of modified files in a package.
	 * 
	 * @param pSourceContainer
	 *            The package.
	 * @return The calculated metric.
	 */
	public static float getChangeSetSize(SourceContainer pSourceContainer) {
		// TODO
		return -1;
	}

}
