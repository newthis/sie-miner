package sie.parser;

public interface ExtractorFactory {

	public CodeExtractor getJavaExtractor();
	public CodeExtractor getCppExtractor();
	public CodeExtractor getCExtractor();
}