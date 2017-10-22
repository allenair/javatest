package com.sinyd.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.common.util.StringUtils;


public class ZipBaseCode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "[{\"type\":\"LN_JLSD_1_RUNTIME_004\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":6,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_009\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":16,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_010\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":18,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_011\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":20,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_012\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":22,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_014\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":26,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_015\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":28,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_002\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":2,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_001\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":0,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_003\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":4,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_005\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":8,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_006\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":10,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_007\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":12,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_008\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":14,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_001\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":0,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_016\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":30,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_013\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":24,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_002\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":2,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_003\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":4,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_004\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":6,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_005\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":8,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_006\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":10,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_007\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":12,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_009\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":16,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_010\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":18,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_011\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":20,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_012\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":22,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_014\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":26,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_015\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":28,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_016\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":30,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_1_RUNTIME_013\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":24,\"slaveId\":1,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}},{\"type\":\"LN_JLSD_2_RUNTIME_008\",\"opt\":\"0\",\"message\":{\"pointType\":3,\"dataType\":5,\"registerId\":14,\"slaveId\":2,\"value\":0,\"innerCode\":\"\",\"ip\":\"127.0.0.1\",\"port\":502,\"timeStampLong\":0}}]@@1339996951319";
		System.out.println(zipStrCodeByBase64(str));
		System.out.println(unzipBase64StrCode(str));
	}

	public static String zipStrCodeByBase64(String srcStr){
		if(StringUtils.isEmpty(srcStr))
			return "";
		
		String res = srcStr;
		ByteArrayOutputStream bos = null;
		GZIPOutputStream os = null;
		byte[] bs = null;
		try{
			bos = new ByteArrayOutputStream();   
			os = new GZIPOutputStream(bos);   
			os.write(srcStr.getBytes("UTF-8"));   
			os.close();
			bs = bos.toByteArray();   
			res = new String(Base64Utility.encode(bs));
			bos.close();
		}catch(Exception e){
			return srcStr;
		}finally{
			os = null;
			bos = null;
		}
		return res;
	}
	
	public static String unzipBase64StrCode(String base64Str){
    	if(base64Str==null || "".equals(base64Str.trim()))
			return "";
    	
    	String res = "";
		ByteArrayInputStream bis = null;   
		ByteArrayOutputStream bos = null;   
		GZIPInputStream is = null;   
		byte[] buf = null;   
		try{
		    bis = new ByteArrayInputStream(Base64Utility.decode(base64Str));   
		    bos = new ByteArrayOutputStream();   
		   	is = new GZIPInputStream(bis);   
		    buf = new byte[1024];   
		    int len;   
		    while ((len = is.read(buf)) != -1) {   
		       bos.write(buf, 0, len);   
		    }   
		    is.close();   
		    bis.close();   
		    bos.close();   
		    
		    res = new String(bos.toByteArray(),"UTF-8");
		}catch(Exception e){
			return base64Str;
		}finally{
			is = null;
			bos = null;
			bis = null;
		}
		return res;
	}
}
