package com.zxtech.hitachi.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueUtil {
	
	static LinkedBlockingQueue<Map<String, String>> queue = new LinkedBlockingQueue<>();
	
	static AtomicInteger count = new AtomicInteger(0);
	
	//队列初始化
	public static void init() {
		new Thread(() -> {
			List<Map<String, String>> rowList = new ArrayList<>();
			try {
				while(true) {   
//					ApiSync apiSync = queue.take();
					Map<String, String> row = queue.take();
					System.out.println("C <<===Queue Size== "+queue.size());
					
					if(!"".equals(row.get("category"))) {
						rowList.add(row);
					}
					if (QueueUtil.count.get() > 5 || rowList.size() > 5) {
						saveApiOperationLogBatch(rowList);
						QueueUtil.count.set(0);
						rowList.clear();
					}
					
					
					
//					if (apiSync != null) {
//				        int saveType = apiSync.getSaveType();
//				        switch(saveType){
//				        case 1:
//				        	saveApiOperationLog(apiSync.getCategory(), apiSync.getUrl(), apiSync.getRequestData(),
//				        			apiSync.getResponseData(), apiSync.getApiName());
//				            break;
//				        default:
//				            break;
//				        }
//					}
					
					
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				int num = count.getAndIncrement();	
				System.out.println("COUNT >>===== "+num);
				if(num>5) {
					Map<String, String> row = new HashMap<String, String>();
					row.put("category", "");
					queue.offer(row);
				}
			}
		},100,1000);
	}
	
	//添加队列
//	public static void put(int saveType, int category, String url, String requestData,
//			String responseData, String apiName, String msg) throws Exception {
//		//保存队列对象
//		ApiSync apiSync = new ApiSync();
//		apiSync.setSaveType(saveType);
//		apiSync.setCategory(category);
//		apiSync.setUrl(url);
//		apiSync.setRequestData(requestData);
//		apiSync.setResponseData(responseData);
//		apiSync.setApiName(apiName);
//		apiSync.setMsg(msg);
//		
//		if (queue != null) {
//			queue.offer(apiSync);
//		} 
//		
//		System.out.println("P >>===Queue Size== "+queue.size());
//	}
	
	public static void putMap(int saveType, int category, String url, String requestData,
			String responseData, String apiName, String msg) throws Exception {

		Map<String, String> row = new HashMap<String, String>();
		row.put("category", ""+category);
		row.put("url", url);
		row.put("requestData", requestData);
		row.put("responseData", responseData);
		row.put("apiName", apiName);
		if (queue != null) {
			queue.offer(row);
		} 
		
//		System.out.println("P >>===Queue Size== "+queue.size());
	}
	
	//保存同步日志
	public static int saveApiOperationLog(int category, String url, String requestData,
			String responseData, String apiName) throws Exception {
		Thread.sleep(30);
		return DBUtil.saveApiOperationLog(category, url, requestData, responseData, apiName);
	}
	
	public static int[] saveApiOperationLogBatch(List<Map<String, String>> rows) throws Exception {
		if(rows.size()==0) {
			return null;
		}
		return DBUtil.saveApiOperationLogBatch(rows);
	}
	
}
