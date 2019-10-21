package com.zxtech.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class SimpleEpdRuleJson {

	public static void main(String[] args) throws Exception {
		try (BufferedReader fin = new BufferedReader(new FileReader("d:/newjson.json"));
				PrintWriter fout = new PrintWriter(new File("d:/newres.json"), "UTF-8")) {

			StringBuilder sb = new StringBuilder();
			fin.lines().forEach(sb::append);

			RuleJsonBean jsonBean = new RuleJsonBean();
			jsonBean = new Gson().fromJson(sb.toString(), jsonBean.getClass());

			RuleJsonBean simpleBean = new RuleJsonBean();
			// CPARA_InputParameterValueList赋值
			for (Map<String, Object> row : jsonBean.getCPARA_InputParameterValueList()) {
				Map<String, Object> simpleRow = new HashMap<>();
				simpleRow.put("PropertyName", row.get("PropertyName"));
				simpleRow.put("ValueList", row.get("ValueList"));
				
				Map<String, Object> innerData = (Map<String, Object>)row.get("Data");
				simpleRow.put("Type", innerData.get("Type"));
				simpleBean.getCPARA_InputParameterValueList().add(simpleRow);
			}

			// CPARA_InternalParameterValueList赋值
			for (Map<String, Object> row : jsonBean.getCPARA_InternalParameterValueList()) {
				Map<String, Object> simpleRow = new HashMap<>();
				simpleRow.put("PropertyName", row.get("PropertyName"));
				simpleRow.put("ValueList", row.get("ValueList"));
				
				Map<String, Object> innerData = (Map<String, Object>)row.get("Data");
				simpleRow.put("Type", innerData.get("Type"));
				simpleBean.getCPARA_InternalParameterValueList().add(simpleRow);
			}

			// CPARA_FormulaLinkup赋值
			for (Map<String, Object> row : jsonBean.getCPARA_FormulaLinkup()) {
				Map<String, Object> simpleRow = new HashMap<>();
				simpleRow.put("PropertyName", row.get("PropertyName"));
				if (row.get("Condition") != null) {
					List<Map<String, Object>> conditionList = (List<Map<String, Object>>) row.get("Condition");
					List<Map<String, Object>> newConditionList = new ArrayList<>();
					for (Map<String, Object> conRow : conditionList) {
						Map<String, Object> newConRow = new HashMap<>();
						List<Map<String, Object>> innerConditionList = (List<Map<String, Object>>) conRow
								.get("Conditions");
						List<Map<String, Object>> newInnerConditionList = new ArrayList<>();
						for (Map<String, Object> innerConRow : innerConditionList) {
							Map<String, Object> newInnerConRow = new HashMap<>();
							newInnerConRow.put("Key", innerConRow.get("Key"));
							newInnerConRow.put("Value", innerConRow.get("Value"));
							newInnerConditionList.add(newInnerConRow);
						}
						newConRow.put("Conditions", newInnerConditionList);

						List<Map<String, Object>> innerResultList = (List<Map<String, Object>>) conRow.get("Results");
						List<Map<String, Object>> newInnerResultList = new ArrayList<>();
						for (Map<String, Object> innerResRow : innerResultList) {
							Map<String, Object> newInnerResRow = new HashMap<>();
							newInnerResRow.put("Key", innerResRow.get("Key"));
							newInnerResRow.put("Value", innerResRow.get("Value"));
							newInnerResultList.add(newInnerResRow);
						}
						newConRow.put("Results", newInnerResultList);

						newConditionList.add(newConRow);
					}
					simpleRow.put("Condition", newConditionList);

				}

				Map<String, Object> dataMap = (Map<String, Object>) row.get("Data");
				Map<String, Object> newDataMap = new HashMap<>();
				for (String key : dataMap.keySet()) {
					newDataMap.put(key, dataMap.get(key));
				}
				simpleRow.put("Data", newDataMap);

				simpleBean.getCPARA_FormulaLinkup().add(simpleRow);
			}

			// CPARA_XYTable 赋值
			for (Map<String, Object> row : jsonBean.getCPARA_XYTable()) {
				Map<String, Object> simpleRow = new HashMap<>();
				simpleRow.put("TNo", row.get("TNo"));

				List<Map<String, Object>> conditionList = (List<Map<String, Object>>) row.get("Condition");
				List<Map<String, Object>> newConditionList = new ArrayList<>();
				for (Map<String, Object> conRow : conditionList) {
					Map<String, Object> newConRow = new HashMap<>();
					List<Map<String, Object>> innerConditionList = (List<Map<String, Object>>) conRow.get("Conditions");
					List<Map<String, Object>> newInnerConditionList = new ArrayList<>();
					for (Map<String, Object> innerConRow : innerConditionList) {
						Map<String, Object> newInnerConRow = new HashMap<>();
						newInnerConRow.put("Key", innerConRow.get("Key"));
						newInnerConRow.put("Value", innerConRow.get("Value"));
						newInnerConditionList.add(newInnerConRow);
					}
					newConRow.put("Conditions", newInnerConditionList);

					List<Map<String, Object>> innerResultList = (List<Map<String, Object>>) conRow.get("Results");
					List<Map<String, Object>> newInnerResultList = new ArrayList<>();
					for (Map<String, Object> innerResRow : innerResultList) {
						Map<String, Object> newInnerResRow = new HashMap<>();
						newInnerResRow.put("Key", innerResRow.get("Key"));
						newInnerResRow.put("Value", innerResRow.get("Value"));
						newInnerResultList.add(newInnerResRow);
					}
					newConRow.put("Results", newInnerResultList);

					newConditionList.add(newConRow);
				}

				simpleRow.put("Condition", newConditionList);
				simpleBean.getCPARA_XYTable().add(simpleRow);
			}

//			System.out.println(new Gson().toJson(simpleBean));
			System.out.println("=========FINISHED!!===========");
			fout.println(new Gson().toJson(simpleBean));
		}

	}

}

