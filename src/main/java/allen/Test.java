package allen;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import allen.okhttp.OKHttpTest;

public class Test {
	private static String tt;
	/**
	 * @param args
	 * @throws ScriptException 
	 */
	public static void main(String[] args) throws Exception {
		Test t = new Test();
		
//		ScriptEngine script = new ScriptEngineManager().getEngineByName("js");
//		script.put("aa", 1.22345678);
//		System.out.println(script.eval("aa==1.22345678"));
//		double aa = 1.12345678;
//		BigDecimal bb = new BigDecimal("1.12345678");
//		System.out.println(aa==1.22345678);
		
//		System.out.println(calConditionStr("12.1", "12.0"));
//		System.out.println(calConditionStr("12.3", "($,18)"));
//		System.out.println(calConditionStr("12.3", "($,$)"));
//		System.out.println(calConditionStr("15", "11,13, 15.0, 15.2,13"));
		
		
		final String url="http://localhost:8888/testpost.do";
//		final String jsondata = "{\"name\":\"Allen\",\"post\":\"aa\",\"sex\":\"male\",\"time\":\""+System.currentTimeMillis()+"\"}";
		final String jsondata = "{\"name\":\"Allen\",\"post\":\"aa\",\"sex\":\"male\",\"time\":\"";
		
		
		for(int i=0; i<30; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						new OKHttpTest().postAsyn(url, jsondata+System.currentTimeMillis()+"\"}");
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}
		
		
//		new OKHttpTest().postAsyn(url, jsondata);
//		new OKHttpTest().post(url, jsondata);
		
		
		System.out.println("===========END============");
	}
	
	public static Timestamp getNowTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	
	public static boolean isNumber(String str){  
        String reg = "^[0-9]+(.[0-9]+)?$";  
        return str.matches(reg);  
    }  
	
	private static boolean calConditionStr(String val, String conditionStr) {
		boolean flag = false;
		
		// 如果没有值则认为该条件不成立
		if(StringUtils.isEmpty(val)) {
			return false;
		}
		// 如果没有条件描述，则认为该条件成立
		conditionStr = conditionStr.trim();
		if(StringUtils.isEmpty(conditionStr) || "NA".equalsIgnoreCase(conditionStr)) {
			return true;
		}
		
		BigDecimal valNum = null;
		String valStr = null;
		boolean valNumFlag;
		if(isNumber(val)) {
			valNum = new BigDecimal(val);
			valNumFlag = true;
		}else {
			valStr = val;
			valNumFlag = false;
		}
		
		String firstStr = conditionStr.substring(0, 1);
		String lastStr = conditionStr.substring(conditionStr.length()-1);
		if("(".equals(firstStr) || "[".equals(firstStr)) {
			String[] arr = conditionStr.substring(1,conditionStr.length()-1).split(",");
			arr[0] = arr[0].trim();
			arr[1] = arr[1].trim();
			BigDecimal leftNum, rightNum;
			if("$".equals(arr[0])) {
				leftNum = new BigDecimal(Integer.MIN_VALUE);
			}else {
				leftNum = new BigDecimal(arr[0]);
			}
			if("$".equals(arr[1])) {
				rightNum = new BigDecimal(Integer.MAX_VALUE);
			}else {
				rightNum = new BigDecimal(arr[1]);
			}
			
			if("(".equals(firstStr) && ")".equals(lastStr)) {
				flag = (leftNum.compareTo(valNum) < 0) && (rightNum.compareTo(valNum) > 0);
			}
			if("[".equals(firstStr) && "]".equals(lastStr)) {
				flag = (leftNum.compareTo(valNum) <= 0) && (rightNum.compareTo(valNum) >= 0);
			}
			if("(".equals(firstStr) && "]".equals(lastStr)) {
				flag = (leftNum.compareTo(valNum) < 0) && (rightNum.compareTo(valNum) >= 0);
			}
			if("[".equals(firstStr) && ")".equals(lastStr)) {
				flag = (leftNum.compareTo(valNum) <= 0) && (rightNum.compareTo(valNum) > 0);
			}
			
		}else if(conditionStr.contains(",")) {
			String[] arr = conditionStr.split(",");
			BigDecimal tmpNum;
			for (String string : arr) {
				string = string.trim();
				if(valNumFlag) {
					tmpNum = new BigDecimal(string);
					if(tmpNum.compareTo(valNum)==0) {
						flag = true;
						break;
					}
				}else {
					if(string.equalsIgnoreCase(valStr)) {
						flag = true;
						break;
					}
				}
			}
			
		}else {
			if(valNumFlag) {
				BigDecimal singleNum = new BigDecimal(conditionStr);
				flag = (singleNum.compareTo(valNum)==0);
			}else {
				flag = valStr.equalsIgnoreCase(conditionStr);
			}
			
		}
		
		return flag;
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
