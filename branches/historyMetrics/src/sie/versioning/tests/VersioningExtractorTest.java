package sie.versioning.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sie.db.entity.Change;
import sie.versioning.UnixVersioningExtractorsFactory;
import sie.versioning.VersioningExtractor;

public class VersioningExtractorTest {
	
	private static VersioningExtractor v;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		v = UnixVersioningExtractorsFactory.getFactory().getGitExtractor();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Process p = Runtime.getRuntime().exec("rm -rf vers_tmp/");
		p.waitFor();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyUrl() throws IOException {
		v.extract("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullUrl() throws IOException {
		v.extract(null);
	}
	
	@Test(expected = IOException.class)
	public void testInvalidUrl1() throws IOException {
		v.extract("asdasda");
	}
	
	@Test(expected = IOException.class)
	public void testInvalidUrl2() throws IOException {
		v.extract("www.asdasda");
	}
	
	@Test
	public void testInvalidUrl3() throws IOException {
		Collection<Change> c = v.extract("git://git.code.sf.net/p/siegittestrepository/code");
		assertNotNull(c);
		assertEquals(c.size(), 5);
		for(Change c1 : c)
			assertNotNull(c1);
	}
}