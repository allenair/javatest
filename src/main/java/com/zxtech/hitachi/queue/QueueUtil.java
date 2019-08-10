package com.zxtech.hitachi.queue;

import java.util.concurrent.LinkedBlockingQueue;

public class QueueUtil {
	
	static LinkedBlockingQueue<ApiSync> queue = new LinkedBlockingQueue<>();
	
	//队列初始化
	public static void init() {
		new Thread(() -> {
			try {
				while(true) {   
					ApiSync apiSync = queue.take();
					System.out.println("C <<===Queue Size== "+queue.size());
					if (apiSync != null) {
				        int saveType = apiSync.getSaveType();
				        switch(saveType){
				        case 1:
				        	saveApiOperationLog(apiSync.getCategory(), apiSync.getUrl(), apiSync.getRequestData(),
				        			apiSync.getResponseData(), apiSync.getApiName());
				            break;
				        case 2:
//				        	saveApiErrorLog(apiSync.getCategory(), apiSync.getUrl(), apiSync.getRequestData(),
//				        			apiSync.getResponseData(), apiSync.getApiName(), apiSync.getMsg());
				            break;
				        case 3:
//				        	saveFailureDataApi(apiSync.getCategory(), apiSync.getUrl(), apiSync.getRequestData(),
//				        			apiSync.getResponseData(), apiSync.getApiName(), apiSync.getMsg());
				            break;
				        default:
				            break;
				        }
					}
	            }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}
	
	//添加队列
	public static void put(int saveType, int category, String url, String requestData,
			String responseData, String apiName, String msg) throws Exception {
		//保存队列对象
		ApiSync apiSync = new ApiSync();
		apiSync.setSaveType(saveType);
		apiSync.setCategory(category);
		apiSync.setUrl(url);
		apiSync.setRequestData(requestData);
		apiSync.setResponseData(responseData);
		apiSync.setApiName(apiName);
		apiSync.setMsg(msg);
		
		if (queue != null) {
//			queue.put(apiSync);
			queue.offer(apiSync);
		} 
//		else {
//			queue = new LinkedBlockingQueue<>();
//			queue.put(apiSync);
//		}
		
		System.out.println("P >>===Queue Size== "+queue.size());
	}
	
	//保存同步日志
	public static int saveApiOperationLog(int category, String url, String requestData,
			String responseData, String apiName) throws Exception {
		Thread.sleep(30);
		return DBUtil.saveApiOperationLog(category, url, requestData, responseData, apiName);
	}
	
	
}
