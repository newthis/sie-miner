package sie.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;



import sie.db.entity.SType;
import sie.db.entity.Method;

public class SemanticMetrics {

	public static double getSumCCBC(SType cb, Set<SType> system) throws Exception{
		
		double sumCCBC = 0;
	
		 Vector<SType> toMeasure = getClassesFromTheSamePackage(cb, system);
		
		 if(toMeasure.size() == 0){
			 for(int i=0; i<(system.size()/10); i++){
					SType c = toMeasure.elementAt(i);
					if(!c.getName().equals(cb.getName())){
						VectorSpaceModel ir = new VectorSpaceModel();
						double tmpCCBC = 0;
						Collection<String[]> classes = new ArrayList<String[]>();

						String[] classes1=new String[2];
						classes1[0]=cb.getName();
						classes1[1]=cb.getTextContent();
						classes.add(classes1);
						
						String[] classes2=new String[2];
						classes2[0]=c.getName();
						classes2[1]=c.getTextContent();
						classes.add(classes2);
						
						
						//Call ir engine to indexing the methods
						ir.generateMatrix(classes);
						
						tmpCCBC=ir.getSimilarity(cb.getName(), c.getName());
						
						sumCCBC+=tmpCCBC;
					}
				}
			 return sumCCBC;
		 }
		 
		for(int i=0; i<toMeasure.size(); i++){
			SType c = toMeasure.elementAt(i);
			if(!c.getName().equals(cb.getName())){
				VectorSpaceModel ir = new VectorSpaceModel();
				double tmpCCBC = 0;
				Collection<String[]> classes = new ArrayList<String[]>();

				String[] classes1=new String[2];
				classes1[0]=cb.getName();
				classes1[1]=cb.getTextContent();
				classes.add(classes1);
				
				String[] classes2=new String[2];
				classes2[0]=c.getName();
				classes2[1]=c.getTextContent();
				classes.add(classes2);
				
				
				//Call ir engine to indexing the methods
				ir.generateMatrix(classes);
				
				tmpCCBC=ir.getSimilarity(cb.getName(), c.getName());
				
				sumCCBC+=tmpCCBC;
			}
		}
		
		return sumCCBC;
		
	}
	
	
	
	public static double getC3(SType cb) throws Exception{
		
		double C3 = 0;
	
		Vector<Method> methods = new Vector<>(cb.getMethods());
		
		double totalMethodsPairs = (methods.size()*(methods.size()-1))/2;
		
		for(int i=0; i<methods.size(); i++){
			for (int j=i+1; j<methods.size(); j++){
				Method m1 = methods.elementAt(i);
				Method m2 = methods.elementAt(j);
				
				VectorSpaceModel ir = new VectorSpaceModel();
				double csm = 0;
				Collection<String[]> methodsVector = new ArrayList<String[]>();

				String[] method1=new String[2];
				method1[0]=m1.getName();
				method1[1]=m1.getTextContent();
				methodsVector.add(method1);
				
				String[] method2=new String[2];
				method2[0]=m2.getName();
				method2[1]=m2.getTextContent();
				methodsVector.add(method2);
				
				
				//Call ir engine to indexing the methods
				ir.generateMatrix(methodsVector);
				
				csm=ir.getSimilarity(m1.getName(), m2.getName());
				
				C3+=csm;
			}
		}
		
		return C3/totalMethodsPairs;
		
	}
	
	
	public static Vector<SType> getClassesFromTheSamePackage(SType c, Set<SType> system){
		Vector<SType> results = new Vector<SType>();
		
		for(SType cb: system){
			if(c.getBelongingPackage().equals(cb.getBelongingPackage())){
				results.add(cb);
			}
		}
		
		return results;
	}
	
	
}
