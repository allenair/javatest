package com.sinyd.allen;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceRef;

import com.sinyd.allen.service.webserviceclient.TestWebService;

public class WebserviceTest {

	@WebServiceRef
	private static TestWebService wws;

	public static void main(String[] args) throws Exception {
		// wws = new TestWebService();
		wws = new TestWebService(
				new URL("http://192.168.1.188/TestWebService/TestWebService.asmx?WSDL"),
				new QName("http://www.sinyd.com/", "TestWebService"));
		String aa = wws.getTestWebServiceSoap().helloWorld("aa", "f");
		System.out.print(aa);
	}

	// private void
}
