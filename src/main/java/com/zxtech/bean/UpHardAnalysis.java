package com.zxtech.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;


/*
 * @version 2.0
 */

public class UpHardAnalysis implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private java.sql.Timestamp up_time;
	
	private String elevator_code;
	
	private String err;
	
	private String nav;
	
	private String ins;
	
	private String run;
	
	private String do_p;
	
	private String dol;
	
	private String dw;
	
	private String dcl;
	
	private String dz;
	
	private String efo;
	
	private String cb;
	
	private String up;
	
	private String down;
	
	private Integer fl;
	
	private Integer cnt;
	
	private Integer ddfw;
	
	private Integer hxxh;
	
	private String es;
	
	private String se;
	
	private String dfc;
	
	private String tci;
	
	private String ero;
	
	private String lv1;
	
	private String lv2;
	
	private String ls1;
	
	private String ls2;
	
	private String dob;
	
	private String dcb;
	
	private String lrd;
	
	private String dos;
	
	private String efk;
	
	private String pks;
	
	private String rdol;
	
	private String rdcl;
	
	private String rdob;
	
	private String rdcb;
	
	private String others;
	
	private String electric_flag;
	
	private String people_flag;
	
	private String room_electric_flag;
	
	private String room_maintain_flag;
	
	private String top_electric_flag;
	
	private String top_maintain_flag;
	
	
	/**
	* default val cols name array
	*/	
	private static String[] defaultValColArr = {
	};
	
	/**
	* pk cols name array
	*/	
	private static String[] pkColArr = {
	  	"id"
	};
	
	private static String[] columnNameArr = {
		"id",
		"up_time",
		"elevator_code",
		"err",
		"nav",
		"ins",
		"run",
		"do_p",
		"dol",
		"dw",
		"dcl",
		"dz",
		"efo",
		"cb",
		"up",
		"down",
		"fl",
		"cnt",
		"ddfw",
		"hxxh",
		"es",
		"se",
		"dfc",
		"tci",
		"ero",
		"lv1",
		"lv2",
		"ls1",
		"ls2",
		"dob",
		"dcb",
		"lrd",
		"dos",
		"efk",
		"pks",
		"rdol",
		"rdcl",
		"rdob",
		"rdcb",
		"others",
		"electric_flag",
		"people_flag",
		"room_electric_flag",
		"room_maintain_flag",
		"top_electric_flag",
		"top_maintain_flag"
	};
  
	public Integer getId () {
		return id;
	}
	
	public void setId (Integer obj) {
		id = obj;
	}
	
	public java.sql.Timestamp getUp_time () {
		return up_time;
	}
	
	public void setUp_time (java.sql.Timestamp obj) {
		up_time = obj;
	}
	
	public String getElevator_code () {
		return elevator_code;
	}
	
	public void setElevator_code (String obj) {
		elevator_code = obj;
	}
	
	public String getErr () {
		return err;
	}
	
	public void setErr (String obj) {
		err = obj;
	}
	
	public String getNav () {
		return nav;
	}
	
	public void setNav (String obj) {
		nav = obj;
	}
	
	public String getIns () {
		return ins;
	}
	
	public void setIns (String obj) {
		ins = obj;
	}
	
	public String getRun () {
		return run;
	}
	
	public void setRun (String obj) {
		run = obj;
	}
	
	public String getDo_p () {
		return do_p;
	}
	
	public void setDo_p (String obj) {
		do_p = obj;
	}
	
	public String getDol () {
		return dol;
	}
	
	public void setDol (String obj) {
		dol = obj;
	}
	
	public String getDw () {
		return dw;
	}
	
	public void setDw (String obj) {
		dw = obj;
	}
	
	public String getDcl () {
		return dcl;
	}
	
	public void setDcl (String obj) {
		dcl = obj;
	}
	
	public String getDz () {
		return dz;
	}
	
	public void setDz (String obj) {
		dz = obj;
	}
	
	public String getEfo () {
		return efo;
	}
	
	public void setEfo (String obj) {
		efo = obj;
	}
	
	public String getCb () {
		return cb;
	}
	
	public void setCb (String obj) {
		cb = obj;
	}
	
	public String getUp () {
		return up;
	}
	
	public void setUp (String obj) {
		up = obj;
	}
	
	public String getDown () {
		return down;
	}
	
	public void setDown (String obj) {
		down = obj;
	}
	
	public Integer getFl () {
		return fl;
	}
	
	public void setFl (Integer obj) {
		fl = obj;
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
	
	public String getEs () {
		return es;
	}
	
	public void setEs (String obj) {
		es = obj;
	}
	
	public String getSe () {
		return se;
	}
	
	public void setSe (String obj) {
		se = obj;
	}
	
	public String getDfc () {
		return dfc;
	}
	
	public void setDfc (String obj) {
		dfc = obj;
	}
	
	public String getTci () {
		return tci;
	}
	
	public void setTci (String obj) {
		tci = obj;
	}
	
	public String getEro () {
		return ero;
	}
	
	public void setEro (String obj) {
		ero = obj;
	}
	
	public String getLv1 () {
		return lv1;
	}
	
	public void setLv1 (String obj) {
		lv1 = obj;
	}
	
	public String getLv2 () {
		return lv2;
	}
	
	public void setLv2 (String obj) {
		lv2 = obj;
	}
	
	public String getLs1 () {
		return ls1;
	}
	
	public void setLs1 (String obj) {
		ls1 = obj;
	}
	
	public String getLs2 () {
		return ls2;
	}
	
	public void setLs2 (String obj) {
		ls2 = obj;
	}
	
	public String getDob () {
		return dob;
	}
	
	public void setDob (String obj) {
		dob = obj;
	}
	
	public String getDcb () {
		return dcb;
	}
	
	public void setDcb (String obj) {
		dcb = obj;
	}
	
	public String getLrd () {
		return lrd;
	}
	
	public void setLrd (String obj) {
		lrd = obj;
	}
	
	public String getDos () {
		return dos;
	}
	
	public void setDos (String obj) {
		dos = obj;
	}
	
	public String getEfk () {
		return efk;
	}
	
	public void setEfk (String obj) {
		efk = obj;
	}
	
	public String getPks () {
		return pks;
	}
	
	public void setPks (String obj) {
		pks = obj;
	}
	
	public String getRdol () {
		return rdol;
	}
	
	public void setRdol (String obj) {
		rdol = obj;
	}
	
	public String getRdcl () {
		return rdcl;
	}
	
	public void setRdcl (String obj) {
		rdcl = obj;
	}
	
	public String getRdob () {
		return rdob;
	}
	
	public void setRdob (String obj) {
		rdob = obj;
	}
	
	public String getRdcb () {
		return rdcb;
	}
	
	public void setRdcb (String obj) {
		rdcb = obj;
	}
	
	public String getOthers () {
		return others;
	}
	
	public void setOthers (String obj) {
		others = obj;
	}
	
	public String getElectric_flag () {
		return electric_flag;
	}
	
	public void setElectric_flag (String obj) {
		electric_flag = obj;
	}
	
	public String getPeople_flag () {
		return people_flag;
	}
	
	public void setPeople_flag (String obj) {
		people_flag = obj;
	}
	
	public String getRoom_electric_flag () {
		return room_electric_flag;
	}
	
	public void setRoom_electric_flag (String obj) {
		room_electric_flag = obj;
	}
	
	public String getRoom_maintain_flag () {
		return room_maintain_flag;
	}
	
	public void setRoom_maintain_flag (String obj) {
		room_maintain_flag = obj;
	}
	
	public String getTop_electric_flag () {
		return top_electric_flag;
	}
	
	public void setTop_electric_flag (String obj) {
		top_electric_flag = obj;
	}
	
	public String getTop_maintain_flag () {
		return top_maintain_flag;
	}
	
	public void setTop_maintain_flag (String obj) {
		top_maintain_flag = obj;
	}
	
	
	/**
	* put all columns into a map
	*/
	public void putInMap(Map<String, Object> paramMap) {
		paramMap.put("id", this.id);
		paramMap.put("up_time", this.up_time);
		paramMap.put("elevator_code", this.elevator_code);
		paramMap.put("err", this.err);
		paramMap.put("nav", this.nav);
		paramMap.put("ins", this.ins);
		paramMap.put("run", this.run);
		paramMap.put("do_p", this.do_p);
		paramMap.put("dol", this.dol);
		paramMap.put("dw", this.dw);
		paramMap.put("dcl", this.dcl);
		paramMap.put("dz", this.dz);
		paramMap.put("efo", this.efo);
		paramMap.put("cb", this.cb);
		paramMap.put("up", this.up);
		paramMap.put("down", this.down);
		paramMap.put("fl", this.fl);
		paramMap.put("cnt", this.cnt);
		paramMap.put("ddfw", this.ddfw);
		paramMap.put("hxxh", this.hxxh);
		paramMap.put("es", this.es);
		paramMap.put("se", this.se);
		paramMap.put("dfc", this.dfc);
		paramMap.put("tci", this.tci);
		paramMap.put("ero", this.ero);
		paramMap.put("lv1", this.lv1);
		paramMap.put("lv2", this.lv2);
		paramMap.put("ls1", this.ls1);
		paramMap.put("ls2", this.ls2);
		paramMap.put("dob", this.dob);
		paramMap.put("dcb", this.dcb);
		paramMap.put("lrd", this.lrd);
		paramMap.put("dos", this.dos);
		paramMap.put("efk", this.efk);
		paramMap.put("pks", this.pks);
		paramMap.put("rdol", this.rdol);
		paramMap.put("rdcl", this.rdcl);
		paramMap.put("rdob", this.rdob);
		paramMap.put("rdcb", this.rdcb);
		paramMap.put("others", this.others);
		paramMap.put("electric_flag", this.electric_flag);
		paramMap.put("people_flag", this.people_flag);
		paramMap.put("room_electric_flag", this.room_electric_flag);
		paramMap.put("room_maintain_flag", this.room_maintain_flag);
		paramMap.put("top_electric_flag", this.top_electric_flag);
		paramMap.put("top_maintain_flag", this.top_maintain_flag);
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
		if(paramMap.get("up_time") == null) {
			paramMap.remove("up_time");
		}
		if(paramMap.get("elevator_code") == null) {
			paramMap.remove("elevator_code");
		}
		if(paramMap.get("err") == null) {
			paramMap.remove("err");
		}
		if(paramMap.get("nav") == null) {
			paramMap.remove("nav");
		}
		if(paramMap.get("ins") == null) {
			paramMap.remove("ins");
		}
		if(paramMap.get("run") == null) {
			paramMap.remove("run");
		}
		if(paramMap.get("do_p") == null) {
			paramMap.remove("do_p");
		}
		if(paramMap.get("dol") == null) {
			paramMap.remove("dol");
		}
		if(paramMap.get("dw") == null) {
			paramMap.remove("dw");
		}
		if(paramMap.get("dcl") == null) {
			paramMap.remove("dcl");
		}
		if(paramMap.get("dz") == null) {
			paramMap.remove("dz");
		}
		if(paramMap.get("efo") == null) {
			paramMap.remove("efo");
		}
		if(paramMap.get("cb") == null) {
			paramMap.remove("cb");
		}
		if(paramMap.get("up") == null) {
			paramMap.remove("up");
		}
		if(paramMap.get("down") == null) {
			paramMap.remove("down");
		}
		if(paramMap.get("fl") == null) {
			paramMap.remove("fl");
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
		if(paramMap.get("es") == null) {
			paramMap.remove("es");
		}
		if(paramMap.get("se") == null) {
			paramMap.remove("se");
		}
		if(paramMap.get("dfc") == null) {
			paramMap.remove("dfc");
		}
		if(paramMap.get("tci") == null) {
			paramMap.remove("tci");
		}
		if(paramMap.get("ero") == null) {
			paramMap.remove("ero");
		}
		if(paramMap.get("lv1") == null) {
			paramMap.remove("lv1");
		}
		if(paramMap.get("lv2") == null) {
			paramMap.remove("lv2");
		}
		if(paramMap.get("ls1") == null) {
			paramMap.remove("ls1");
		}
		if(paramMap.get("ls2") == null) {
			paramMap.remove("ls2");
		}
		if(paramMap.get("dob") == null) {
			paramMap.remove("dob");
		}
		if(paramMap.get("dcb") == null) {
			paramMap.remove("dcb");
		}
		if(paramMap.get("lrd") == null) {
			paramMap.remove("lrd");
		}
		if(paramMap.get("dos") == null) {
			paramMap.remove("dos");
		}
		if(paramMap.get("efk") == null) {
			paramMap.remove("efk");
		}
		if(paramMap.get("pks") == null) {
			paramMap.remove("pks");
		}
		if(paramMap.get("rdol") == null) {
			paramMap.remove("rdol");
		}
		if(paramMap.get("rdcl") == null) {
			paramMap.remove("rdcl");
		}
		if(paramMap.get("rdob") == null) {
			paramMap.remove("rdob");
		}
		if(paramMap.get("rdcb") == null) {
			paramMap.remove("rdcb");
		}
		if(paramMap.get("others") == null) {
			paramMap.remove("others");
		}
		if(paramMap.get("electric_flag") == null) {
			paramMap.remove("electric_flag");
		}
		if(paramMap.get("people_flag") == null) {
			paramMap.remove("people_flag");
		}
		if(paramMap.get("room_electric_flag") == null) {
			paramMap.remove("room_electric_flag");
		}
		if(paramMap.get("room_maintain_flag") == null) {
			paramMap.remove("room_maintain_flag");
		}
		if(paramMap.get("top_electric_flag") == null) {
			paramMap.remove("top_electric_flag");
		}
		if(paramMap.get("top_maintain_flag") == null) {
			paramMap.remove("top_maintain_flag");
		}
		return paramMap;
	}	
	

}