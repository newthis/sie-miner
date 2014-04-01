package sie.parser;

import org.eclipse.core.runtime.CoreException;

public class MetricExtractorSnippet {

	public static void main(String args[]) {
		JavaExtractor extractor = new JavaExtractor();
		try {
			extractor.extractCode("ICSME14");
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
 }