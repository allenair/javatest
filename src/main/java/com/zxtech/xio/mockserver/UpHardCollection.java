package com.zxtech.xio.mockserver;

import java.io.Serializable;

/*
 * @version 2.0
 */

public class UpHardCollection implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private java.sql.Timestamp up_time;
	
	private String elevator_id;
	
	private String parameter_str;
	
	private String hard_time;
	
	private String electric_flag;
	
	private String people_flag;
	
	private String room_electric_flag;
	
	private String room_maintain_flag;
	
	private String top_electric_flag;
	
	private String top_maintain_flag;
	
	private String alarm;
	
	private String err_info;

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

	public String getElevator_id() {
		return elevator_id;
	}

	public void setElevator_id(String elevator_id) {
		this.elevator_id = elevator_id;
	}

	public String getParameter_str() {
		return parameter_str;
	}

	public void setParameter_str(String parameter_str) {
		this.parameter_str = parameter_str;
	}

	public String getHard_time() {
		return hard_time;
	}

	public void setHard_time(String hard_time) {
		this.hard_time = hard_time;
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

	public String getErr_info() {
		return err_info;
	}

	public void setErr_info(String err_info) {
		this.err_info = err_info;
	}
	
	
}