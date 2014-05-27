package sie.parser;

public class EclipseExtractorFactory implements ExtractorFactory {

	private EclipseExtractorFactory() {
	}

	public static ExtractorFactory getFactory() {
		if (builder == null)
			builder = new EclipseExtractorFactory();

		return builder;
	}

	@Override
	public CodeExtractor getJavaExtractor() {
		return new JavaExtractor();
	}

	@Override
	public CodeExtractor getCppExtractor() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public CodeExtractor getCExtractor() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private static ExtractorFactory builder = null;
}
