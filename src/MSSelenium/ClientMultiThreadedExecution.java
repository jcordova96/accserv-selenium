package MSSelenium;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * An example that performs GETs from multiple threads.
 *
 */
public class ClientMultiThreadedExecution {

    public static void main(String currentPage, String[] args, String[] labels, String[] types, Tester tester) throws Exception {
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        
		int timeout = 5;
		RequestConfig config = RequestConfig.custom()
//				  .setConnectTimeout(timeout * 1000)
//				  .setConnectionRequestTimeout(timeout * 1000)
//				  .setSocketTimeout(timeout * 1000)
				  .setCookieSpec(CookieSpecs.STANDARD)
				  .build();

		CloseableHttpClient httpclient = HttpClientBuilder.create()
				.setDefaultRequestConfig(config)
                .setConnectionManager(cm)
				.build();
		
        try {
            // create an array of URIs to perform GETs on
        	String[] urisToGet = args;
        	
            // create a thread for each URI
            GetThread[] threads = new GetThread[urisToGet.length];
            for (int i = 0; i < threads.length; i++) {
            	
            	if(urisToGet[i] != null) {
            		HttpGet httpget = new HttpGet(urisToGet[i]);
                    threads[i] = new GetThread(httpclient, httpget, i + 1);
                    threads[i].setTester(tester);
                    threads[i].setLabel(labels[i]);
                    threads[i].order = i;
                    threads[i].currentPage = currentPage;
                    threads[i].type = types[i];
            	}
            }

            // start the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].start();
                Thread.sleep(100);
            }

            // join the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].join();
            }

        } finally {
            httpclient.close();
        }
    }

    /**
     * A thread that performs a GET.
     */
    static class GetThread extends Thread {

        private final CloseableHttpClient httpClient;
        private final HttpClientContext context;
        private final HttpGet httpget;
        private final int id;
        
        private Tester tester;
        
    	private CloseableHttpResponse response;
        private String label;
        public int order;
        public String currentPage;
        public String type;

        public GetThread(CloseableHttpClient httpClient, HttpGet httpget, int id) {
            this.httpClient = httpClient;
//            this.context = new BasicHttpContext();
    		this.context = HttpClientContext.create();

            this.httpget = httpget;
            this.id = id;
        }

        /**
         * Executes the GetMethod and prints some status information.
         */
        @Override
        public void run() {
        	
    		String url = "";
    		String msg = "";
    		String status = "";
    		String finalUrl = "";
    		String repeat = "";
    		int respCode = 0;
    		
        	URI uri = httpget.getURI();
        	url = uri.toString().trim();
			HttpRequest request = tester.urlChecked.get(url);
    		
//            System.out.println("processing " + id + ": " + url);
            
        	if(request == null) {
        		
        		// mark/block space before request runs
               	tester.urlChecked.put(url, new HttpRequest(url, true, respCode, status, msg, finalUrl));
        	
	            try {
	                response = httpClient.execute(httpget, context);
	        		respCode = response.getStatusLine().getStatusCode();
	        		msg = new Integer(respCode).toString();
	        		
	                // get final uri
	        		List<URI> redirectURIs = context.getRedirectLocations();
	        		URI finalURI = URI.create(url);
	        	    if (redirectURIs != null && !redirectURIs.isEmpty()) {
	        	        for (URI redirectURI : redirectURIs) {
//	        	            System.out.println("Redirect URI: " + redirectURI);
	        	        }
	        	        finalURI = redirectURIs.get(redirectURIs.size() - 1);
	        	    }
	    			finalUrl = (!finalURI.equals(null) && !finalURI.toString().equals(url)) ? finalURI.toString() : "";

	    			
	    			
	    			
	    			
	    			// TODO: domain test
//					String domain = HttpRequest.getDomain(finalUrl);
					
					
					
					
					
					// flag to check if original url's domain is on the ignore list
					Boolean ignore = tester.ignoreDomain.get(HttpRequest.getDomain(url));
	
					// if on the ignore list - dev links, etc.
					if (ignore != null && ignore == true) {
						msg = "";
						status = "ignore";
					}
					// if both fail then log an error for the url
					else if (respCode >= 400) {
						msg = "error: HTTP CODE " + respCode + "\n";
						status = "failed";
	
					// for passing cases, check that the domain passed the 404 test
					} else {
						// everything is ok!
	//					if (test404.get(domain) == "passed") {
						if (true) {
							msg = "";
							status = "passed";
						}
						// domain did not pass the 404 check, recommend checking url
						else {
							msg = "domain 404 test failed";
							status = "check";
						}
					}
	
					response.close();
	                    
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
					status = "failed";
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
				} catch (Exception e) {
	                System.out.println(id + " - error: " + e);
	            } finally {
	            	// overwrite values after request has completed
	               	tester.urlChecked.put(url, new HttpRequest(url, true, respCode, status, msg, finalUrl));
		        }
	            
        	} else {
        		
				System.out.println("Already checked - " + url);
				status = request.status;
				msg = request.errMsg;
				finalUrl = request.finalUrl;
				respCode = request.respCode;
				repeat = "x";
        	}

        	if(respCode != 0) {
        		System.out.println(respCode + " - " + url);
        	}
        	
        	tester.history.addTemp(this.type, this.label, url, finalUrl, this.currentPage, status, msg, repeat, this.order);
        }
        
        
        public void setTester(Tester tester) {
        	this.tester = tester;
        }

        public void setLabel(String label) {
        	this.label = label;
        }

    }

}