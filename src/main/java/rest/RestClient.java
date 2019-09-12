package rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestClient {
	public static String url="http://192.168.1.118:8080/zzc1/registSession.do;jsessionid=16719A607FF87CEA44C419ABBFFD7655";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception  {
		String resultString = new RestClient().load(RestClient.url,"userId=1111");
		System.out.println("resultString>>"+resultString);
	}

	public String load(String url, String query) throws Exception {
		URL restURL = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setAllowUserInteraction(false); 

		PrintStream ps = new PrintStream(conn.getOutputStream());
		ps.print(query);
		ps.close();

		BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		String line, resultStr = "";
		while (null != (line = bReader.readLine())){
			resultStr += line;
		}
		bReader.close();
		
		return resultStr;
	}
}
