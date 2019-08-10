package com.zxtech.hitachi.queue;

public class MockEsb {

	public static void main(String[] args) throws Exception{
		QueueUtil.init();

		String url = "xxxxxhttp://localhost:8080/hess/api/syncStaffCertificateInfo.api";
		String requestData = "{\"rtnCode\":\"S\",\"rtnMsg\":\"请求成功\",\"rtnData\":{\"list\":[{\"erp_comp_id\":\"00001\",\"erp_comp_name\":\"东东方懂法\",\"erpc_entity_code\":\"chaoji\",\"erp_comp_code\":\"bacc0021\",\"hr_comp_code\":\"hcaoooa\"},{\"erp_comp_id\":\"00001\",\"erp_comp_name\":\"东东方懂法\",\"erpc_entity_code\":\"chaoji\",\"erp_comp_code\":\"bacc0021\",\"hr_comp_code\":\"hcaoooa\"}]}}";
		String responseData = "Result [rtnCode=E, rtnMsg=系统异常, data=null]";
		
		for(int i=0;i<10000;i++) {
			Thread.sleep(10);
			QueueUtil.put(1, 9, url, requestData, responseData, "Num: "+i, "");
		}
		
		
		while(true) {
			Thread.sleep(10*1000);
		}
	}

}
