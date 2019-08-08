package com.zxtech.hitachi;

import java.io.IOException;
import java.util.Map;
import java.util.stream.IntStream;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MultiThreadTest {
	private static String tokenStr="eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NjUxNDQ5NjgsImNsYWltc191c2VyIjoiNzIzZmE4YzdmNDM2MjUyMTM1ZTM4MWY3NGVlMzU3NjEifQ.qBryUD3yTFsHAkeMTePehBNubgYtzdvpnBExEasKPEwOiniJWrry6zTsSZcQnHnQmc-eDBzReaAZslayTzzL8w";
	private static String[] urlArr = {
			"http://localhost/hmt/base/basestationlist.do",
			"http://localhost/hmt/base/basegrouplist.do",
			"http://localhost/hmt/base/company/getcomptreelist.do",
			"http://localhost/hmt/base/company/getattachlist.do",
			"http://localhost/hmt/base/chinalistbytype.do",
			"http://localhost/hmt/base/basearealist.do",
			"http://localhost/hmt/base/empcertificatelistpage.do",
			"http://localhost/hmt/base/empcertificatelistpage.do",
			"http://localhost/hmt/base/emptraininglistpage.do",
			"http://localhost/hmt/base/propertylistpage.do",
			"http://localhost/hmt/base/propertypersonlistpage.do",
			"http://localhost/hmt/base/propertypersonlistpage.do",
			"http://localhost/hmt/base/elevatorlistpage.do",
			"http://localhost/hmt/base/baseHotLinedictionarylist.do",
			"http://localhost/hmt/base/basefaultdutyparamlist.do",
			"http://localhost/hmt/base/baseContractdictionarylist.do",
			"http://localhost/hmt/base/baseMachineTypelist.do",
			"http://localhost/hmt/base/basedeptlist.do",
			"http://localhost/hmt/base/basedictionarylist.do"
	};
	
	public static void main(String[] args) {
		IntStream.range(0, 100).forEach(num -> {
			new Thread(() -> {
				try {
					System.out.println("======"+num+"==START==");
					OkHttpClient client = new OkHttpClient();
					String urlstr = urlArr[num%urlArr.length];
					get(client,urlstr, num);
					System.out.println(">>>>>"+num+">>END>>");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}).start();
		});

	}
	public static void get(OkHttpClient client,String urlstr, int num)  throws Exception {
		int page = num % 7 + 1;
		HttpUrl url = HttpUrl.parse(urlstr);
		url = url.newBuilder()
		.addQueryParameter("_token", tokenStr)
		.addQueryParameter("page", ""+page)
		.build();
		
		System.out.println("##URL##"+num+"##"+url);
		Request request = new Request.Builder().url(url).build();
		client.newCall(request).execute();
	}
	
	public String post(OkHttpClient client,String urlstr, Map<String, String> paraMap) throws IOException {
		FormBody.Builder builder = new FormBody.Builder();
		for (String key : paraMap.keySet()) {
			builder.add(key, paraMap.get(key));
		}
		FormBody body = builder.build();

		Request request = new Request.Builder()
				.header("Content-Type", "application/x-www-form-urlencoded")
				.url(urlstr)
				.post(body).build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}
}
