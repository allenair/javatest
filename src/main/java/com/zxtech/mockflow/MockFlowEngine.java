package com.zxtech.mockflow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class MockFlowEngine {
	private static Map<String, MockFlowBean> flowMap = new ConcurrentHashMap<>();
	private static Map<String, Map<String, Boolean>> flowRunSatus = new ConcurrentHashMap<>();
	private static Map<String, String> nameMap = new ConcurrentHashMap<>();
	private static Map<String, MockFlowCurrentStatBean> currentFlowBean = new ConcurrentHashMap<>();

	static {
		init();
	}

	private static void init() {
		try {
			ClassPathResource classPathResource = new ClassPathResource("/mock");

			File file = classPathResource.getFile();
			if (file.exists()) {
				Stream.<File>of(file.listFiles()).filter(f -> f.isFile()).forEach(f -> {
					try (BufferedReader br = new BufferedReader(new FileReader(f))) {
						StringBuilder jsonSb = new StringBuilder();
						br.lines().forEach(str -> jsonSb.append(str.trim()));

						MockFlowBean bean = new Gson().fromJson(jsonSb.toString(), MockFlowBean.class);
						flowMap.put(bean.getFlowCode(), bean);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> startProcess(String flowCode) {
		Map<String, String> resMap = new HashMap<>();
		String procInstId = UUID.randomUUID().toString();
		String startNodeCode = flowMap.get(flowCode).getNodes().get(0).getNodeCode();
		MockFlowCurrentStatBean bean = new MockFlowCurrentStatBean();
		bean.setFlowCode(flowCode);
		bean.setCurrentNodeCode(startNodeCode);
		bean.setStartFlag(true);
		bean.setEndFlag(false);
		
		currentFlowBean.put(procInstId, bean);
		
		resMap.put("procInstId", procInstId);
		resMap.put("startNodeCode", startNodeCode);
		return resMap;
	}
	
	public boolean cancelTask(String procInstId) {
		currentFlowBean.remove(procInstId);
		return true;
	}
	
	public String[] startFlow(String flowName) {
		String fid = UUID.randomUUID().toString();
		MockFlowBean bean = flowMap.get(flowName);
		if (bean == null) {
			return null;
		}
		Map<String, Boolean> nodeRunMap = new HashMap<>();
		bean.getNodes().forEach(node -> {
			if (node.getNodeName().equalsIgnoreCase("start")) {
				nodeRunMap.put(node.getNodeName(), true);
			} else {
				nodeRunMap.put(node.getNodeName(), false);
			}
		});

		flowRunSatus.put(fid, nodeRunMap);
		nameMap.put(fid, flowName);

		String nextName = runFlow(fid, bean.getNodes().get(0).getConditions().get(0).getNextNodeCode(), null);
		return new String[] { fid, nextName };
	}

	public String runFlow(String fid, String nodeName, final Map<String, String> params) {
		String flowName = nameMap.get(fid);
		if (flowName == null) {
			return "#err:fid";
		}

		MockFlowBean bean = flowMap.get(flowName);

		Node node = bean.getNodes().stream().filter(n -> n.getNodeName().equalsIgnoreCase(nodeName)).findFirst()
				.orElse(null);
		if (node == null) {
			return "#err:nodeName";
		}

		if (flowRunSatus.get(fid).get(node.getNodeName())) {
			return "#inner:finished";
		}

		boolean preFlag = node.getPreNodeCodes().stream().allMatch(pname -> {
			return flowRunSatus.get(fid).get(pname);
		});
		if (!preFlag) {
			return "#inner:prenode not finished";
		}

		String nextNodeName = node.getConditions().stream().filter(con -> dealExpress(con.getCondition(), params))
				.findFirst().orElse(new Condition()).getNextNodeCode();

		if (nextNodeName == null) {
			return "#inner:condition are all wrong";
		}

		if (nextNodeName == "end") {
			nameMap.remove(fid);
			flowRunSatus.remove(fid);
		}
		return nextNodeName;
	}

	public List<String> getAllNodeName(String flowName) {
		MockFlowBean flowBean = flowMap.get(flowName);
		List<String> nodeNameList = new ArrayList<>();
		flowBean.getNodes().forEach(n -> {
			if (!n.getNodeName().equalsIgnoreCase("start") && !n.getNodeName().equalsIgnoreCase("end")) {
				nodeNameList.add(n.getNodeName());
			}
		});
		return nodeNameList;
	}

	public List<String> getFinishedFlowNodes(String fid) {
		List<String> finishedList = new ArrayList<>();
		String flowName = nameMap.get(fid);
		List<String> allNodes = getAllNodeName(flowName);
		Map<String, Boolean> runMap = flowRunSatus.get(fid);

		allNodes.forEach(nodeName -> {
			if (runMap.get(nodeName)) {
				finishedList.add(nodeName);
			}
		});

		return finishedList;
	}

	public List<Map<String, String>> getUserTaskList(String userName) {
		List<Map<String, String>> resList = new ArrayList<>();

		for (String fid : flowRunSatus.keySet()) {
			String flowName = nameMap.get(fid);
			MockFlowBean bean = flowMap.get(flowName);
			Map<String, Boolean> runMap = flowRunSatus.get(fid);

			Node node = bean.getNodes().stream().filter(n -> {
				return !n.getNodeName().equalsIgnoreCase("start") && !n.getNodeName().equalsIgnoreCase("end")
						&& !runMap.get(n.getNodeName());
			}).findFirst().orElse(null);

			if (node == null) {
				continue;
			}
			Map<String, String> task = new HashMap<>();
			if (node.getNeedUserNames().contains(userName)) {
				task.put("fid", fid);
				task.put("nodeName", node.getNodeName());
				resList.add(task);
			} else {
				node.getNeedRoleNames().stream().forEach(role -> {
//					List<String> userNameList = rbacService.getUserByRole(role);
					List<String> userNameList = Arrays.asList("role1","role2");
					userNameList.stream().forEach(user -> {
						if (node.getNeedUserNames().contains(user)) {
							task.put("fid", fid);
							task.put("nodeName", node.getNodeName());
							resList.add(task);
						}
					});
				});
			}
		}

		return resList;
	}

	public void forceSetNodeStatus(String fid, String nodeName, boolean status) {
		flowRunSatus.get(fid).put(nodeName, status);
	}

	private boolean dealExpress(String express, Map<String, String> params) {
		if (params == null) {
			params = new HashMap<>();
		}

		express = express.replaceAll("'", "").trim();
		String[] arr = express.split("==");
		String paraName = arr[0].replaceAll("${", "").replaceAll("}", "").trim();
		String val = arr[1].trim();

		return val.equalsIgnoreCase(params.get(paraName));
	}
}
