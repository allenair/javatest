package allen.iotplatform.newrule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptBean {
	private static Logger log = LoggerFactory.getLogger(ScriptBean.class);
	private List<LogicUnitBean> logic_unit;
	private Map<String, Object> init_parameter;
	private Map<String, Integer> inner_state;
	private Map<String, Integer> state;
	private Map<String, List<String>> state_priority;
	private Map<String, String> state_description;
	private List<String> except_state;

	public List<String> getExcept_state() {
		return except_state;
	}

	public void setExcept_state(List<String> except_state) {
		this.except_state = except_state;
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

	// 此处是对模板的规范化操作，在模板中一些没有必要的项目可以不予维护，但是为了在解析中的简便（不用进行各种为空的和规范性的检验），此处对内存中
	// 加载的模板对象进行规范化操作
	public void init() {
		if (this.init_parameter == null) {
			this.setInit_parameter(new HashMap<>());
		}
		if (this.inner_state == null) {
			this.setInner_state(new HashMap<>());
		}
		if (this.state == null) {
			log.error("==ERROR=There is not any OUTPUT??==");
			this.setState(new HashMap<>());
		}
		dealPriority();
		if (this.state_description == null) {
			this.setState_description(new HashMap<>());
		}
		if (this.except_state == null) {
			this.setExcept_state(new ArrayList<>());
		}

		if (this.logic_unit == null) {
			log.error("==ERROR=There is not any LOGIC CELL??==");
			this.logic_unit = new ArrayList<>();
		}
		for (LogicUnitBean bean : this.logic_unit) {
			if (StringUtils.isEmpty(bean.getLg_name())) {
				bean.setLg_name(UUID.randomUUID().toString());
			}
			if (bean.getDep_logic() == null) {
				bean.setDep_logic(new ArrayList<>());
			}
			if (bean.getCondition() == null) {
				bean.setCondition(new HashMap<>());
			}
			if (bean.getLogic() == null) {
				bean.setLogic(new HashMap<>());
			}
			if (bean.getValue_map() == null) {
				bean.setValue_map(new HashMap<>());
			}
			this.beanMap.put(bean.getLg_name(), bean);
		}
	}

	// 此处处理一个问题，就是如果模板优先级列表中没有数据或是数据少于state中的要求，则此处默认将所有state中数据加入，
	// 此处是对模板的修正，此处只修改模板的内存对象，并不变动模板文件
	// 把所有不包括在priority中的state都默认增加到，最小优先级（99）的列表中，以保证都能输出
	private void dealPriority() {
		List<String> lastPriorityList = new ArrayList<>();

		Map<String, String> allPriorityMap = new HashMap<>();
		if (this.state_priority != null) {
			for (String priorityKey : this.state_priority.keySet()) {
				for (String priorityState : this.state_priority.get(priorityKey)) {
					allPriorityMap.put(priorityState, "1");
				}
			}
		} else {
			this.setState_priority(new HashMap<>());
		}

		for (String stateKey : this.state.keySet()) {
			if (!allPriorityMap.containsKey(stateKey)) {
				lastPriorityList.add(stateKey);
			}
		}

		this.state_priority.put("99", lastPriorityList);
	}
}
