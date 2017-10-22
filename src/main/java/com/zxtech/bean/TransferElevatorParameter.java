package com.zxtech.bean;

import java.io.Serializable;

public class TransferElevatorParameter implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// 鹰慧设备id
	private String elevatorId;
	// 长度201的字符串（P01~P200）
	private String parameterStr;
	private String time;
	// 废弃不用，0-未通电，1-通电
	private String electric;
	// 0-无人，1-有人
	private String people;
	// 0-机房没电，1-机房有电
	private String roomElectric;
	// 0-机房没有维护，1-机房维护
	private String roomMaintain;
	// 0-矫顶没电，1-矫顶有电
	private String topElectric;
	// 0-矫顶没有维护，1-矫顶维护
	private String topMaintain;
	
	
	public String getRoomElectric() {
		return roomElectric;
	}
	public void setRoomElectric(String roomElectric) {
		this.roomElectric = roomElectric;
	}
	public String getRoomMaintain() {
		return roomMaintain;
	}
	public void setRoomMaintain(String roomMaintain) {
		this.roomMaintain = roomMaintain;
	}
	public String getTopElectric() {
		return topElectric;
	}
	public void setTopElectric(String topElectric) {
		this.topElectric = topElectric;
	}
	public String getTopMaintain() {
		return topMaintain;
	}
	public void setTopMaintain(String topMaintain) {
		this.topMaintain = topMaintain;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getElectric() {
		return electric;
	}
	public void setElectric(String electric) {
		this.electric = electric;
	}
	public String getPeople() {
		return people;
	}
	public void setPeople(String people) {
		this.people = people;
	}
	public String getElevatorId() {
		return elevatorId;
	}
	public void setElevatorId(String elevatorId) {
		this.elevatorId = elevatorId;
	}
	public String getParameterStr() {
		return parameterStr;
	}
	public void setParameterStr(String parameterStr) {
		this.parameterStr = parameterStr;
	}

}
