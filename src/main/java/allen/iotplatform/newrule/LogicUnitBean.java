package allen.iotplatform.newrule;

import java.util.List;
import java.util.Map;

public class LogicUnitBean {
	private String lg_name;
	private List<String> dep_logic;
	private Map<String, String> condition;
	private Map<String, Map<String, Object>> logic;
	private Map<String, List<Map<String, String>>> value_map;

	public String getLg_name() {
		return lg_name;
	}

	public void setLg_name(String lg_name) {
		this.lg_name = lg_name;
	}

	public List<String> getDep_logic() {
		return dep_logic;
	}

	public void setDep_logic(List<String> dep_logic) {
		this.dep_logic = dep_logic;
	}

	public Map<String, String> getCondition() {
		return condition;
	}

	public void setCondition(Map<String, String> condition) {
		this.condition = condition;
	}

	public Map<String, Map<String, Object>> getLogic() {
		return logic;
	}

	public void setLogic(Map<String, Map<String, Object>> logic) {
		this.logic = logic;
	}

	public Map<String, List<Map<String, String>>> getValue_map() {
		return value_map;
	}

	public void setValue_map(Map<String, List<Map<String, String>>> value_map) {
		this.value_map = value_map;
	}

}