class RuleJsonBean {
	private List<Map<String, Object>> CPARA_ChangeRecord = new ArrayList<>();
	private List<Map<String, Object>> CPARA_FormulaLinkup = new ArrayList<>();
	private List<Map<String, Object>> CPARA_Script = new ArrayList<>();
	private List<Map<String, Object>> CPARA_XYTable = new ArrayList<>();
	private List<Map<String, Object>> CPARA_InternalParameterValueList = new ArrayList<>();
	private List<Map<String, Object>> CPARA_InputParameterValueList = new ArrayList<>();

	public List<Map<String, Object>> getCPARA_ChangeRecord() {
		return CPARA_ChangeRecord;
	}

	public void setCPARA_ChangeRecord(List<Map<String, Object>> cPARA_ChangeRecord) {
		CPARA_ChangeRecord = cPARA_ChangeRecord;
	}

	public List<Map<String, Object>> getCPARA_FormulaLinkup() {
		return CPARA_FormulaLinkup;
	}

	public void setCPARA_FormulaLinkup(List<Map<String, Object>> cPARA_FormulaLinkup) {
		CPARA_FormulaLinkup = cPARA_FormulaLinkup;
	}

	public List<Map<String, Object>> getCPARA_Script() {
		return CPARA_Script;
	}

	public void setCPARA_Script(List<Map<String, Object>> cPARA_Script) {
		CPARA_Script = cPARA_Script;
	}

	public List<Map<String, Object>> getCPARA_XYTable() {
		return CPARA_XYTable;
	}

	public void setCPARA_XYTable(List<Map<String, Object>> cPARA_XYTable) {
		CPARA_XYTable = cPARA_XYTable;
	}

	public List<Map<String, Object>> getCPARA_InternalParameterValueList() {
		return CPARA_InternalParameterValueList;
	}

	public void setCPARA_InternalParameterValueList(List<Map<String, Object>> cPARA_InternalParameterValueList) {
		CPARA_InternalParameterValueList = cPARA_InternalParameterValueList;
	}

	public List<Map<String, Object>> getCPARA_InputParameterValueList() {
		return CPARA_InputParameterValueList;
	}

	public void setCPARA_InputParameterValueList(List<Map<String, Object>> cPARA_InputParameterValueList) {
		CPARA_InputParameterValueList = cPARA_InputParameterValueList;
	}

}
