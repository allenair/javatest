package allen.okhttp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpTest {
	private static Logger log = LoggerFactory.getLogger(OKHttpTest.class);
	
	public static final OkHttpClient client = new OkHttpClient();
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	
	
	public static void main(String[] args) throws Exception {
		 
		
//		for(int i=0;i<10;i++) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					for(int k=0;k<10;k++) {
//						try {
//							new OKHttpTest().getFunAsyn("http://localhost:8080/ess/api/inserterrordataforiot.io?hardCode=80008&errorCode=E300-"+k);
//							
//							Thread.sleep(10);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					
//				}
//			}).start();
//		}
		
//		String url = "http://localhost:8080/ess/api/savecallfixdataforiot.io?hardCode=80063&errorDescript=aaaaallllleeeeennnnn" + "&peopleFlag=1";
//		new OKHttpTest().getFunAsyn(url);
		
		final OKHttpTest okhttp = new OKHttpTest();
//		final String url = "http://localhost:9999/essiot/ftiotdata.io";
//		final String json="{\"elevatorId\":\"allentest123\",\"parameterStr\":\"ExX+D31ubgAAY3oBAA==\",\"time\":\"12345678\"}";
		
//		final String url = "http://localhost:9999/essiot/eliotdata.io";
//		final String json="{\"elevatorId\":\"el123\",\"parameterStr\":\"AMEAfwDEtwAA5AwAAAMAAACkgQEPLS0AFwAAAMgBzAAABQACAQEBAAAAAKqqqqoAAAAAAGkAAAAAAAAAAAAAcg==\",\"time\":\"123456789\",\"electric\":\"1\",\"people\":\"1\",\"roomElectric\":\"1\",\"roomMaintain\":\"0\",\"topElectric\":\"1\",\"topMaintain\":\"0\",\"alarm\":\"0\",\"errInfo\":\"100\"}";
//		okhttp.post(url, json);
		
//		int deviceCount=2500;
//		int dataCount = 10;
//		for (int k = 0; k < deviceCount; k++) {
//			new Thread(() -> {
//				for (int n = 0; n < dataCount; n++) {
//					try {
//						okhttp.post(url, json);
//						Thread.sleep(500);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}).start();
//		}
		
		
//		okhttp.mockMailClient();
		
		
//		IntStream.range(0, 10).forEach(n->{
//			new Thread(()->{
//				for(int k=0;k<10;k++) {
//					try {
//						Map<String, String> paraMap = new HashMap<>();
//						paraMap.put("phoneNums", n + "-" + k + "-12345");
//						paraMap.put("content", n + "-" + k + "-内容asdf");
//						new OKHttpTest().post("http://127.0.0.1:9876/util/sendsms.do", paraMap);
//						Thread.sleep(1);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}).start();
//		});
		
		
		okhttp.testUploadFile();
		
		System.out.println("========FIN==========");

	}
	
	public void mockMailClient()  throws Exception {
//		HttpUrl url = new HttpUrl.Builder().scheme("http").host("192.168.0.119").port(8080)
//				.addPathSegment("/xzessutil/util/sendmail.do")
//				.addQueryParameter("address", "yucai.wang@5000m.com")
//				.addQueryParameter("personal", "测试AAA")
//				.addQueryParameter("title", "这是测试发送@"+System.currentTimeMillis())
//				.addQueryParameter("content", "<div><a href='http://www.baidu.com'>「baidu百度」</a></div>")
//				.build();
		
		HttpUrl url = HttpUrl.parse("http://192.168.0.119:8080/xzessutil/util/sendmail.do");
		url = url.newBuilder()
		.addQueryParameter("address", "yucai.wang@5000m.com")
		.addQueryParameter("personal", "测试AAA")
		.addQueryParameter("title", "这是测试发送@"+System.currentTimeMillis())
		.addQueryParameter("content", "<div><a href='http://www.baidu.com'>「baidu百度」</a></div>")
		.build();
		
		
		System.out.println(url.toString());
		
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		System.out.println(response.body().string());
	}
	
	public void getFunAsyn(String url) throws Exception {
		Request request = new Request.Builder().url(url).build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(response.isSuccessful()) {
					log.info(response.body().string());
				}else {
					throw new IOException("Some Error Happen: " + response);
				}
			}
			@Override
			public void onFailure(Call call, IOException err) {
				log.error(err.getMessage());
			}
		});
	}
	
	public String post(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
	public String post(String url, Map<String, String> paraMap) throws IOException {
		FormBody.Builder builder = new FormBody.Builder();
		for (String key : paraMap.keySet()) {
			builder.add(key, paraMap.get(key));
		}
		FormBody body = builder.build();

		Request request = new Request.Builder()
				.header("Content-Type", "application/x-www-form-urlencoded")
				.url(url)
				.post(body).build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}
	
	public void postAsyn(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(response.isSuccessful()) {
					log.info(response.body().string());
					System.out.println("===========TTTTT==="+System.currentTimeMillis());
				}else {
					throw new IOException("Some Error Happen: " + response);
				}
			}
			@Override
			public void onFailure(Call call, IOException err) {
				log.error(err.getMessage());
			}
		});
	}
	
	public void testUploadFile() throws IOException {
		File file = new File("d:/222.png");
		RequestBody requestBody = new MultipartBody.Builder()
			    .setType(MultipartBody.FORM)
			    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
			    .build();
		
		Request request = new Request.Builder().url("http://localhost:8780/pub/upfile.do").post(requestBody).build();
		try (Response response = client.newCall(request).execute()) {
			System.out.println(response.body().string());
		}
	}
	
}
