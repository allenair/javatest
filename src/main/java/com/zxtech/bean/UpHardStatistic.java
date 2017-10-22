package com.zxtech.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;




public class UpHardStatistic implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String elevator_code;
	
	private java.sql.Timestamp update_time;
	
	private Integer ins_count;
	
	private Integer do_count;
	
	private Integer dz_count;
	
	private Integer efo_count;
	
	private Integer cb_count;
	
	private Integer cnt;
	
	private Integer ddfw;
	
	private Integer hxxh;
	
	private Integer es_count;
	
	private Integer se_count;
	
	private Integer dfc_count;
	
	private Integer tci_count;
	
	private Integer ero_count;
	
	private Integer lv1_count;
	
	private Integer lv2_count;
	
	private Integer ls1_count;
	
	private Integer ls2_count;
	
	private Integer dob_count;
	
	private Integer dcb_count;
	
	private Integer lrd_count;
	
	private Integer dos_count;
	
	private Integer efk_count;
	
	private Integer pks_count;
	
	private Integer rdol_count;
	
	private Integer rdcl_count;
	
	private Integer rdob_count;
	
	private Integer rdcb_count;
	
	
	/**
	* default val cols name array
	*/	
	private static String[] defaultValColArr = {
	  	"ins_count",
	  	"do_count",
	  	"dz_count",
	  	"efo_count",
	  	"cb_count",
	  	"cnt",
	  	"ddfw",
	  	"hxxh",
	  	"es_count",
	  	"se_count",
	  	"dfc_count",
	  	"tci_count",
	  	"ero_count",
	  	"lv1_count",
	  	"lv2_count",
	  	"ls1_count",
	  	"ls2_count",
	  	"dob_count",
	  	"dcb_count",
	  	"lrd_count",
	  	"dos_count",
	  	"efk_count",
	  	"pks_count",
	  	"rdol_count",
	  	"rdcl_count",
	  	"rdob_count",
	  	"rdcb_count"
	};
	
	/**
	* pk cols name array
	*/	
	private static String[] pkColArr = {
	  	"id"
	};
	
	private static String[] columnNameArr = {
		"id",
		"elevator_code",
		"update_time",
		"ins_count",
		"do_count",
		"dz_count",
		"efo_count",
		"cb_count",
		"cnt",
		"ddfw",
		"hxxh",
		"es_count",
		"se_count",
		"dfc_count",
		"tci_count",
		"ero_count",
		"lv1_count",
		"lv2_count",
		"ls1_count",
		"ls2_count",
		"dob_count",
		"dcb_count",
		"lrd_count",
		"dos_count",
		"efk_count",
		"pks_count",
		"rdol_count",
		"rdcl_count",
		"rdob_count",
		"rdcb_count"
	};
  
	public Integer getId () {
		return id;
	}
	
	public void setId (Integer obj) {
		id = obj;
	}
	
	public String getElevator_code () {
		return elevator_code;
	}
	
	public void setElevator_code (String obj) {
		elevator_code = obj;
	}
	
	public java.sql.Timestamp getUpdate_time () {
		return update_time;
	}
	
	public void setUpdate_time (java.sql.Timestamp obj) {
		update_time = obj;
	}
	
	public Integer getIns_count () {
		return ins_count;
	}
	
	public void setIns_count (Integer obj) {
		ins_count = obj;
	}
	
	public Integer getDo_count () {
		return do_count;
	}
	
	public void setDo_count (Integer obj) {
		do_count = obj;
	}
	
	public Integer getDz_count () {
		return dz_count;
	}
	
	public void setDz_count (Integer obj) {
		dz_count = obj;
	}
	
	public Integer getEfo_count () {
		return efo_count;
	}
	
	public void setEfo_count (Integer obj) {
		efo_count = obj;
	}
	
	public Integer getCb_count () {
		return cb_count;
	}
	
	public void setCb_count (Integer obj) {
		cb_count = obj;
	}
	
	public Integer getCnt () {
		return cnt;
	}
	
	public void setCnt (Integer obj) {
		cnt = obj;
	}
	
	public Integer getDdfw () {
		return ddfw;
	}
	
	public void setDdfw (Integer obj) {
		ddfw = obj;
	}
	
	public Integer getHxxh () {
		return hxxh;
	}
	
	public void setHxxh (Integer obj) {
		hxxh = obj;
	}
	
	public Integer getEs_count () {
		return es_count;
	}
	
	public void setEs_count (Integer obj) {
		es_count = obj;
	}
	
	public Integer getSe_count () {
		return se_count;
	}
	
	public void setSe_count (Integer obj) {
		se_count = obj;
	}
	
	public Integer getDfc_count () {
		return dfc_count;
	}
	
	public void setDfc_count (Integer obj) {
		dfc_count = obj;
	}
	
	public Integer getTci_count () {
		return tci_count;
	}
	
	public void setTci_count (Integer obj) {
		tci_count = obj;
	}
	
	public Integer getEro_count () {
		return ero_count;
	}
	
	public void setEro_count (Integer obj) {
		ero_count = obj;
	}
	
	public Integer getLv1_count () {
		return lv1_count;
	}
	
	public void setLv1_count (Integer obj) {
		lv1_count = obj;
	}
	
	public Integer getLv2_count () {
		return lv2_count;
	}
	
	public void setLv2_count (Integer obj) {
		lv2_count = obj;
	}
	
	public Integer getLs1_count () {
		return ls1_count;
	}
	
	public void setLs1_count (Integer obj) {
		ls1_count = obj;
	}
	
	public Integer getLs2_count () {
		return ls2_count;
	}
	
	public void setLs2_count (Integer obj) {
		ls2_count = obj;
	}
	
	public Integer getDob_count () {
		return dob_count;
	}
	
	public void setDob_count (Integer obj) {
		dob_count = obj;
	}
	
	public Integer getDcb_count () {
		return dcb_count;
	}
	
	public void setDcb_count (Integer obj) {
		dcb_count = obj;
	}
	
	public Integer getLrd_count () {
		return lrd_count;
	}
	
	public void setLrd_count (Integer obj) {
		lrd_count = obj;
	}
	
	public Integer getDos_count () {
		return dos_count;
	}
	
	public void setDos_count (Integer obj) {
		dos_count = obj;
	}
	
	public Integer getEfk_count () {
		return efk_count;
	}
	
	public void setEfk_count (Integer obj) {
		efk_count = obj;
	}
	
	public Integer getPks_count () {
		return pks_count;
	}
	
	public void setPks_count (Integer obj) {
		pks_count = obj;
	}
	
	public Integer getRdol_count () {
		return rdol_count;
	}
	
	public void setRdol_count (Integer obj) {
		rdol_count = obj;
	}
	
	public Integer getRdcl_count () {
		return rdcl_count;
	}
	
	public void setRdcl_count (Integer obj) {
		rdcl_count = obj;
	}
	
	public Integer getRdob_count () {
		return rdob_count;
	}
	
	public void setRdob_count (Integer obj) {
		rdob_count = obj;
	}
	
	public Integer getRdcb_count () {
		return rdcb_count;
	}
	
	public void setRdcb_count (Integer obj) {
		rdcb_count = obj;
	}
	
	
	/**
	* put all columns into a map
	*/
	public void putInMap(Map<String, Object> paramMap) {
		paramMap.put("id", this.id);
		paramMap.put("elevator_code", this.elevator_code);
		paramMap.put("update_time", this.update_time);
		paramMap.put("ins_count", this.ins_count);
		paramMap.put("do_count", this.do_count);
		paramMap.put("dz_count", this.dz_count);
		paramMap.put("efo_count", this.efo_count);
		paramMap.put("cb_count", this.cb_count);
		paramMap.put("cnt", this.cnt);
		paramMap.put("ddfw", this.ddfw);
		paramMap.put("hxxh", this.hxxh);
		paramMap.put("es_count", this.es_count);
		paramMap.put("se_count", this.se_count);
		paramMap.put("dfc_count", this.dfc_count);
		paramMap.put("tci_count", this.tci_count);
		paramMap.put("ero_count", this.ero_count);
		paramMap.put("lv1_count", this.lv1_count);
		paramMap.put("lv2_count", this.lv2_count);
		paramMap.put("ls1_count", this.ls1_count);
		paramMap.put("ls2_count", this.ls2_count);
		paramMap.put("dob_count", this.dob_count);
		paramMap.put("dcb_count", this.dcb_count);
		paramMap.put("lrd_count", this.lrd_count);
		paramMap.put("dos_count", this.dos_count);
		paramMap.put("efk_count", this.efk_count);
		paramMap.put("pks_count", this.pks_count);
		paramMap.put("rdol_count", this.rdol_count);
		paramMap.put("rdcl_count", this.rdcl_count);
		paramMap.put("rdob_count", this.rdob_count);
		paramMap.put("rdcb_count", this.rdcb_count);
	}
	
	/**
	* return the columns map
	*/
	public Map<String, Object> getInfoMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		this.putInMap(paramMap);
		return paramMap;
	}
	
	/**
	* remove default value and pk if it is null
	*/
	private Map<String, Object> dealWithMap(Map<String, Object> paramMap) {
		Set<String> set = new HashSet<String>();
		for (String colName : defaultValColArr) {
			set.add(colName);
		}
		for (String colName : pkColArr) {
			set.add(colName);
		}
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String colName = iterator.next();
			if(paramMap.get(colName) == null) {
				paramMap.remove(colName);
			}
		}
		return paramMap;
	}
	
	public Map<String, Object> setSymbolInsert(Map<String, Object> paramMap) {
		paramMap = dealWithMap(paramMap);
		Boolean flag = true;
		for (String ss : columnNameArr) {
			if (flag) {
				if (paramMap.containsKey(ss) && paramMap.get(ss) != null) {
					paramMap.put(ss+"Symbol", " ");
					flag = false;
				}
			} else {
				paramMap.put(ss+"Symbol", ", ");
			}
		}	
		return paramMap;
	}
	
	private Map<String, Object> setSymbolUpdateWithNullValue(Map<String, Object> paramMap) {
		return setSymbolUpdate(dealWithMap(paramMap));
	}
	
	private Map<String, Object> setSymbolUpdateWithoutNullValue(Map<String, Object> paramMap) {
		return setSymbolUpdate(dealWithMapNullVal(paramMap));
	}
	
	public Map<String, Object> setSymbolUpdate(Map<String, Object> paramMap) {
		Boolean flag = true;
		for (String ss : columnNameArr) {
			if (flag) {
				if (paramMap.containsKey(ss) && paramMap.get(ss) != null && !Arrays.asList(pkColArr).contains(ss)) {
					paramMap.put(ss+"Symbol", " ");
					flag = false;
				}
			} else {
				paramMap.put(ss+"Symbol", ", ");
			}
		}	
		return paramMap;
	}
	
	/**
	* remove null
	*/
	private Map<String, Object> dealWithMapNullVal(Map<String, Object> paramMap) {
		if(paramMap.get("id") == null) {
			paramMap.remove("id");
		}
		if(paramMap.get("elevator_code") == null) {
			paramMap.remove("elevator_code");
		}
		if(paramMap.get("update_time") == null) {
			paramMap.remove("update_time");
		}
		if(paramMap.get("ins_count") == null) {
			paramMap.remove("ins_count");
		}
		if(paramMap.get("do_count") == null) {
			paramMap.remove("do_count");
		}
		if(paramMap.get("dz_count") == null) {
			paramMap.remove("dz_count");
		}
		if(paramMap.get("efo_count") == null) {
			paramMap.remove("efo_count");
		}
		if(paramMap.get("cb_count") == null) {
			paramMap.remove("cb_count");
		}
		if(paramMap.get("cnt") == null) {
			paramMap.remove("cnt");
		}
		if(paramMap.get("ddfw") == null) {
			paramMap.remove("ddfw");
		}
		if(paramMap.get("hxxh") == null) {
			paramMap.remove("hxxh");
		}
		if(paramMap.get("es_count") == null) {
			paramMap.remove("es_count");
		}
		if(paramMap.get("se_count") == null) {
			paramMap.remove("se_count");
		}
		if(paramMap.get("dfc_count") == null) {
			paramMap.remove("dfc_count");
		}
		if(paramMap.get("tci_count") == null) {
			paramMap.remove("tci_count");
		}
		if(paramMap.get("ero_count") == null) {
			paramMap.remove("ero_count");
		}
		if(paramMap.get("lv1_count") == null) {
			paramMap.remove("lv1_count");
		}
		if(paramMap.get("lv2_count") == null) {
			paramMap.remove("lv2_count");
		}
		if(paramMap.get("ls1_count") == null) {
			paramMap.remove("ls1_count");
		}
		if(paramMap.get("ls2_count") == null) {
			paramMap.remove("ls2_count");
		}
		if(paramMap.get("dob_count") == null) {
			paramMap.remove("dob_count");
		}
		if(paramMap.get("dcb_count") == null) {
			paramMap.remove("dcb_count");
		}
		if(paramMap.get("lrd_count") == null) {
			paramMap.remove("lrd_count");
		}
		if(paramMap.get("dos_count") == null) {
			paramMap.remove("dos_count");
		}
		if(paramMap.get("efk_count") == null) {
			paramMap.remove("efk_count");
		}
		if(paramMap.get("pks_count") == null) {
			paramMap.remove("pks_count");
		}
		if(paramMap.get("rdol_count") == null) {
			paramMap.remove("rdol_count");
		}
		if(paramMap.get("rdcl_count") == null) {
			paramMap.remove("rdcl_count");
		}
		if(paramMap.get("rdob_count") == null) {
			paramMap.remove("rdob_count");
		}
		if(paramMap.get("rdcb_count") == null) {
			paramMap.remove("rdcb_count");
		}
		return paramMap;
	}	

	private static Map<String, Object> setSymbolPKPrior(Map<String, Object> paramMap, Boolean exclude_pk, Boolean isOr) {
		if (paramMap == null || paramMap.size() == 0) {
			return paramMap;
		} 
		if (exclude_pk) {
			for (String ss : pkColArr) {
				paramMap.put(ss+"Symbol", "exists");
			}
			paramMap.put("exclude_pk", true);
		}	
		Boolean flag = true;
		if (isOr) {
			paramMap.put("params_exists", true);
			for (String ss : columnNameArr) {
				if (flag) {
					if (paramMap.containsKey(ss) && !paramMap.containsKey(ss+"Symbol")) {
						paramMap.put(ss+"Symbol", " ");
						flag = false;
					}
				} else {
					paramMap.put(ss+"Symbol", " or ");
				}
			}
		}
		return paramMap;
	}
}