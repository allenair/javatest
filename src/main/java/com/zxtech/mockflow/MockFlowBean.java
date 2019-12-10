package com.zxtech.mockflow;

import java.util.List;

public class MockFlowBean {
	private String flowName;
	private List<Node> nodes;

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
	private List<String> preNodeNames;
	private List<String> needUserNames;
	private List<String> needRoleNames;
	private List<Condition> conditions;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public List<String> getPreNodeNames() {
		return preNodeNames;
	}

	public void setPreNodeNames(List<String> preNodeNames) {
		this.preNodeNames = preNodeNames;
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
}

class Condition {
	private String nextNodeName;
	private String condition;

	public String getNextNodeName() {
		return nextNodeName;
	}

	public void setNextNodeName(String nextNodeName) {
		this.nextNodeName = nextNodeName;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}