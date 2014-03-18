package sie.versioning.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sie.versioning.UnixGitExtractor;
import sie.versioning.UnixVersioningExtractorsFactory;
import sie.versioning.VersioningExtractor;

public class UnixExtractorFactoryTester {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExtractorBuilder() {
		assertNotNull(UnixVersioningExtractorsFactory.getFactory());
	}
	
	@Test
	public void testGitExtractor() {
		VersioningExtractor v = UnixVersioningExtractorsFactory.getFactory().getGitExtractor();
		assertNotNull(v);
		assertEquals(v.getClass(), UnixGitExtractor.class);
	}

	@Test (expected = UnsupportedOperationException.class)
	public void testCvsExtractor() {
		UnixVersioningExtractorsFactory.getFactory().getCvsExtractor();
	}
	
	@Test (expected = UnsupportedOperationException.class)
	public void testSvnExtractor() {
		UnixVersioningExtractorsFactory.getFactory().getSvnExtractor();
	}

}
