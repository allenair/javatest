package com.zxtech.hitachi.queue;

public class ApiSync {
	
	//保存类别，1-同步、2-错误、3-推送
	private int saveType;

	//请求类别，1-主动、2-被动
	private int category;

	//请求URL
	private String url;
	
	//请求数据
	private String requestData;
	
	//返回数据
	private String responseData;
	
	//接口名称
	private String apiName;
	
	//错误日志
	private String msg;

	public int getSaveType() {
		return saveType;
	}

	public void setSaveType(int saveType) {
		this.saveType = saveType;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
