package com.zxtech.xio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class Xio96333 {

//	private static String URL = "https://120.76.132.19:8444/lmp-api/json.jsp";
	private static String URLStr = "https://120.76.132.19:8444/lmp-api/fwbg3rd/sjzd.action";

	public static void testClientCertPEM() throws Exception {
		HttpsURLConnection con;

		URL url = new URL(URLStr);
		con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setUseCaches(false);    
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		con.connect();
		
		StringBuffer params = new StringBuffer();
        //传参数
        params.append("loginname").append("=").append("xiao").append("&")
              .append("password").append("=").append("e10adc3949ba59abbe56e057f20f883e"); // 123456
        byte[] bypes = params.toString().getBytes();
        con.getOutputStream().write(bypes);// 输入参数

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

		reader.close();
		con.disconnect();
	}

	public static void main(String[] args) {
		System.setProperty("javax.net.ssl.keyStore", "D:\\ddd\\test96333\\client.p12");
		System.setProperty("javax.net.ssl.keyStorePassword", "zhanben");
		System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		System.setProperty("javax.net.ssl.trustStore", "D:\\ddd\\test96333\\trust_store_root");
		System.setProperty("javax.net.ssl.trustStorePassword", "zhanben");
//		System.setProperty("javax.net.debug", "ssl,handshake");

		Socket s = null;
		try {
			SocketFactory sf = SSLSocketFactory.getDefault();
			s = sf.createSocket("120.76.132.19", 8444);
			PrintWriter writer = new PrintWriter(s.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			writer.println("hello");
			writer.flush();

			testClientCertPEM();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s!= null)
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
