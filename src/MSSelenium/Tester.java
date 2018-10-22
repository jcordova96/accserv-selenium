package MSSelenium;

import java.awt.Toolkit;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.net.ssl.SSLHandshakeException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Tester {

	protected WebDriver driver;
	// protected String baseUrl;
	protected HttpURLConnection huc = null;
	JavascriptExecutor executor;
	protected static final Logger LOGGER = Logger.getLogger(Tester.class.getName());

	protected Boolean debug = false;
	public Hashtable<String, HttpRequest> urlChecked;
	protected Hashtable<String, Boolean> pageChecked;
	public Hashtable<String, Boolean> ignoreDomain;
	protected Hashtable<String, String> test404;
	protected Hashtable<String, VPDim> viewPorts;
	protected Boolean loggedIn = false;
	protected String currentPage;
	protected String currentViewport = "";
	protected String logBuffer = "";
	protected String driverType = "";
	protected String basePath = "";
	public String driverExt = "";
	protected int screenHeight = 0;
	public String[] pages;

	public History history;

	public TestCaseConfig config = new TestCaseConfig();
	
	public ArrayList<String> removeClasses = new ArrayList<String>();
	public ArrayList<String> removeIds = new ArrayList<String>();

	// -------------------------------------------------------------------------------------------------------------->
	// Setup

	public Tester() {
//		setDriver("Chrome");
	}

	public Tester(WebDriver suppliedDriver) {
		driver = suppliedDriver;
	}

	public Tester(String driverType) {
		this.driverType = driverType;
	}

	public void init() {
		
		this.basePath = "C:\\Selenium\\";
		
		if(this.driverType.equals("")) {
			this.driverType = "Chrome";
		}
		
		String OS = System.getProperty("os.name");
		if(OS.startsWith("Windows")) {
//			this.driverExt = ".exe";
		}
		// System.setProperty("http.maxRedirects", "100");

//		setDriver(this.driverType);

		urlChecked = new Hashtable<String, HttpRequest>();
		pageChecked = new Hashtable<String, Boolean>();
		ignoreDomain = new Hashtable<String, Boolean>();
		ignoreDomain.put("https://cmspreview2.corp.microsoft.com", true);
		test404 = new Hashtable<String, String>();
		history = new History();

		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenHeight = (int) screenSize.getHeight();
		viewPorts = new Hashtable<String, VPDim>();
		viewPorts.put("mobile", new VPDim(375, screenHeight));
		viewPorts.put("tablet", new VPDim(769, screenHeight));
		viewPorts.put("desktop", new VPDim(1400, screenHeight));

		executor = (JavascriptExecutor) driver;

		setTrustAllCerts();
	}
	
	public void setDriverType(String type) {
		driverType = type;
	}

	public void setBasePath(String path) {
		basePath = path;
	}

	public void setDriver(String type) {

		File file;

		switch (type) {

		case "Firefox":
			file = new File(basePath + "geckodriver" + driverExt);
			System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
			driver = new FirefoxDriver();
			break;

		case "Chrome":
			file = new File(basePath + "chromedriver" + driverExt);
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			driver = new ChromeDriver();
			break;

		case "Edge":
			file = new File(basePath + "MicrosoftWebDriver" + driverExt);
			System.setProperty("webdriver.edge.driver", file.getAbsolutePath());
			driver = new EdgeDriver();
			break;

		default:
			file = new File(basePath + "geckodriver" + driverExt);
			System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
			driver = new FirefoxDriver();
			break;

		}

		driver.manage().timeouts().implicitlyWait(config.timeout, TimeUnit.SECONDS);
	}

	public void setPages(String[] slugs) throws Exception {

		if (slugs != null && config.source == "slugs") {
			pages = new String[slugs.length];
			for (int i = 0; i < slugs.length; i++) {
				if (config.mode == "sitemuse-dev") {
					pages[i] = "https://cmspreview2.corp.microsoft.com/www.microsoft.com/en-us/" + config.site + "/"
							+ slugs[i] + "?CollectionId=" + config.collectionId;
				} else if(config.mode == "live" && !config.domain.equals("")) {
					
					String url = config.domain;
					if(!config.site.equals("")) {
						url += "/" + config.site;
					}
					
					if (!slugs[i].equals("")) {
						url += "/" + slugs[i];
					}
					
					pages[i] = url;
				} else {
					throw new Exception("Could not load any pages.  Configuration problem.");
				}
			}
		} else if (config.sitemapUrl != null && config.source == "sitemap") {
			pages = getPagesFromSiteMap(config.sitemapUrl);
		} else {
			throw new Exception("Could not load any pages.  Configuration problem.");
		}
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setPageUrl(String url) {
		driver.get(url);
	}

	public void login() {
		if (!loggedIn) {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Trademarks")));
			loggedIn = true;
		}
	}

	public History getHistory() {
		return this.history;
	}

	// -------------------------------------------------------------------------------------------------------------->
	// Generic procedures

	public String[] getPagesFromSiteMap(String sitemapUrl) {
		List<String> pages = new ArrayList<String>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(sitemapUrl);
			NodeList nList = doc.getElementsByTagName("ns1:url");

			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				Element elem = (Element) node;
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String url = elem.getElementsByTagName("ns1:loc").item(0).getTextContent();
					pages.add(url);
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return pages.toArray(new String[0]);
	}

	public void validateAllAltAria() {

		statusMsg("Checking alt and aria tags...");
		
		String[] patterns = { "//a[(text() or *) and not(@tabindex=-1) and not(@href='#')]" };
		
		for (String xpath : patterns) {
			By by = By.xpath(xpath);
		
			java.util.List<WebElement> elems = (isElementPresent(by)) ? driver.findElements(by) : null;
			if (elems != null) {
				
				for (int i = 0; i < elems.size(); i++) {
					
					
				}
			}
		}
	}

	
	public void validateAllLinks() {

		statusMsg("Checking links...");
		
		String url;
		String label = "";
		String tag;
		
		// make a list of urls to be checked
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> temp2 = new ArrayList<String>();
		ArrayList<String> temp3 = new ArrayList<String>();
		
		for (String xpath : config.elemMatchPatterns.get("links")) {
			By by = By.xpath(xpath);
			java.util.List<WebElement> links = (isElementPresent(by)) ? driver.findElements(by) : null;
			if (links != null) {
				
				Hashtable<String, Boolean> imgsChecked = new Hashtable<String, Boolean>();
				
				for (int j = 0; j < links.size(); j++) {
					String type = "";
					WebElement link = links.get(j);
					tag = link.getTagName();
					
					switch(tag) {
					case "a":
						url = link.getAttribute("href");
						type = "link check";
						break;
						
					case "img":
						url = link.getAttribute("src");
						if(imgsChecked.get(url) != null) {
							continue;
						}
						imgsChecked.put(url, true);
						label = (link.getText().equals(null)) ? "" : link.getText();
						type = "image check";
						break;
						
					default:
						continue;
					}
					
					// if null, not http/https, or already checked, do not send a new get request
					if(url != null && url.startsWith("http") && pageChecked.get(url) == null) {
						temp.add(url);
						temp2.add(label);
						temp3.add(type);
					}
					
					// make a list of all of the urls appearing on the page
					
					
				}
			}
		}
		
		// send get requests multithreaded
		try {
			String[] urls = temp.toArray(new String[temp.size()]);
			String[] labels = temp2.toArray(new String[temp2.size()]);
			String[] types = temp3.toArray(new String[temp3.size()]);
			ClientMultiThreadedExecution.main(this.currentPage, urls, labels, types, this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// move temp entries into the main historyItem list, this is done here to keep grouping
			// and sorting correct since threads will not complete syncronously
        	history.processTemp();
		}
		
		// record results to history
		
		
//		println("Already checked");
//		status = request.status;
//		msg = request.errMsg;
//		finalUrl = request.finalUrl;
//		repeat = "x";


		
		
		// unhighlight(link);
		statusMsg("idle...");
	}

	public void validateLink(WebElement link) {

		String label = link.getText();
		String ariaLabel = link.getAttribute("aria-label");
		String href = link.getAttribute("href");
		String msg = "";
		String status = "";
		String finalUrl = "";
		String repeat = "";
		int respCode = 0;

		// highlight(link);
		// scrollIntoView(link);
		
		if(href == null) {
			
		}
		else if (!href.startsWith("http") || pageChecked.get(this.currentPage) != null) {

		} else {

			println("------------------------------------------------------");
			println("Label: " + label);
			println(href);

			HttpRequest request = urlChecked.get(href);

			// if not url not yet checked
			if (request == null) {

				try {
					// send head request to check if link is ok
					RequestInfo info = sendRequest(href, "GET");
					respCode = info.respCode;
					if (!href.equals(info.finalUrl)) {
						finalUrl = info.finalUrl;
					}
					checkDomain404Validity(info.finalUrl);

					// if head request fails verify that a get request will also fail
//					if (info.respCode >= 400) {
//						info = sendRequest(href, "GET");
//					}

					String domain = HttpRequest.getDomain(info.finalUrl);
					// flag to check if original url's domain is on the ignore list
					Boolean ignore = ignoreDomain.get(HttpRequest.getDomain(info.url));

					// if on the ignore list - dev links, etc.
					if (ignore != null && ignore == true) {
						println("Ignore");
						msg = "";
						status = "ignore";
					}
					// if both fail then log an error for the url
					else if (info.respCode >= 400) {
						println("Error: http response code " + info.respCode);
						
						msg = "error: HTTP CODE " + info.respCode + "\n";
						this.logBuffer += msg;
						status = "failed";

						// for passing cases, check that the domain passed the 404 test
					} else {
						// everything is ok!
						if (test404.get(domain) == "passed") {
							println("Passed");
							msg = "";
							status = "passed";
						}
						// domain did not pass the 404 check, recommend checking url
						else {
							println("Check");
							msg = "domain 404 test failed";
							status = "check";
						}
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
					status = "failed";
					msg = "MalformedURLException: " + e.getMessage();
				} catch (UnknownHostException e) {
					e.printStackTrace();
					status = "failed";
					msg = "UnknownHostException: " + e.getMessage();
				} catch (ProtocolException e) {
					e.printStackTrace();
					// status = "failed";
					status = "check";
					msg = "ProtocolException: " + e.getMessage();
				} catch (ConnectException e) {
					e.printStackTrace();
					status = "failed";
					msg = "ConnectException: " + e.getMessage();
				} catch (SSLHandshakeException e) {
					e.printStackTrace();
					status = "failed";
					msg = "SSLHandshakeException: " + e.getMessage();
				} catch (ConnectTimeoutException e) {
					e.printStackTrace();
					status = "failed";
					msg = "ConnectTimeoutException: " + e.getMessage();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					status = "failed";
					msg = "ClientProtocolException: " + e.getMessage();
				} catch (IOException e) {
					e.printStackTrace();
					status = "failed";
					msg = "IOException: " + e.getMessage();
				} finally {

					urlChecked.put(href, new HttpRequest(href, true, respCode, status, msg, finalUrl));

				}

				// if already checked use the values from the previous request
			} else {
				println("Already checked");
				status = request.status;
				msg = request.errMsg;
				finalUrl = request.finalUrl;
				repeat = "x";
			}

			history.add("link check", label, href, finalUrl, this.currentPage, status, msg, repeat, 0);
		}
	}

	public RequestInfo sendRequest(String url, String method) throws IOException {
		
		int timeout = 5;
		RequestConfig config = RequestConfig.custom()
		  .setConnectTimeout(timeout * 1000)
		  .setConnectionRequestTimeout(timeout * 1000)
		  .setSocketTimeout(timeout * 1000)
		  .setCookieSpec(CookieSpecs.STANDARD)
		  .build();
		
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		HttpGet request = new HttpGet(url);
		
		
		HttpClientContext context = HttpClientContext.create();
		HttpResponse response = client.execute(request, context);
		
		List<URI> redirectURIs = context.getRedirectLocations();
		URI finalURI = URI.create(url);
	    if (redirectURIs != null && !redirectURIs.isEmpty()) {
	        for (URI redirectURI : redirectURIs) {
	            println("Redirect URI: " + redirectURI);
	        }
	        finalURI = redirectURIs.get(redirectURIs.size() - 1);
	    }
		
		int respCode = response.getStatusLine().getStatusCode();
		String msg = new Integer(respCode).toString();
		client.close();
		String finalUrl = (!finalURI.equals(null)) ? finalURI.toString() : url; 
		return new RequestInfo(respCode, msg, url, finalUrl);
	}

	public RequestInfo sendRequest2(String url, String method) throws MalformedURLException, IOException {

		URL resourceUrl, base, next;
		String location;
		Integer respCode;
		int i = 0;
		String origUrl = url;

		while (true) {

			i++;
			if (i > config.maxRedirects) {
				throw new ProtocolException("Too many redirects");
			}

			resourceUrl = new URL(url);
			huc = (HttpURLConnection) resourceUrl.openConnection();
			huc.setRequestMethod(method);
			huc.setInstanceFollowRedirects(false);
//			huc.setConnectTimeout(15000);
//			huc.setReadTimeout(15000);
//			huc.setDoOutput(false);
			huc.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			huc.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
			huc.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
			huc.setRequestProperty("Connection", "keep-alive");
			
			huc.connect();
			
			respCode = huc.getResponseCode();

			switch (respCode) {
			case HttpURLConnection.HTTP_MOVED_PERM:
			case HttpURLConnection.HTTP_MOVED_TEMP:
				location = huc.getHeaderField("Location");
				location = URLDecoder.decode(location, "UTF-8");
				base = new URL(url);
				next = new URL(base, location); // Deal with relative URLs
				url = next.toExternalForm();
				continue;
			}

			break;
		}
		
		huc.disconnect();

		return new RequestInfo(respCode, "", origUrl, url);
	}

	protected void checkDomain404Validity(String href) {
		// check domain 404 validity
		try {
			String domain = HttpRequest.getDomain(href);

			if (domain != null) {

				if (test404.get(domain) == null) {
					String href404 = domain + "/aldkjfsoidfmelwidjfosidafalksjdflsdkjflskdjfalksdjf";
					RequestInfo info = sendRequest(href404, "GET");

					if (info.respCode != 404) {
						println("Forced 404 did not return 404!!!");
						test404.put(domain, "failed");
					} else {
						// println("404 test passed");
						test404.put(domain, "passed");
					}
				}

			} else {
				throw new Exception("Invalid domain on 404 check");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void validateAllPivotTables(String xpath) {

		statusMsg("Checking pivot tables...");

		pollingFn();

		xpath = (xpath.equals(null) || xpath.equals(""))
				? "//div[contains(@class, 'c-pivot')]//a[contains(@role, 'tab')]"
				: xpath;
		By by = By.xpath(xpath);

		java.util.List<WebElement> elems = (isElementPresent(by)) ? driver.findElements(by) : null;
		if (elems == null) {
			return;
		}

		// process each pivot table
		for (int i = 0; i <= elems.size(); i++) {
			WebElement elem = elems.get(i);
			highlight(elem);
			scrollIntoView(elem);
			executor.executeScript("arguments[0].click();", elem);
			sleep(config.delay);
			unhighlight(elem);
		}

		statusMsg("idle");
	}

	public void validateAllCarousels(String xpath) {

		statusMsg("Checking carousels...");

		pollingFn();

		xpath = (xpath.equals(null) || xpath.equals(""))
				? "//div[contains(@class, 'c-carousel')]//button[contains(@class, 'f-next')]"
				: xpath;
		By by = By.xpath(xpath);

		java.util.List<WebElement> elems = (isElementPresent(by)) ? driver.findElements(by) : null;
		if (elems == null) {
			return;
		}

		// process each pivot table
		for (int i = 0; i <= elems.size(); i++) {
			WebElement elem = elems.get(i);

			for (int j = 0; j < 5; j++) {
				highlight(elem);
				scrollIntoView(elem);

				executor.executeScript("arguments[0].click();", elem);

				sleep(config.delay);
				unhighlight(elem);
			}
		}

		statusMsg("idle...");
	}

	public void scrollPage() {

		statusMsg("Scrolling the page...");

		pollingFn();
		scrollTop();
		sleep(config.delay * 4);
		String hit = "false";

		while (hit.trim().equals("false")) {
			scrollOnePage();
			sleep(config.delay * 2);
			hit = hitBottom();
		}

		statusMsg("idle...");
	}

	public void closePopups() {
		ArrayList<String> xpaths = new ArrayList<String>() {
			{
				add("//div[contains(@id, 'csInv')]//img[contains(@alt, 'Close Survey Invite')]");
				add("//img[contains(@alt, 'Close chat offer')]");
			}
		};

		for (String xpath : xpaths) {
			By by = By.xpath(xpath);
			WebElement elem = (isElementPresent(by)) ? driver.findElement(by) : null;
			if (elem == null) {
				return;
			}

			try {
				elem.click();
			} catch (ElementNotInteractableException e) {
			}
		}
	}

	public void pollingFn() {
		closePopups();
	}

	public void removeUHF() {
		removeFromDOMById("epb");
		removeFromDOMByClass("c-uhfh-gcontainer");
		removeFromDOMByClass("c-uhff-nav");
		removeFromDOMByClass("c-uhff-base");

		// works for dynamics365
		removeFromDOMByClass("js-cat-head");
		
		// yoursurfacestudio
		removeFromDOMById("footer-nav");
		
		// cloud platform
		removeFromDOMById("Default_SHARE_FollowUsToolbar");

	}
	
	public void removeElemsSiteSpecific() {
		if(!removeClasses.isEmpty()) {
			for(String className : removeClasses) {
				removeFromDOMByClass(className);
			}
		}
		if(!removeIds.isEmpty()) {
			for(String id : removeIds) {
				removeFromDOMById(id);
			}
		}
	}
	

	// -------------------------------------------------------------------------------------------------------------->
	// Batch procedures

	public void runTestsOnPages() {
		for (String page : pages) {

			this.currentPage = page;

			if (ArrayUtils.contains(config.viewports, "desktop")) {
				runTests("desktop");
			}
			if (ArrayUtils.contains(config.viewports, "tablet")) {
				runTests("tablet");
			}
			if (ArrayUtils.contains(config.viewports, "mobile")) {
				runTests("mobile");
			}
		}

		history.saveAsXls();
		// log(this.logBuffer);
	}

	protected void runTests(String viewport) {

		setViewportSize(getVPWidth(viewport), screenHeight);
		this.currentViewport = viewport;

		try {

			// go to the current page
			setPageUrl(this.currentPage);
			if (config.msLogin) {
				login();
			}

			if (config.removeUHF == true) {
				removeUHF();
			}
			
			removeElemsSiteSpecific();

			addStatusWindow();

			// if check links set in the config and not yet checked
			if (pageChecked.get(this.currentPage) == null && ArrayUtils.contains(config.tests, "links")) {

				println("------------------------------------------------------");
				println("------------------------------------------------------");
				println("Checking page: " + this.currentPage);

				println("------------------------------------------------------");
				println("Checking element urls: ");
				validateAllLinks();
			}
		} catch (Exception e) {
			e.printStackTrace();
			println(e.getMessage());
		}

		try {

			if (ArrayUtils.contains(config.tests, "pivot tables")) {
				println("------------------------------------------------------");
				println("Checking pivot tables: ");
				validateAllPivotTables("");
			}

		} catch (Exception e) {

		}

		try {

			if (ArrayUtils.contains(config.tests, "carousels")) {
				println("------------------------------------------------------");
				println("Checking carousels: ");
				validateAllCarousels("");
			}

		} catch (Exception e) {

		}

		if (ArrayUtils.contains(config.tests, "scroll-page")) {
			println("------------------------------------------------------");
			println("Scrolling the page: ");
			scrollPage();
		}

		// bookkeep pages checked
		if (pageChecked.get(this.currentPage) == null) {
			pageChecked.put(this.currentPage, true);
		}

	}

	// -------------------------------------------------------------------------------------------------------------->
	// Helpers

	public void highlight(WebElement element) {
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].setAttribute('style', 'border: 1px solid red; border-radius: 5px;')", element);
	}

	public void unhighlight(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style', '')", element);
	}

	public void setViewportSize(int w, int h) {
		driver.manage().window().setSize(new Dimension(w, h));
	}

	public void scrollIntoView(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0, -300);",
				element);
	}

	public void scrollTop() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)", "");
	}

	public void scrollOnePage() {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + (int) (screenHeight * 0.5) + ")", "");
	}

	public String hitBottom() {
		return (String) ((JavascriptExecutor) driver).executeScript("var d = document.documentElement;"
				+ "var offset = d.scrollTop + window.innerHeight;" + "var height = d.offsetHeight;"
				+ "console.log(offset, height);" + "return (offset >= height - 50) ? 'true' : 'false'", "");
	}

	public void addStatusWindow() {
//		((JavascriptExecutor) driver).executeScript(
//				"document.body.innerHTML += \"<div id='selenium-status-window' style='height: 23px; width: 400px; border: 1px solid red; position: fixed; top: 0; z-index: 1000000000; background-color: white'></div>\";",
//				"");
	}

	public void statusMsg(String msg) {
//		((JavascriptExecutor) driver)
//				.executeScript("document.getElementById('selenium-status-window').innerHTML = \"" + msg + "\";", "");
	}

	public void sleep(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected int getVPWidth(String viewport) {
		int vpWidth = 1400;

		switch (viewport) {

		case "desktop":
			vpWidth = 1400;
			break;

		case "tablet":
			vpWidth = 769;
			break;

		case "mobile":
			vpWidth = 375;
			break;
		}

		return vpWidth;
	}

	protected void log(String msg) {

		FileHandler fh;

		try {
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

			fh = new FileHandler("logs/CnE-error-" + timeStamp + ".log");
			LOGGER.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			LOGGER.info(msg);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void takeScreenshot(String filename) {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenshot, new File("screenshots/" + filename + ".jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Boolean isElementPresent(By by) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(config.timeout, TimeUnit.SECONDS);
		}
	}
	
	public void waitForPageToLoad() {
		new WebDriverWait(driver, 10000).until(
          webDriver -> executor.executeScript("return document.readyState").equals("complete"));
	}

	public void removeFromDOMByClass(String className) {
		executor.executeScript("" + "var elems = document.getElementsByClassName('" + className + "');" + "if(elems) {"
				+ "	 for(var i = 0; i < elems.length; i++){ elems[i].remove(); } " + "}");
	}

	public void removeFromDOMById(String id) {
		executor.executeScript("var elem = document.getElementById('" + id + "'); if(elem) elem.remove();");
	}

	protected TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			// No need to implement.
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			// No need to implement.
		}
	} };

	protected void setTrustAllCerts() {
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			println(e.getMessage());
		}
	}
	
	public static void println(String msg) {
		
		System.out.println(msg);
		
	}

}
