import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.htmlparser.Parser;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

/**
 * Author: Lingpeng Kong, lingpenk@cs.cmu.edu
 */
public class CClient {
	public static final int sleep_time = 0;
	
	public static void main(String[] args) {
		//mainloop(
		//		"http://search.proquest.com/hnpnewyorktimes/results/13FD0F8A26C57C5DAA2/1/$5bqueryType$3dbasic:hnpnewyorktimes$3b+sortType$3drelevance$3b+searchTerms$3d$5b$3cAND$7ccitationBodyTags:mao+zedong$3e$5d$3b+searchParameters$3d$7bNAVIGATORS$3dpubtitlenav,decadenav$28filter$3d110$2f0$2f*,sort$3dname$2fascending$29,yearnav$28filter$3d1100$2f0$2f*,sort$3dname$2fascending$29,yearmonthnav$28filter$3d120$2f0$2f*,sort$3dname$2fascending$29,monthnav$28sort$3dname$2fascending$29,daynav$28sort$3dname$2fascending$29,+RS$3dOP,+chunkSize$3d20,+instance$3dprod.academic,+ftblock$3d740842+1+660848+670831+194104+194001+670829+194000+660843+660840+104,+removeDuplicates$3dtrue$7d$3b+metaData$3d$7bUsageSearchMode$3dBasic,+dbselections$3d1007155,+fdbok$3dN$7d$5d?accountid=9902",
		//       http://search.proquest.com/hnpnewyorktimes/results/13FD0F8A26C57C5DAA2/1/bqueryTypedbasic:hnpnewyorktimesb+sortTypedrelevanceb+searchTermsdbcANDccitationBodyTags:mao+zedongedb+searchParametersdbNAVIGATORSdpubtitlenav,decadenav8filterd110f0f*,sortdnamefascending9,yearnav8filterd1100f0f*,sortdnamefascending9,yearmonthnav8filterd120f0f*,sortdnamefascending9,monthnav8sortdnamefascending9,daynav8sortdnamefascending9,+RSdOP,+chunkSized20,+instancedprod.academic,+ftblockd740842+1+660848+670831+194104+194001+670829+194000+660843+660840+104,+removeDuplicatesdtruedb+metaDatadbUsageSearchModedBasic,+dbselectionsd1007155,+fdbokdNdd?accountid=9902
		
		//		2000, 20, new File("Download"));
//		for(int i = 0; i < args.length; i++){
//			System.out.println(i+"\t"+args[i]);
//		}
		File rf = new File(args[0]);
		LineReader lr = new LineReader(rf);
		String u = lr.readNextLine().trim();
		lr.closeAll();
		mainloop(u,Integer.parseInt(args[1]), Integer.parseInt(args[2]), new File(args[3]));
		
		// downloadI("http://search.proquest.com/pagepdf.openpdfinviewercopy/http:$2f$2fmedia.proquest.com$2fmedia$2fpq$2fhnp$2fdoc$2f118535346$2ffmt$2fai$2frep$2fNONE$3fhl$3d$26cit$253Aauth$3d$26cit$253Atitle$3dSMOKERS$2bASSURED$2bIN$2bINDUSTRY$2bSTUDY$253A$2bReport$2bby$2bTobacco$2bCouncil$2bFinds$2bNo$2b...$26cit$253Apub$3dNew$2bYork$2bTimes$2b$25281923-Current$2bfile$2529$26cit$253Avol$3d$26cit$253Aiss$3d$26cit$253Apg$3d27$26cit$253Adate$3dAug$2b17$252C$2b1964$26ic$3dtrue$26cit$253Aprod$3dProQuest$2bHistorical$2bNewspapers$253A$2bThe$2bNew$2bYork$2bTimes$2b$25281851-2009$2529$26_a$3d20130809052123485$25253A911305-107279-ONE_SEARCH-128.237.146.64-45545-115874978-DocumentImage-null-null-Online-FT-PFT-1964$25252F08$25252F17-1964$25252F08$25252F17---Online--------Historical$252BNewspapers---------PrePaid--T1M6RU1TLVBkZkRvY1ZpZXdCYXNlLWdldE1lZGlhVXJsRm9ySXRlbQ$253D$253D-$25257BP-1007155-9902-PERPETUAL-null-1077354$25257D$26_s$3dJ$252BIZy5vZB3VeBrlkXsuoICULSiQ$253D$23statusbar$3d1$26zoom$3d150?site=hnpnewyorktimes&amp;t:ac=115874978/Record/13FC68EF90F51051705/1",
		// 1, new File(""));
	}

	public static String domain;
	// private static String pre;
	// private static String follow;

	private static int numberInPage;

	public static void mainloop(String startURI, int total, int nip, File dir) {

		// System.out.println(startURI.replace("/1/", "/" + 2 + "/"));
		numberInPage = nip;
		int total_page = (total / numberInPage) + 1;

		domain = startURI.substring(0, startURI.indexOf('/', 7));
		// pre = startURI.substring(0, startURI.lastIndexOf('/') + 1);
		// follow = startURI.substring(startURI.lastIndexOf('/') + 2);
		System.out.println("domain:\t" + domain);
		// System.out.println("pre:\t" + pre);
		// System.out.println("follow:\t" + follow);
		// System.out.println("20th URI:\t" + getIthURI(20));
		CParser.setCurrentURI(startURI);
		for (int i = 1; i <= total_page; i++) {
			finishPage(i, dir);
			CParser.goNext();
		}

	}

