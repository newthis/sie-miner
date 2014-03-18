package sie.parser.java;

import java.util.Set;

import sie.db.DAOHibernate;
import sie.db.entity.Method;
import sie.db.entity.SType;
import sie.db.entity.TypeMetric;
import sie.metrics.CKMetrics;
import sie.metrics.SemanticMetrics;

public class ClassMetricManager {

	public ClassMetricManager(){
		
	}
	
	public static void LOC(SType cb){
		double value=CKMetrics.getLOC(cb);
		TypeMetric tm=new TypeMetric(value, "LOC", cb);
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void WMC(SType cb){
		double value=CKMetrics.getWMC(cb);
		TypeMetric tm=new TypeMetric(value, "WMC", cb);
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void DIT(SType cb, Set<SType> System, int inizialization){
		double value=CKMetrics.getDIT(cb,System,inizialization);
		TypeMetric tm=new TypeMetric(value, "DIT", cb);
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void NOC(SType cb, Set<SType> System){
		double value=CKMetrics.getNOC(cb,System);
		TypeMetric tm=new TypeMetric(value, "NOC", cb);
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void RFC(SType cb){
		double value=CKMetrics.getRFC(cb);
		TypeMetric tm=new TypeMetric(value, "RFC", cb);
	/*	DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void CBO(SType cb){
		double value=CKMetrics.getCBO(cb);
		TypeMetric tm=new TypeMetric(value, "CBO", cb);
	/*	DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void LCOM(SType cb){
		double value=CKMetrics.getLCOM(cb);
		TypeMetric tm=new TypeMetric(value, "LCOM", cb);
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void NOM(SType cb){
		double value=CKMetrics.getNOM(cb);
		TypeMetric tm=new TypeMetric(value, "NOM", cb);
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void NOA(SType cb, Set<SType> System){
		double value=CKMetrics.getNOA(cb,System);
		TypeMetric tm=new TypeMetric(value, "NOA", cb);
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void NOO(SType cb, Set<SType> System){
		double value=CKMetrics.getNOO(cb,System);
		TypeMetric tm=new TypeMetric(value, "NOO", cb);
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void SumCCBC(SType cb, Set<SType> System){
		double value = 0;
		try {
			value = SemanticMetrics.getSumCCBC(cb,System);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TypeMetric tm=new TypeMetric(value, "SumCCBC", cb);
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
	public static void C3(SType cb){
		double value = 0;
		try {
			value = SemanticMetrics.getC3(cb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TypeMetric tm=new TypeMetric(value, "C3", cb);
		/*DAOHibernate dao =DAOHibernate.getInstance();
		dao.beginTransaction();
		dao.getSession().save(tm);
		dao.commitTransation();*/
	}
	
}
