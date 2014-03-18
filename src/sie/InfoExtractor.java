package sie;

import sie.bugtracking.BugExtractor;
import sie.parser.CodeExtractor;
import sie.versioning.VersioningExtractor;

public interface InfoExtractor extends CodeExtractor, BugExtractor,
		VersioningExtractor {
}