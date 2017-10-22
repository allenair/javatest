package com.sinyd.rest;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.jaxrs.client.WebClient;

public class RestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args)  throws Exception {
		WebClient client = WebClient.create("http://localhost:8080/restfultest");
		client.path("rest/test");
		client.accept(MediaType.TEXT_PLAIN_TYPE);
		Response res = client.get();
		InputStream is = (InputStream)res.getEntity();
		System.out.println(getStringFromInputStream(is));
	}

	private static String getStringFromInputStream(InputStream in) throws Exception {
        CachedOutputStream bos = new CachedOutputStream();
        IOUtils.copy(in, bos);
        in.close();
        bos.close();
        return bos.getOut().toString();
    }
}
