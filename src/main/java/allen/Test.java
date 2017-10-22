package allen;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


import com.google.gson.Gson;

import deepCopy.TreeBean;

public class Test {
	private static String tt;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test t = new Test();
//		t.hashmapTest();
//		System.out.println((long)(Math.random()*100));
//		System.out.println(new BigDecimal(12.34));
//		
//		System.out.println(t.getMD5Str("Allen��"));
//		
//		for (String string : "com.sinyd.demo.domain".split("\\.")) {
//			System.out.println(string);
//		}
//		
//		System.out.println(replaceStr("wangyc@allen#base_transport_org#list"));
//		String tmp="1234,asd,";
//		System.out.println(tmp.substring(0, tmp.length()-1));
//		
//		
//		String url = "/ //qasp//sysmanage/sysorglist.do";
//		String[] arrUrl = url.split("/");
//		StringBuilder sb = new StringBuilder();
//		int index=0;
//		for (String string : arrUrl) {
//			if(StringUtils.isNotBlank(string)){
//				index++;
//			}
//			if(index>1){
//				sb.append("/").append(string);
//			}
//		}
//		System.out.println(sb.toString());
//		
//		
//		System.out.println((allToJson(1,2,3)));
//		System.out.println((allToJson()));
//		System.out.println((allToJson(null)));
//		System.out.println((allToJson(new TreeBean(), tt, null, new Date(), null)));
//		System.out.println(new Date());
//		
//		System.out.println(checkIP("127.0.0.1"));
//		
//		HashMap<String, String> mmm = new HashMap<String, String>();
//		mmm.put("aaa", "aaa");
//		
//		System.out.println(mmm.get("aaa"));
//		System.out.println(mmm.get(null));
		
//		Map<String, String> aa = new HashMap<>();
//		aa.put("01", "aaa");
//		aa.put("02", "bbb");
//		aa.put("13", "bbb");
//		
//		String[] priorityArr = aa.keySet().toArray(new String[0]);
//		Arrays.sort(priorityArr);
//		for (String priorityName : priorityArr) {
//			System.out.println(priorityName);
//		}
		
		
		
		System.out.println(getNowTimestamp().getTime());
		
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println(getNowTimestamp().getTime());
	}
	
	public static Timestamp getNowTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	
	private static boolean checkIP(String ip) {
		if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)
				|| ip.split("\\.").length != 4) {
			return false;
		}
		return true;
	}
	
	public static String replaceStr(String srcStr){
		String resStr = srcStr;
		resStr = resStr.replace("@", "_");
		resStr = resStr.replace("#", "_");
		return resStr.toUpperCase();
	}
	
	private static String allToJson(Object... objArray){
		Gson gson = new Gson();
		if(objArray!=null){
			return gson.toJson(objArray);
		}
		return gson.toJson(new Object[0]);
	}

    public static String getMD5Str(String str) {  
        MessageDigest messageDigest = null;  
  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
  
            messageDigest.reset();  
  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (Exception e) {  
            System.out.println("NoSuchAlgorithmException caught!");  
            System.exit(-1);  
        } 
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5StrBuff.toString();  
    }  
    
	public void test01() {
		DecimalFormat df = new DecimalFormat("####.000");
		System.out.println(df.format(12345.34567890));
		System.out.println(df.format(15.3));
		System.out.println(df.format(16));
		System.out.println(df.format(216));
		System.out.println(df.format(0));
	}
	
	public void test0606(){
		int aa = -123;
		String bitStr = Integer.toBinaryString(aa);
		DecimalFormat df = new DecimalFormat("000");
		System.out.println(bitStr);
		System.out.println(df.format(11));
		
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<32-bitStr.length();i++){
			sb.append("0");
		}
		sb.append(bitStr);
		System.out.println(sb.toString());
		
		ParentClass clazz = new ChildClass("ss");
		System.out.println(clazz.getClass().getName());
		
		try{
			System.out.println("sss");
		}catch(Exception e){
			
		}finally{
			
		}
	}
	
	public void test0711(){
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.YEAR, 2012);
		c1.set(Calendar.MONTH, Calendar.JANUARY);
		c1.set(Calendar.DAY_OF_MONTH, 12);
		
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.YEAR, 2012);
		c2.set(Calendar.MONTH, Calendar.FEBRUARY);
		c2.set(Calendar.DAY_OF_MONTH, 22);
		
		long days = (long)Math.ceil((c2.getTimeInMillis()-c1.getTimeInMillis())/(24*60*60*1000));
		System.out.println(days);
		
		System.out.println(isleapYear(2008));
	}
	
	public boolean isleapYear(int year){
		return year%4==0 && year%100!=0 || year%400==0;
	}
	
	public void deleteSpace(String src){
		StringBuilder sb = new StringBuilder();
		
		String[] strArray = src.trim().split("\\s");
		for (String string : strArray) {
			if(string!=null && string.length()>0){
				sb.append(string.substring(0,1).toUpperCase());
				sb.append(string.substring(1));
				sb.append(" ");
			}
		}
		
		System.out.println(sb.toString());
	}
	
	public void hashmapTest(){
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		for(int i=0;i<10;i++){
			ArrayList<String> list = new ArrayList<String>();
			for(int k=0;k<9;k++){
				list.add("innerList@"+i+"#"+k);
			}
			map.put("map@"+i, list);
		}
		
		
		for (String key : map.keySet()) {
			for (String str : map.get(key)) {
				System.out.print(str+"\t");
			}
			System.out.println();
		}
		
		System.out.println("===================================================");
		
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			for (String str : entry.getValue()) {
				System.out.print(str+"\t");
			}
			System.out.println();
		}
		
	}
}
