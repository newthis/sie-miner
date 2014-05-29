package sie.parser.java;

import sie.db.entity.SourceContainer;
import sie.db.entity.PackageMetric;
import sie.metrics.PackageMetrics;

public class PackageMetricManager {

	public static void MEAN_NCHANGE(SourceContainer pSourceContainer){
		double value = PackageMetrics.getMeanNumberOfChanges(pSourceContainer);
		PackageMetric metric = new PackageMetric(value, "mean_NCHANGE", pSourceContainer);
		metric.setDescription("This metric represents the mean number of changes in which files of a package have been modified.");
		metric.setName("mean_NCHANGE");
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(metric);
		dao.commitTransation();*/
	}
	
}