	private static void finishPage(int npage, File dir) {
		ArrayList<String> list = null;
		while (list == null) {
			System.out.println("Trying to Link List...");
			list = CParser.extractLinks(numberInPage, domain);
			try {
				Thread.sleep(sleep_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		boolean[] indicator = new boolean[numberInPage + 1];
		for (int j = 1; j <= numberInPage; j++) {
			indicator[j] = false;
		}
		
		int[] times = new int[numberInPage + 1];
		for (int j = 1; j <= numberInPage; j++){
			times[j] = 0;
		}

		boolean finished = false;
		while (!finished) {
			for (int i = 1; i <= numberInPage; i++) {
				if (indicator[i] == false && times[i] < 20) {
					System.out.println("Try to download "
							+ (((npage - 1) * numberInPage) + i));
					if(getILink(list.get(i - 1), ((npage - 1) * numberInPage) + i,
							dir)){
						times[i] = times[i] + 2; // It's more like the webpage simply do not have the link to download
					}else{
						times[i] = times[i] + 1; // It's more like there is something wrong with the connection...
					}
					
					System.out.println("Wait some seconds");
				}
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			boolean flag = true;
			int remain = 0;
			for (int i = 1; i <= numberInPage; i++) {
				if(times[i] >= 20 ){
					// Okay, we will never try this again; We give up....
					indicator[i] = true;
					continue;
				}
				File f = new File(dir.getAbsolutePath() + "/"
						+ (((npage - 1) * numberInPage) + i) + ".pdf");
				if (!f.exists()) {
					System.out.println("Waiting to download: "
							+ (((npage - 1) * numberInPage) + i));
					flag = false;
					remain++;
				} else {
					indicator[i] = true;
				}
			}
			System.out.println("Still " + remain + " Remaining.");
			finished = flag;
		}

	}

	// private static String getIthURI(int i) {
	// return pre + i + follow;
	// }

	public static String getBack(String url, DefaultHttpClient httpclient,
			CookieStore cookieStore, HttpContext httpContext) {
		try {
			HttpGet httpGet = constructHttpGet(url);
			httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			HttpResponse response1 = httpclient.execute(httpGet, httpContext);
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();
			InputStream ins = entity1.getContent();
			StringWriter writer = new StringWriter();
			IOUtils.copy(ins, writer, "utf-8");
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean getILink(String url, int i, File dir) {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = constructHttpGet(url);
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext httpContext = new BasicHttpContext();

		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		try {
			HttpResponse response1 = httpclient.execute(httpGet, httpContext);
			boolean status = (response1.getStatusLine().getStatusCode() == 200);
			if (!status) {
				return false;
			}

			HttpEntity entity1 = response1.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			InputStream ins = entity1.getContent();
			StringWriter writer = new StringWriter();
			IOUtils.copy(ins, writer, "utf-8");
			String rs = writer.toString();
			// System.out.println(rs);
			if (rs.indexOf("href=\"/pagepdf.openpdfinviewercopy/") > 0) {
				rs = rs.substring(rs
						.indexOf("href=\"/pagepdf.openpdfinviewercopy/") + 6);
				rs = rs.substring(0, rs.indexOf("\">"));
			} else {
				Parser parser = Parser.createParser(rs, "utf-8");

				parser.setEncoding(parser.getEncoding());
				NodeList nl = parser.extractAllNodesThatMatch(new StringFilter(
						"Open in PDF Reader"));
				if (nl == null || nl.size() == 0) {
					return true;
				} else {
					TagNode n = (TagNode) nl.elementAt(0).getParent();
					rs = n.getAttribute("href");
				}
			}
			// rs = rs.substring(rs.indexOf("EmbedFile")+16);

			rs = domain + rs;
			rs = rs.replaceAll("&amp;", "&");
			// System.out.println(rs);

			HttpGet httpGet2 = constructHttpGet(rs);

			HttpResponse response2 = httpclient.execute(httpGet2, httpContext);
			Header[] hs = response2.getAllHeaders();
			boolean isPDF = false;
			for (Header h : hs) {
				if (h.getName().equals("Content-Type")
						&& h.getValue().equals("application/pdf")) {
					isPDF = true;
					break;
				}
			}
			if (!isPDF)
				return false;
			HttpEntity entity2 = response2.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			InputStream ins2 = entity2.getContent();
			saveToLocal(ins2, dir.getAbsolutePath() + "/" + i + ".pdf");

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static HttpGet constructHttpGet(String uri) {
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader(
				"Accept",
				"text/html,application/xhtml+xml,application/xml,application/pdf;q=0.9,*/*;q=0.8");
		httpGet.setHeader("Accept-Charset", "utf-8");
		// httpGet.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpGet.setHeader("Accept-Language:", "en-US");
		httpGet.setHeader("Cache-Control", "max-age=0");
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.83 Safari/537.1");
		return httpGet;

	}

	private static void saveToLocal(InputStream instream, String filePath) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					new File(filePath)));
			byte b[] = new byte[1024];
			int n;
			while ((n = instream.read(b)) != -1) {
				out.write(b, 0, n);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
