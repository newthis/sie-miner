package sie.versioning;

import java.io.IOException;

public class GitExtractorSnippet {

	public static void main(String args[]) {
		UnixGitExtractor extractor = new UnixGitExtractor();
		try {
			extractor.init("https://github.com/apache/subversion.git/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}