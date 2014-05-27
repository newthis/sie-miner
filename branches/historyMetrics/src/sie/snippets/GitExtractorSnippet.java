package sie.snippets;

import java.io.IOException;

import sie.versioning.UnixGitExtractor;

public class GitExtractorSnippet {

	public static void main(String args[]) {
		UnixGitExtractor extractor = new UnixGitExtractor();
		try {
			extractor.extract("https://github.com/apache/subversion.git/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}