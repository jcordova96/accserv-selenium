package MSSelenium;


public class App2 {

	public static void main(String[] args) {
		
		
		
		TestForm tester = new TestForm();
		tester.init();
		tester.driverExt = ".exe";
//		tester.setBasePath("");
		tester.setDriver("Chrome");

		tester.run();
	}


}
