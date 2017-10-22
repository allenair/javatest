package allen.iotplatform;

import java.util.List;
import java.util.Map;

public class LogicUnitBean {
	private String lg_name;
	private List<String> dep_logic;
	private Map<String, String> condition;
	private Map<String, List<Map<String, Object>>> logic;
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
	public Map<String, List<Map<String, Object>>> getLogic() {
		return logic;
	}
	public void setLogic(Map<String, List<Map<String, Object>>> logic) {
		this.logic = logic;
	}
	
}
