package com.zxtech;

import com.google.gson.GsonBuilder;
import com.zxtech.bean.UpHardAnalysis;

public class RedisTest {
	private static GsonBuilder gsonBulder = new GsonBuilder();
	static{
        gsonBulder.registerTypeAdapter(java.sql.Timestamp.class, JsonUtil.TIMESTAMP);
	}
	
	public static void main(String[] args) {
		String srcJson = "{\"up_time\":\"2016-08-28 23:36:33\",\"elevator_code\":\"900000000000000010002\",\"err\":\"0\",\"nav\":\"1\",\"ins\":\"1\",\"run\":\"0\",\"do_p\":\"0\",\"dol\":\"0\",\"dw\":\"0\",\"dcl\":\"0\",\"dz\":\"0\",\"efo\":\"0\",\"cb\":\"0\",\"up\":\"0\",\"down\":\"0\",\"fl\":512,\"cnt\":1459617792,\"ddfw\":615713025,\"hxxh\":0,\"es\":\"0\",\"se\":\"1\",\"dfc\":\"1\",\"tci\":\"0\",\"ero\":\"0\",\"lv1\":\"1\",\"lv2\":\"1\",\"ls1\":\"0\",\"ls2\":\"1\",\"dob\":\"0\",\"dcb\":\"0\",\"lrd\":\"0\",\"dos\":\"0\",\"efk\":\"0\",\"pks\":\"0\",\"rdol\":\"0\",\"rdcl\":\"0\",\"rdob\":\"0\",\"rdcb\":\"0\",\"others\":\"000000000000000000000000000000000000000000000\",\"electric_flag\":\"1\",\"people_flag\":\"0\"}";
		UpHardAnalysis bean = gsonBulder.create().fromJson(srcJson, UpHardAnalysis.class);
		
		System.out.println(bean.getInfoMap());
		
		RedisUtil.set("aa", srcJson);
		RedisUtil.set("b:bb", srcJson, 3600);
		
	}

}
