package sie.versioning;

/**
 * Questa classe implementa {@link VersioningExtractorFactory} per i sistemi unix
 * like.
 * @author Santolo Tubelli.
 * */
public class UnixVersioningExtractorsFactory implements
		VersioningExtractorFactory {

	private UnixVersioningExtractorsFactory() {
	}

	/**
	 * Restituisce una factory per instanziare estrattori di cambiamenti su sistemi unix
	 * @return una factory basata su unix.
	 * */
	public static UnixVersioningExtractorsFactory getFactory() {
		if (builder == null)
			builder = new UnixVersioningExtractorsFactory();
		return builder;
	}

	/**
	 * Restituisce un estrattore di informazioni per git.
	 * */
	@Override
	public VersioningExtractor getGitExtractor() {
		return new UnixGitExtractor();
	}

	/**
	 * Restituisce un estrattore di informazioni per svn.
	 * (Non ancora implementato)
	 * */
	@Override
	public VersioningExtractor getSvnExtractor() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Restituisce un estrattore di informazioni per cvs.
	 * (Non ancora implementato).
	 * */
	@Override
	public VersioningExtractor getCvsExtractor() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private static UnixVersioningExtractorsFactory builder;
}
