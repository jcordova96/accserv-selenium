package MSSelenium;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest {
	
	public HttpRequest(String url, Boolean checked, int respCode, String status, String errMsg, String finalUrl) {
		this.url = url;
		this.checked = checked;
		this.status = status;
		this.errMsg = errMsg;
		this.finalUrl = finalUrl;
		this.respCode = respCode;
	}
	
	public String url;
	public Boolean checked;
	public String status;
	public String errMsg;
	public String finalUrl;
	public int respCode;
	
	
	public static String getDomain(String url) {
		String domain = null;
	    URL aURL;
		try {
			aURL = new URL(url);
			domain = aURL.getProtocol() + "://" + aURL.getHost();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return domain;
	}
	
}
