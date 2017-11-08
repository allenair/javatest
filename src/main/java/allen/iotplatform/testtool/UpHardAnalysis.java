package allen.iotplatform.testtool;

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

	private String rear_en;

	private String rdoo;

	private String logic_err;

	private String show_left;

	private String show_right;

	private Integer last_count;

	private Integer total_time;

	private String driver_err;

	private String logic_lock;

	private String sys_model;

	private Integer xh_time;

	private Integer arm_code;

	private Integer dsp_code;

	private String ver_code;

	private String safe_circle;

	private String open_fault;

	private String close_fault;

	private String up_switch;

	private String down_switch;

	private String stop_fault;

	private String lock_broken;

	private String speed_fault;

	private String go_top;

	private String go_down;

	private String driver_fault;

	private String logic_fault;

	private String logic_status;

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

	public String getRear_en() {
		return rear_en;
	}

	public void setRear_en(String rear_en) {
		this.rear_en = rear_en;
	}

	public String getRdoo() {
		return rdoo;
	}

	public void setRdoo(String rdoo) {
		this.rdoo = rdoo;
	}

	public String getLogic_err() {
		return logic_err;
	}

	public void setLogic_err(String logic_err) {
		this.logic_err = logic_err;
	}

	public String getShow_left() {
		return show_left;
	}

	public void setShow_left(String show_left) {
		this.show_left = show_left;
	}

	public String getShow_right() {
		return show_right;
	}

	public void setShow_right(String show_right) {
		this.show_right = show_right;
	}

	public Integer getLast_count() {
		return last_count;
	}

	public void setLast_count(Integer last_count) {
		this.last_count = last_count;
	}

	public Integer getTotal_time() {
		return total_time;
	}

	public void setTotal_time(Integer total_time) {
		this.total_time = total_time;
	}

	public String getDriver_err() {
		return driver_err;
	}

	public void setDriver_err(String driver_err) {
		this.driver_err = driver_err;
	}

	public String getLogic_lock() {
		return logic_lock;
	}

	public void setLogic_lock(String logic_lock) {
		this.logic_lock = logic_lock;
	}

	public String getSys_model() {
		return sys_model;
	}

	public void setSys_model(String sys_model) {
		this.sys_model = sys_model;
	}

	public Integer getXh_time() {
		return xh_time;
	}

	public void setXh_time(Integer xh_time) {
		this.xh_time = xh_time;
	}

	public Integer getArm_code() {
		return arm_code;
	}

	public void setArm_code(Integer arm_code) {
		this.arm_code = arm_code;
	}

	public Integer getDsp_code() {
		return dsp_code;
	}

	public void setDsp_code(Integer dsp_code) {
		this.dsp_code = dsp_code;
	}

	public String getVer_code() {
		return ver_code;
	}

	public void setVer_code(String ver_code) {
		this.ver_code = ver_code;
	}

	public String getSafe_circle() {
		return safe_circle;
	}

	public void setSafe_circle(String safe_circle) {
		this.safe_circle = safe_circle;
	}

	public String getOpen_fault() {
		return open_fault;
	}

	public void setOpen_fault(String open_fault) {
		this.open_fault = open_fault;
	}

	public String getClose_fault() {
		return close_fault;
	}

	public void setClose_fault(String close_fault) {
		this.close_fault = close_fault;
	}

	public String getUp_switch() {
		return up_switch;
	}

	public void setUp_switch(String up_switch) {
		this.up_switch = up_switch;
	}

	public String getDown_switch() {
		return down_switch;
	}

	public void setDown_switch(String down_switch) {
		this.down_switch = down_switch;
	}

	public String getStop_fault() {
		return stop_fault;
	}

	public void setStop_fault(String stop_fault) {
		this.stop_fault = stop_fault;
	}

	public String getLock_broken() {
		return lock_broken;
	}

	public void setLock_broken(String lock_broken) {
		this.lock_broken = lock_broken;
	}

	public String getSpeed_fault() {
		return speed_fault;
	}

	public void setSpeed_fault(String speed_fault) {
		this.speed_fault = speed_fault;
	}

	public String getGo_top() {
		return go_top;
	}

	public void setGo_top(String go_top) {
		this.go_top = go_top;
	}

	public String getGo_down() {
		return go_down;
	}

	public void setGo_down(String go_down) {
		this.go_down = go_down;
	}

	public String getDriver_fault() {
		return driver_fault;
	}

	public void setDriver_fault(String driver_fault) {
		this.driver_fault = driver_fault;
	}

	public String getLogic_fault() {
		return logic_fault;
	}

	public void setLogic_fault(String logic_fault) {
		this.logic_fault = logic_fault;
	}

	public String getLogic_status() {
		return logic_status;
	}

	public void setLogic_status(String logic_status) {
		this.logic_status = logic_status;
	}

}
