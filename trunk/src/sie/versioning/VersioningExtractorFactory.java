package sie.versioning;

/**
 * Quest'interfaccia descrive una Factory generica per instanziare estrattori di
 * informazioni per git svn e cvs.
 * */
public interface VersioningExtractorFactory {
	VersioningExtractor getGitExtractor();

	VersioningExtractor getSvnExtractor();

	VersioningExtractor getCvsExtractor();
}
