package com.zxtech.mockflow;

import java.util.List;

public class MockFlowBean {
	private String flowName;
	private String flowCode;
	private List<Node> nodes;

	public String getFlowCode() {
		return flowCode;
	}

	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
}

class Node {
	private String nodeName;
	private String nodeCode;
	private String remark;
	private List<String> preNodeCodes;
	private List<String> needUserNames;
	private List<String> needRoleNames;
	private List<Condition> conditions;

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public List<String> getPreNodeCodes() {
		return preNodeCodes;
	}

	public void setPreNodeCodes(List<String> preNodeCodes) {
		this.preNodeCodes = preNodeCodes;
	}

	public List<String> getNeedUserNames() {
		return needUserNames;
	}

	public void setNeedUserNames(List<String> needUserNames) {
		this.needUserNames = needUserNames;
	}

	public List<String> getNeedRoleNames() {
		return needRoleNames;
	}

	public void setNeedRoleNames(List<String> needRoleNames) {
		this.needRoleNames = needRoleNames;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

class Condition {
	private String nextNodeCode;
	private String condition;

	public String getNextNodeCode() {
		return nextNodeCode;
	}

	public void setNextNodeCode(String nextNodeCode) {
		this.nextNodeCode = nextNodeCode;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}