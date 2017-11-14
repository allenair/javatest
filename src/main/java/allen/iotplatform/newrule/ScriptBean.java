package allen.iotplatform.newrule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptBean {
	private List<LogicUnitBean> logic_unit;
	private Map<String, Object> init_parameter;
	private Map<String, Integer> inner_state;
	private Map<String, Integer> state;
	private Map<String, List<String>> state_priority;
	private Map<String, String> state_description;
	private List<String> stop_condition;

	public List<String> getStop_condition() {
		return stop_condition;
	}

	public void setStop_condition(List<String> stop_condition) {
		this.stop_condition = stop_condition;
	}

	private Map<String, LogicUnitBean> beanMap = new HashMap<>();

	public List<LogicUnitBean> getLogic_unit() {
		return logic_unit;
	}

	public void setLogic_unit(List<LogicUnitBean> logic_unit) {
		this.logic_unit = logic_unit;
	}

	public Map<String, Object> getInit_parameter() {
		return init_parameter;
	}

	public void setInit_parameter(Map<String, Object> init_parameter) {
		this.init_parameter = init_parameter;
	}

	public Map<String, Integer> getInner_state() {
		return inner_state;
	}

	public void setInner_state(Map<String, Integer> inner_state) {
		this.inner_state = inner_state;
	}

	public Map<String, Integer> getState() {
		return state;
	}

	public void setState(Map<String, Integer> state) {
		this.state = state;
	}

	public Map<String, List<String>> getState_priority() {
		return state_priority;
	}

	public void setState_priority(Map<String, List<String>> state_priority) {
		this.state_priority = state_priority;
	}

	public Map<String, String> getState_description() {
		return state_description;
	}

	public void setState_description(Map<String, String> state_description) {
		this.state_description = state_description;
	}

	public Map<String, LogicUnitBean> getBeanMap() {
		return beanMap;
	}

	public void setBeanMap(Map<String, LogicUnitBean> beanMap) {
		this.beanMap = beanMap;
	}

	public void init() {
		for (LogicUnitBean bean : this.logic_unit) {
			this.beanMap.put(bean.getLg_name(), bean);
		}
	}
}
