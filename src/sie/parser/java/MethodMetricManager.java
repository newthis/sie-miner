package sie.parser.java;

import sie.db.DAOHibernate;
import sie.db.entity.Method;
import sie.db.entity.MethodMetric;
import sie.metrics.CKMetrics;

public class MethodMetricManager {

	public MethodMetricManager(){
		
	}
	
	public static void McCabeCycloComplexity(Method mb){
		double value=CKMetrics.getMcCabeCycloComplexity(mb);
		MethodMetric tm=new MethodMetric(value, "McCabeCycloComplexity", mb);
		DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();
	}
	
}
