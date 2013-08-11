import java.util.ArrayList;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

/**
 * Author: Lingpeng Kong, lingpenk@cs.cmu.edu
 */
public class CParser {
	public static String currentURI;
	public static String nextURI;
	
	public static DefaultHttpClient httpclient = new DefaultHttpClient();
	public static CookieStore cookieStore = new BasicCookieStore();
	public static HttpContext httpContext = new BasicHttpContext();
	
	public static String getCurrentURI() {
		return currentURI;
	}

	public static void setCurrentURI(String currentURI) {
		CParser.currentURI = currentURI;
	}
	
	public static void goNext(){
		currentURI = nextURI;
		nextURI = null;
	}

	public static ArrayList<String> extractLinks(int numberInPage, String domain) {
		try {
			String s = CClient.getBack(currentURI, httpclient, cookieStore, httpContext);
			if(s == null) return null;
			//System.out.println(s);
			
			setNextPageLink(s,domain);
			ArrayList<String> list = new ArrayList<String>(numberInPage);
			Parser parser = Parser.createParser(s, "utf-8");

			parser.setEncoding(parser.getEncoding());
			NodeList nl = parser
					.extractAllNodesThatMatch(new HasAttributeFilter("class",
							"previewTitle resultTitle Topicsresult"));
			for (int i = 0; i < nl.size(); i++) {
				Node n = nl.elementAt(i);
				//System.out.println(n.getText());
				if (n instanceof TagNode) {
					TagNode tn = (TagNode) n;
					System.out.println(tn.getText());
					list.add(domain + tn.getAttribute("href"));
				}
			}
			if(list!=null && list.size()!=numberInPage){
				System.err.println("List size do not match the number in the page.");
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}
	
	public static void setNextPageLink(String s, String domain){
		try {
			Parser parser = Parser.createParser(s, "utf-8");
			parser.setEncoding(parser.getEncoding());
			NodeList nl = parser
					.extractAllNodesThatMatch(new HasAttributeFilter("title",
							"Next page"));
			TagNode tn =  (TagNode) nl.elementAt(0);
			nextURI = domain + tn.getAttribute("href").replaceAll("&amp;", "&");
		} catch (Exception e) {
			e.printStackTrace();
			nextURI = null;
		}
			
		
		
	}

	public static void main(String[] args) {
	}
}