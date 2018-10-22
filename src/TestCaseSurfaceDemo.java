import org.junit.*;

import MSSelenium.TestForm;
import MSSelenium.Tester;

public class TestCaseSurfaceDemo {
	
	TestForm tester;

	@Before
	public void setUp() throws Exception {

		tester = new TestForm();
		
		

	}

	@Test
	public void testCase001() throws Exception {
		
		
		tester.run();

	}

	@After
	public void tearDown() throws Exception {
		tester.getDriver().quit();
	}
}
