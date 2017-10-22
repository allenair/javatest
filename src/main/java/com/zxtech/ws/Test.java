package com.zxtech.ws;

import java.net.URL;

public class Test {

	public static void main(String[] args)throws Exception {
		BdxService service = new BdxService(new URL("file:///Users/allen/Desktop/BdxService.asmx.xml"));
		
		
		
		System.out.println(service.getBdxServiceSoap().getFetchType());
	}

}
