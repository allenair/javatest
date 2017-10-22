package com.sinyd.rest;

import java.io.InputStream;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RestClientByHttpclient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		getTest();
		postTest();
	}

	private static void getTest() throws Exception {
		HttpClient client = new DefaultHttpClient();

		try {
//            HttpHost target = new HttpHost("localhost", 8080, "http");
//            HttpGet req = new HttpGet("/restfultest/rest/getdata");
//            System.out.println("executing request to " + target);
//            HttpResponse rsp = client.execute(target, req);
            
            HttpGet get = new HttpGet("http://localhost:8080/restfultest/rest/getdata");
            HttpResponse rsp = client.execute(get);
            
            HttpEntity entity = rsp.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(rsp.getStatusLine());
            Header[] headers = rsp.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");

            if (entity != null) {
                System.out.println(EntityUtils.toString(entity));
            }

        } finally {
        	client.getConnectionManager().shutdown();
        }
	}

	private static void postTest() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://localhost:8080/restfultest/rest/savedata");
		StringEntity reqEntity = new StringEntity("dataContent=testaaa");
		reqEntity.setContentType("application/x-www-form-urlencoded");
		post.setEntity(reqEntity);

		client.execute(post);
	}
}
