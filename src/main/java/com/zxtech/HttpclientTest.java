package com.zxtech;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

public class HttpclientTest {
	private static String CERT_FILE = "/Users/allen/Downloads/tomcat.keystore";
	private static String URL_STR = "http://192.168.1.242:8080/ZXKoneUI/UpSapAction.do";
	private static String FILE_NAME = "/Users/allen/Desktop/log4j.xml";

	public static void main(String[] args) throws Exception {
//		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//		FileInputStream instream = new FileInputStream(new File(CERT_FILE));
//		trustStore.load(instream, "123456".toCharArray());
//		// 相信自己的CA和所有自签名的证书
//		SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
//				.build();
//		// 只允许使用TLSv1协议
//		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
//				SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		 CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpPost httppost = new HttpPost(URL_STR);

			FileBody bin = new FileBody(new File(FILE_NAME));
			StringBody comment = new StringBody("This is a message!", ContentType.TEXT_PLAIN);

			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file", bin).addPart("name", comment)
					.build();

			httppost.setEntity(reqEntity);

			System.out.println("executing request " + httppost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: " + resEntity.getContentLength());
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

}
