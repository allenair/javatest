package com.zxtech.xio.mockserver;

import java.io.Serializable;

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
	
	private String alarm;
	
	private String maintenance;
	
	private String show_fl;
	
	private String err_info;
	
	private String board_type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.sql.Timestamp getUp_time() {
		return up_time;
	}

	public void setUp_time(java.sql.Timestamp up_time) {
		this.up_time = up_time;
	}

	public String getElevator_code() {
		return elevator_code;
	}

	public void setElevator_code(String elevator_code) {
		this.elevator_code = elevator_code;
	}

	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}

	public String getNav() {
		return nav;
	}

	public void setNav(String nav) {
		this.nav = nav;
	}

	public String getIns() {
		return ins;
	}

	public void setIns(String ins) {
		this.ins = ins;
	}

	public String getRun() {
		return run;
	}

	public void setRun(String run) {
		this.run = run;
	}

	public String getDo_p() {
		return do_p;
	}

	public void setDo_p(String do_p) {
		this.do_p = do_p;
	}

	public String getDol() {
		return dol;
	}

	public void setDol(String dol) {
		this.dol = dol;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public String getDcl() {
		return dcl;
	}

	public void setDcl(String dcl) {
		this.dcl = dcl;
	}

	public String getDz() {
		return dz;
	}

	public void setDz(String dz) {
		this.dz = dz;
	}

	public String getEfo() {
		return efo;
	}

	public void setEfo(String efo) {
		this.efo = efo;
	}

	public String getCb() {
		return cb;
	}

	public void setCb(String cb) {
		this.cb = cb;
	}

	public String getUp() {
		return up;
	}

	public void setUp(String up) {
		this.up = up;
	}

	public String getDown() {
		return down;
	}

	public void setDown(String down) {
		this.down = down;
	}

	public Integer getFl() {
		return fl;
	}

	public void setFl(Integer fl) {
		this.fl = fl;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public Integer getDdfw() {
		return ddfw;
	}

	public void setDdfw(Integer ddfw) {
		this.ddfw = ddfw;
	}

	public Integer getHxxh() {
		return hxxh;
	}

	public void setHxxh(Integer hxxh) {
		this.hxxh = hxxh;
	}

	public String getEs() {
		return es;
	}

	public void setEs(String es) {
		this.es = es;
	}

	public String getSe() {
		return se;
	}

	public void setSe(String se) {
		this.se = se;
	}

	public String getDfc() {
		return dfc;
	}

	public void setDfc(String dfc) {
		this.dfc = dfc;
	}

	public String getTci() {
		return tci;
	}

	public void setTci(String tci) {
		this.tci = tci;
	}

	public String getEro() {
		return ero;
	}

	public void setEro(String ero) {
		this.ero = ero;
	}

	public String getLv1() {
		return lv1;
	}

	public void setLv1(String lv1) {
		this.lv1 = lv1;
	}

	public String getLv2() {
		return lv2;
	}

	public void setLv2(String lv2) {
		this.lv2 = lv2;
	}

	public String getLs1() {
		return ls1;
	}

	public void setLs1(String ls1) {
		this.ls1 = ls1;
	}

	public String getLs2() {
		return ls2;
	}

	public void setLs2(String ls2) {
		this.ls2 = ls2;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getDcb() {
		return dcb;
	}

	public void setDcb(String dcb) {
		this.dcb = dcb;
	}

	public String getLrd() {
		return lrd;
	}

	public void setLrd(String lrd) {
		this.lrd = lrd;
	}

	public String getDos() {
		return dos;
	}

	public void setDos(String dos) {
		this.dos = dos;
	}

	public String getEfk() {
		return efk;
	}

	public void setEfk(String efk) {
		this.efk = efk;
	}

	public String getPks() {
		return pks;
	}

	public void setPks(String pks) {
		this.pks = pks;
	}

	public String getRdol() {
		return rdol;
	}

	public void setRdol(String rdol) {
		this.rdol = rdol;
	}

	public String getRdcl() {
		return rdcl;
	}

	public void setRdcl(String rdcl) {
		this.rdcl = rdcl;
	}

	public String getRdob() {
		return rdob;
	}

	public void setRdob(String rdob) {
		this.rdob = rdob;
	}

	public String getRdcb() {
		return rdcb;
	}

	public void setRdcb(String rdcb) {
		this.rdcb = rdcb;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getElectric_flag() {
		return electric_flag;
	}

	public void setElectric_flag(String electric_flag) {
		this.electric_flag = electric_flag;
	}

	public String getPeople_flag() {
		return people_flag;
	}

	public void setPeople_flag(String people_flag) {
		this.people_flag = people_flag;
	}

	public String getRoom_electric_flag() {
		return room_electric_flag;
	}

	public void setRoom_electric_flag(String room_electric_flag) {
		this.room_electric_flag = room_electric_flag;
	}

	public String getRoom_maintain_flag() {
		return room_maintain_flag;
	}

	public void setRoom_maintain_flag(String room_maintain_flag) {
		this.room_maintain_flag = room_maintain_flag;
	}

	public String getTop_electric_flag() {
		return top_electric_flag;
	}

	public void setTop_electric_flag(String top_electric_flag) {
		this.top_electric_flag = top_electric_flag;
	}

	public String getTop_maintain_flag() {
		return top_maintain_flag;
	}

	public void setTop_maintain_flag(String top_maintain_flag) {
		this.top_maintain_flag = top_maintain_flag;
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}

	public String getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(String maintenance) {
		this.maintenance = maintenance;
	}

	public String getShow_fl() {
		return show_fl;
	}

	public void setShow_fl(String show_fl) {
		this.show_fl = show_fl;
	}

	public String getErr_info() {
		return err_info;
	}

	public void setErr_info(String err_info) {
		this.err_info = err_info;
	}

	public String getBoard_type() {
		return board_type;
	}

	public void setBoard_type(String board_type) {
		this.board_type = board_type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}