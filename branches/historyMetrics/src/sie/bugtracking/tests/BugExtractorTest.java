package sie.bugtracking.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sie.bugtracking.BugExtractor;
import sie.bugtracking.HttpBugExtractorFactory;
import sie.db.entity.Change;
import sie.db.entity.Issue;

public class BugExtractorTest {
	
	private static BugExtractor b;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		b = HttpBugExtractorFactory.getDefault().getBugzillaExtractor();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//Process p = Runtime.getRuntime().exec("rm -rf vers_tmp/");
		//p.waitFor();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = MalformedURLException.class)
	public void testEmptyUrl() throws IOException {
		b.extractBug(new URL(""),"prova");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyProject() throws IOException {
		b.extractBug(new URL("https://bugzilla.mozilla.org/"),"");
	}

	@Test(expected = MalformedURLException.class)
	public void testEmptyAll() throws IOException {
		b.extractBug(new URL(""),"");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullUrl() throws IOException {
		b.extractBug(null,"prova");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullProject() throws IOException {
		b.extractBug(new URL("https://bugzilla.mozilla.org/"),null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullAll() throws IOException {
		b.extractBug(null,null);
	}
	
	@Test(expected = IOException.class)
	public void testInvalidUrl1() throws IOException {
		b.extractBug(new URL("asdasda"),"prova");
	}
	
	@Test(expected = IOException.class)
	public void testInvalidUrl2() throws IOException {
		b.extractBug(new URL("www.asdasda"),"prova");
	}
	
	@Test
	public void testInvalidUrl3() throws IOException {
		Collection<Issue> c = b.extractBug(new URL("https://bugzilla.mozilla.org/"),"firefox");
		assertNotNull(c);
		assertEquals(c.size(), 100);
		for(Issue c1 : c)
			assertNotNull(c1);
	}
}