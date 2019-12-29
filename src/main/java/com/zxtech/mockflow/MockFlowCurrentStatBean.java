package com.zxtech.mockflow;

public class MockFlowCurrentStatBean {
	private String flowCode;
	private String currentNodeCode;
	private boolean startFlag;
	private boolean endFlag;

	public String getFlowCode() {
		return flowCode;
	}

	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}

	public String getCurrentNodeCode() {
		return currentNodeCode;
	}

	public void setCurrentNodeCode(String currentNodeCode) {
		this.currentNodeCode = currentNodeCode;
	}

	public boolean isStartFlag() {
		return startFlag;
	}

	public void setStartFlag(boolean startFlag) {
		this.startFlag = startFlag;
	}

	public boolean isEndFlag() {
		return endFlag;
	}

	public void setEndFlag(boolean endFlag) {
		this.endFlag = endFlag;
	}

}
