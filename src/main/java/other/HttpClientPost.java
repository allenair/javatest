package other;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientPost {
	private static String url = "http://www.tyyb.gov.cn/query/medicinalCatelogAction!getMedicinalCatelog.do";
	private static CookieStore cookieStore;
	private static Map<String, String> params = new HashMap<String, String>();

	public static void main(String[] args) throws Exception {
		// dto%5B'yka151'%5D=10&gridInfo%5B'mlxxGrid_limit'%5D=100&gridInfo%5B'mlxxGrid_start'%5D=0
		params.put("dto['yka151']", "10");
		params.put("gridInfo['mlxxGrid_limit']", "100");
		params.put("gridInfo['mlxxGrid_start']", "100");

		setCookie();
		getBody();
	}

	private static void setCookie() throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		HttpResponse response = httpclient.execute(httpost);
		cookieStore = httpclient.getCookieStore();
//		System.out.println(">>Cookie<<"+cookieStore);

		httpclient.close();
	}

	private static void getBody()throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.setCookieStore(cookieStore);
		
		HttpPost httpost = new HttpPost(url);
		httpost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		httpost.setHeader("Accept-Encoding", "gzip, deflate");
		httpost.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		httpost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		httpost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		httpost.setHeader("Cookie", "JSESSIONID=2nvyTpSQ7GdyGLdRDQZpk6Mh5V3Y1YQhQzJkQYLsqRnCwJXg1J8N!-1356714421; clientlanguage=zh_CN");
		
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		
		HttpResponse response = httpclient.execute(httpost);
		String body = paseResponse(response);

		System.out.println("##"+body);
		httpclient.close();
	}
	
	
	private static String paseResponse(HttpResponse response) {
		HttpEntity entity = response.getEntity();

		String charset = EntityUtils.getContentCharSet(entity);

		String body = null;
		try {
			body = EntityUtils.toString(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return body;
	}

}
