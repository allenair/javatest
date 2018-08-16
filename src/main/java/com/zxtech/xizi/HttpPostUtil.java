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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class HttpPostUtil {
	URL url;
	HttpURLConnection conn;
	String boundary = "--------httppostimage";
	Map<String, String> textParams = new HashMap<String, String>();
	Map<String, String> fileparams = new HashMap<String, String>();
	DataOutputStream ds;

	public HttpPostUtil(String url) throws Exception {
		this.url = new URL(url);
	}

	// 重新设置要请求的服务器地址，即上传文件的地址。
	public void setUrl(String url) throws Exception {
		this.url = new URL(url);
	}

	// 增加一个普通字符串数据到form表单数据中
	public void addTextParameter(String name, String value) {
		textParams.put(name, value);
	}

	// 增加一个文件到form表单数据中
	public void addFileParameter(String name, String mapurl) {
		fileparams.put(name, mapurl);
	}

	// 清空所有已添加的form表单数据
	public void clearAllParameters() {
		textParams.clear();
		fileparams.clear();
	}

	// 发送数据到服务器，返回一个字节包含服务器的返回结果的数组
	public byte[] send() throws Exception {
		initConnection();
		try {
			conn.connect();
		} catch (SocketTimeoutException e) {
			// something
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
		for(Iterator iter=textParams.keySet().iterator();iter.hasNext();){
			String key = (String)iter.next();
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
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"image.jpg\"\r\n");
			ds.writeBytes("Content-Type: image/jpg\r\n");
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

	public static void main(String[] args) throws Exception {
//		System.setProperty("javax.net.ssl.keyStore", "D:\\ddd\\test96333\\client.p12");
		System.setProperty("javax.net.ssl.keyStore", "/Users/allen/Desktop/work/96333/xizi/test/client.p12");
		System.setProperty("javax.net.ssl.keyStorePassword", "zhanben");
		System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
//		System.setProperty("javax.net.ssl.trustStore", "d:\\ddd\\test96333\\trust_store_root");
		System.setProperty("javax.net.ssl.trustStore", "/Users/allen/Desktop/work/96333/xizi/test/trust_store_root");
		System.setProperty("javax.net.ssl.trustStorePassword", "zhanben");
		Socket s = null;
		try {
			SocketFactory sf = SSLSocketFactory.getDefault();
			s = sf.createSocket("120.76.132.19", 8444);
//			s = sf.createSocket("www.zhanben.net.cn", 8444);
			PrintWriter writer = new PrintWriter(s.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			writer.println("hello");
			writer.flush();

			System.out.println(reader.readLine());
//			服务报告上传
//			HttpPostUtil u = new HttpPostUtil("https://120.76.132.19:8444/lmp-api/fwbg3rd/scfwbg.action");
//			u.addTextParameter("loginname", "xiao");
//			u.addTextParameter("password", "e10adc3949ba59abbe56e057f20f883e");	
//			String fwbgInfo = "{\"zcdm\":\"35003301062012040002\",\"wbzqId\":\"30ad2f05-7be0-11e7-b046-7cd30abeb162\",\"wbgmc\":\"张三,李四\",\"wjid1\":\"92ee4a43-c237-4e2f-9674-394a4cbb1099\",\"wjid2\":\"92ee4a43-c237-4e2f-9674-394sdsdsdsda\",\"jcmxList\":[{\"syjgDm\":\"1\",\"wbzqmxId\":\"0404c291-7be1-11e7-b046-7cd30abeb162\"},{\"syjgDm\":\"1\",\"wbzqmxId\":\"1272fb2e-7be1-11e7-b046-7cd30abeb162\"},{\"syjgDm\":\"1\",\"wbzqmxId\":\"207eff05-7be1-11e7-b046-7cd30abeb162\"},{\"syjgDm\":\"1\",\"wbzqmxId\":\"2cb485fd-7be1-11e7-b046-7cd30abeb162\"},{\"syjgDm\":\"1\",\"wbzqmxId\":\"5227c741-7be1-11e7-b046-7cd30abeb162\",\"bz\":\"这是一个测试结果\"}]}";
//			u.addTextParameter("fwbgInfo", fwbgInfo);
			
			//修改服务报告
//			HttpPostUtil u = new HttpPostUtil("https://120.76.132.19:8444/lmp-api/fwbg3rd/update.action");
//			u.addTextParameter("loginname", "xiao");
//			u.addTextParameter("password", "e10adc3949ba59abbe56e057f20f883e");	
//			String fwbgInfo = "{'fwbgId':'d481cda7-1545-4c6e-abef-861152f3f6aa',    'zcdm': '30103301002002050001',    'wbzqId': 'f8aa0885-d8a5-11e6-8e58-8038bc0f0519',    'wbgmc': '张三1111111',    'wjid': 'd5df25a7-4474-4f5f-b68c-336392a7c29b',    'wjid1': '92ee4a43-c237-4e2f-9674-394a4cbb1099',    'wjid2': '92ee4a43-c237-4e2f-9674-394sdsdsdsda',    'jcmxList': [        {            'syjgDm': '1',	    'wbzqmxId':'005da51a-0564-11e7-8e58-8038bc0f0519'        },        {            'syjgDm': '1',	    'wbzqmxId':'009428e8-d8a6-11e6-8e58-8038bc0f0519'        },        {            'syjgDm': '1',	    'wbzqmxId':'0094800c-d8a6-11e6-8e58-8038bc0f0519'        }    ]}";
//			u.addTextParameter("fwbgInfo", fwbgInfo);
			
			//获取数据字典
//			HttpPostUtil u = new HttpPostUtil("https://www.zhanben.net.cn:8444/lmp-api/fwbg3rd/sjzd.action");
			HttpPostUtil u = new HttpPostUtil("https://120.76.132.19:8444/lmp-api/fwbg3rd/sjzd.action");
			u.addTextParameter("loginname", "xizi");
			u.addTextParameter("password", "e10adc3949ba59abbe56e057f20f883e");
			
			//取维保单位下所有的维保周期表
//			HttpPostUtil u = new HttpPostUtil("https://120.76.132.19:8444/lmp-api/fwbg3rd/getWbzqByWbdwId.action");
//			u.addTextParameter("loginname", "xiao");
//			u.addTextParameter("password", "e10adc3949ba59abbe56e057f20f883e");	
//			u.addTextParameter("wbdwId", "073dfc47-5ab7-4a27-ac9e-645507ce1603");
			
			//上传使用单位签字
//			HttpPostUtil u = new HttpPostUtil("https://120.76.132.19:8444/lmp-api/fwbg3rd/scsydwqz.action");
//			u.addTextParameter("loginname", "xiao");
//			u.addTextParameter("password", "e10adc3949ba59abbe56e057f20f883e");	
//			u.addTextParameter("fwbgId", "0002b89b-1dcb-4602-b59a-3cf55d196ad7");
//			u.addTextParameter("wjid", "073dfc47-5ab7-4a27-ac9e-645507ce1603");
			
			//上传文件
//			HttpPostUtil u = new HttpPostUtil("https://120.76.132.19:8444/lmp-api/uploadFile?loginname=xiao&password=e10adc3949ba59abbe56e057f20f883e");
//			u.addFileParameter("img", "D:\\image.jpg");
			
			
			
			byte[] b = u.send();
			String result = new String(b);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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