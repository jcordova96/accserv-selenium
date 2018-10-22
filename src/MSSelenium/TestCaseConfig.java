package MSSelenium;
import java.util.ArrayList;
import java.util.Hashtable;

public class TestCaseConfig {

	public String site = "";
	public String mode = "live";
	public String source = "";
	public String collectionId = "";
	public String sitemapUrl;
	public Boolean msLogin = true;
	public String domain;
	
	public int delay = 700;
	public String[] viewports;
	public String[] tests;
	public int timeout = 5;
	public int maxRedirects = 20;
	public Boolean removeUHF = true;
	public Hashtable<String, ArrayList<String>> elemMatchPatterns;
	
	public TestCaseConfig() {
//		viewports = new String[] {"desktop", "tablet", "mobile"};
		viewports = new String[] {"desktop"};
		tests = new String[] {"links", "carousels", "pivot tables", "scroll-page"};
		
		domain = "https://www.microsoft.com/en-us";
		
		elemMatchPatterns = new Hashtable<String, ArrayList<String>>();
		
		elemMatchPatterns.put("links", new ArrayList<String>() {{
//			add("//a[contains(@class, 'c-call-to-action')]");
//			add("//a[contains(@class, 'c-hyperlink')]");
//			add("//a[(text() or *) and not(@tabindex=-1) and not(@href='#')]");
			add("//a[text() or *]");
			add("//img[@src]");
		}});
	}
	
	public void setDelay(int msecs) {
		this.delay = msecs;
	}
	
	
}
