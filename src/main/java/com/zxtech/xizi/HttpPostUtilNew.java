package com.zxtech.xizi;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HttpPostUtilNew {
	private static String boundary = "--------httppostimage";
	private static String loginname;
	private static String password;
	private static String serverIP;
	private static int serverPort;
	static {
		System.setProperty("javax.net.ssl.keyStore", "/Users/allen/Desktop/work/96333/xizi/test/client.p12");
		System.setProperty("javax.net.ssl.keyStorePassword", "zhanben");
		System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		System.setProperty("javax.net.ssl.trustStore", "/Users/allen/Desktop/work/96333/xizi/test/trust_store_root");
		System.setProperty("javax.net.ssl.trustStorePassword", "zhanben");

		serverIP = "120.76.132.19";
		serverPort = 8444;
		loginname = "xizi";
		password = "e10adc3949ba59abbe56e057f20f883e";
		
//		loginname = "xizigongcheng";
//		password = "e10adc3949ba59abbe56e057f20f883e";
	}
	
	private URL url;
	private HttpURLConnection conn;
	private Map<String, String> textParams = new HashMap<String, String>();
	private Map<String, String> fileparams = new HashMap<String, String>();
	private DataOutputStream ds;
	
	
	public HttpPostUtilNew(int apiNumber) throws Exception {
//		String urlStr = "https://120.76.132.19:8444";
		String urlStr = "https://test.zhanben.net.cn:8444";
		switch (apiNumber) {
		case 1:
			this.url = new URL(urlStr+"/lmp-api/fwbg3rd/scfwbg.action");
			break;
		case 2:
			this.url = new URL(urlStr+"/lmp-api/fwbg3rd/update.action");
			break;
		case 3:
			this.url = new URL(urlStr+"/lmp-api/uploadFile?loginname="+loginname+"&password="+password);
			break;
		case 4:
			this.url = new URL(urlStr+"/lmp-api/fwbg3rd/sjzd.action");
			break;
		case 5:
			this.url = new URL(urlStr+"/lmp-api/fwbg3rd/getWbzqByWbdwId.action");
			break;
		case 6:
			this.url = new URL(urlStr+"/lmp-api/fwbg3rd/scsydwqz.action");
			break;
		}
	}
	
	// 上传文件（接口3）
	public static String callGovUpFile(String fileFullName) {
		try {
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("fileFullName", fileFullName);
			String resjson = callApiFunc(3, paramMap);
			Map<String, String> resMap = new Gson().fromJson(resjson, new TypeToken<Map<String, Object>>(){}.getType());
			
			if("1".equals(resMap.get("code").toString())) {
				return resMap.get("id").toString();
				
			}else {
				return "";
			}
			
		}catch(Exception e) {
			return "";
		}
	}
	
	// 上传物业签字（接口6）
	public static String callGovManagerSign(String govPlanOrderId, String govFileId) {
		try {
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("fwbgId", govPlanOrderId);
			paramMap.put("wjid", govFileId);
			String resjson = callApiFunc(6, paramMap);
			Map<String, String> resMap = new Gson().fromJson(resjson, new TypeToken<Map<String, Object>>(){}.getType());
			
			if("1".equals(resMap.get("code").toString())) {
				return "ok";
				
			}else {
				return "";
			}
			
		}catch(Exception e) {
			return "";
		}
	}
	
	// 上传服务报告（接口1）
	public static String callGovPlanOrderUp(String planOrderJson) {
		try {
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("fwbgInfo", planOrderJson);
			String resjson = callApiFunc(1, paramMap);
			Map<String, String> resMap = new Gson().fromJson(resjson, new TypeToken<Map<String, Object>>(){}.getType());
			
			if("1".equals(resMap.get("code").toString())) {
				return resMap.get("id").toString();
				
			}else {
				return "";
			}
			
		}catch(Exception e) {
			return "";
		}
	}

	// 增加一个普通字符串数据到form表单数据中
	private void addTextParameter(String name, String value) {
		textParams.put(name, value);
	}

	// 增加一个文件到form表单数据中
	private void addFileParameter(String name, String mapurl) {
		fileparams.put(name, mapurl);
	}

	// 发送数据到服务器，返回一个字节包含服务器的返回结果的数组
	private byte[] send() throws Exception {
		initConnection();
		try {
			conn.connect();
		} catch (SocketTimeoutException e) {
			throw new RuntimeException();
		}
		ds = new DataOutputStream(conn.getOutputStream());
		if(fileparams.size()>0){
			writeFileParams();
		}else{
			writeStringParams();
		}
		
		paramsEnd();
		InputStream in = conn.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int b;
		while ((b = in.read()) != -1) {
			out.write(b);
		}
		conn.disconnect();
		return out.toByteArray();
	}

	// 文件上传的connection的一些必须设置
	private void initConnection() throws Exception {
		conn = (HttpURLConnection) this.url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setConnectTimeout(10000); // 连接超时为10秒
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Charsert", "UTF-8");
		if(fileparams.size()>0){
			conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);
		}else{
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
		}
	}

	// 普通字符串数据
	private void writeStringParams() throws Exception {
		StringBuffer params = new StringBuffer();
		for(Iterator<String> iter=textParams.keySet().iterator();iter.hasNext();){
			String key = iter.next();
			String value = textParams.get(key);
			
			params.append(key).append("=").append(value).append("&");
		}
		byte[] bypes = params.toString().getBytes();
		ds.write(bypes);// 输入参数
	}

	// 文件数据
	private void writeFileParams() throws Exception {
		Set<String> keySet = fileparams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			String filePath = fileparams.get(name);
	        //读取本地图片文件流
			FileInputStream inputStream = new FileInputStream(new File(filePath));
			ds.writeBytes("--" + boundary + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"image.png\"\r\n");
			ds.writeBytes("Content-Type: image/png\r\n");
			ds.writeBytes("\r\n");
			ds.write(getBytes(inputStream));
			ds.writeBytes("\r\n");
			inputStream.close();
//			conn.disconnect();
		}
	}

	// 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
	private String getContentType(File f) throws Exception {

		// return "application/octet-stream"; //
		// 此行不再细分是否为图片，全部作为application/octet-stream 类型
		ImageInputStream imagein = ImageIO.createImageInputStream(f);
		if (imagein == null) {
			return "application/octet-stream";
		}
		Iterator<ImageReader> it = ImageIO.getImageReaders(imagein);
		if (!it.hasNext()) {
			imagein.close();
			return "application/octet-stream";
		}
		imagein.close();
		return "image/" + it.next().getFormatName().toLowerCase();// 将FormatName返回的值转换成小写，默认为大写

	}

	// 把文件转换成字节数组
	private byte[] getBytes(InputStream in) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int n;
		while ((n = in.read(b)) != -1) {
			out.write(b, 0, n);
		}
		in.close();
		return out.toByteArray();
	}

	// 添加结尾数据
	private void paramsEnd() throws Exception {
		ds.writeBytes("--" + boundary + "--" + "\r\n");
		ds.writeBytes("\r\n");
	}

	// 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
	private String encode(String value) throws Exception {
		return URLEncoder.encode(value, "UTF-8");
	}

	// （Allen）此处socket部分是给出的示例代码中的功能，必须在实际传输前调用一次，否则接口不通
	public static String callApiFunc(int apiNumber, Map<String, String> paramMap) throws Exception {
		Socket s = null;
		try {
			SocketFactory sf = SSLSocketFactory.getDefault();
			s = sf.createSocket(serverIP, serverPort);
			PrintWriter writer = new PrintWriter(s.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			writer.println("hello");
			writer.flush();

			HttpPostUtilNew u = new HttpPostUtilNew(apiNumber);
			boolean flag=false;
			switch (apiNumber) {
			case 1: // 1、服务报告上传
				u.addTextParameter("loginname", loginname);
				u.addTextParameter("password", password);
				u.addTextParameter("fwbgInfo", paramMap.get("fwbgInfo"));
				flag = true;
				break;
				
			case 2: // 2、服务报告修改,目前不使用此接口
				break;
				
			case 3: // 3、图片上传
				u.addFileParameter("img", paramMap.get("fileFullName"));
				flag = true;
				break;
				
			case 4: //4. 获取数据字典
				u.addTextParameter("loginname", loginname);
				u.addTextParameter("password", password);
				flag = true;
				break;
				
			case 5: //5. 取维保单位下所有的维保周期表,目前不使用此接口
				break;
				
			case 6: // 6. 使用单位签字上传
				u.addTextParameter("loginname", loginname);
				u.addTextParameter("password", password);
				u.addTextParameter("fwbgId", paramMap.get("fwbgId"));
				u.addTextParameter("wjid", paramMap.get("wjid"));
				flag = true;
				break;
			}
			
			String result="";
			if(flag) {
				byte[] b = u.send();
				result = new String(b);
			}
			return result;
			
		} catch (Exception e) {
			throw e;

		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException ee) {
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
//		Map<String, Object> govInfoMap = new HashMap<>();
//		govInfoMap.put("zcdm", "35003301062012040002");
//		govInfoMap.put("wjid1", "92ee4a43-c237-4e2f-9674-394a4cbb1099");
//		govInfoMap.put("wjid2", "92ee4a43-c237-4e2f-9674-394sdsdsdsda");
//		govInfoMap.put("wbgmc", "张三,李四,王五");
//		govInfoMap.put("wbzqId", "30ad2f05-7be0-11e7-b046-7cd30abeb162");
//		
//		
//		
//		List<Map<String, String>> infoJsonList = new ArrayList<>();
//		Map<String, String> row = new HashMap<>();
//		row.put("syjgDm", "1");
//		row.put("wbzqmxId", "0404c291-7be1-11e7-b046-7cd30abeb162");
//		infoJsonList.add(row);
//		
//		row = new HashMap<>();
//		row.put("syjgDm", "1");
//		row.put("wbzqmxId", "5227c741-7be1-11e7-b046-7cd30abeb162");
//		row.put("bz", "这是9月22日测试结果");
//		infoJsonList.add(row);
//		
//		govInfoMap.put("jcmxList", infoJsonList);
//		
//		String resStr = new Gson().toJson(govInfoMap);
//		System.out.println("==="+resStr);
//		Map<String, String> paramMap = new HashMap<>();
//		paramMap.put("fwbgInfo",  resStr);
//		
//		System.out.println(callApiFunc(1, paramMap));
		
		System.out.println(callApiFunc(4, new HashMap<>()));
	}
}

/*
 * {
  "zcdm": "35003301062012040002",
  "wbzqId": "30ad2f05-7be0-11e7-b046-7cd30abeb162",
  "wbgmc": "张三,李四",
  "wjid1": "92ee4a43-c237-4e2f-9674-394a4cbb1099",
  "wjid2": "92ee4a43-c237-4e2f-9674-394sdsdsdsda",
  "jcmxList": [
    {
      "syjgDm": "1",
      "wbzqmxId": "0404c291-7be1-11e7-b046-7cd30abeb162"
    },
    {
      "syjgDm": "1",
      "wbzqmxId": "1272fb2e-7be1-11e7-b046-7cd30abeb162"
    },
    {
      "syjgDm": "1",
      "wbzqmxId": "207eff05-7be1-11e7-b046-7cd30abeb162"
    },
    {
      "syjgDm": "1",
      "wbzqmxId": "2cb485fd-7be1-11e7-b046-7cd30abeb162"
    },
    {
      "syjgDm": "1",
      "wbzqmxId": "5227c741-7be1-11e7-b046-7cd30abeb162",
      "bz": "这是9月22日测试结果"
    }
  ]
}
 * */



