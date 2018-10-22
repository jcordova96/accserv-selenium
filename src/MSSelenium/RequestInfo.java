package MSSelenium;

public class RequestInfo {
	
	public int respCode;
	public String msg;
	public String url;
	public String finalUrl;
	
	
	RequestInfo(int respCode, String msg, String url, String finalUrl) {
		this.respCode = respCode;
		this.msg = msg;
		this.url = url;
		this.finalUrl = finalUrl;
	}
	

}
