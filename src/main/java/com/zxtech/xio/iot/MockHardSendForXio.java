package com.zxtech.xio.iot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import allen.okhttp.OKHttpTest;

public class MockHardSendForXio {
	private static final String url = "http://localhost:8080/ess/api/transferelevatorparameter.io";
	private static String filename = "d:/testE47.txt";
	private List<TransferElevatorParameter> mockDataList = new ArrayList<>();
	 
	
	public static void main(String[] args) {
		new MockHardSendForXio().doit();
	}
	
	public void doit() {
		try {
			getDataFromFile(this.filename);
			for (TransferElevatorParameter bean : this.mockDataList) {
				mockSend(bean);
				
				Thread.sleep(1000);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void mockSend(TransferElevatorParameter bean) throws Exception{
		OKHttpTest oksend = new OKHttpTest();
		String json = new Gson().toJson(bean);
		
		System.out.println(json);
		
		//String res = oksend.post(url, json);
		//System.out.println("GET>>:" + res + "SEND>>:" + json);
	}
	
	private void getDataFromFile(String fileName) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String tmp;
		while((tmp=br.readLine())!=null){
			TransferElevatorParameter bean = new TransferElevatorParameter();
			this.mockDataList.add(bean);
			
			String[] arr = tmp.trim().split(",",-1);
			bean.setElevatorId(getValue(arr[0]));
			bean.setParameterStr(getValue(arr[1]));
			bean.setTime(getValue(arr[2]));
			bean.setElectric(getValue(arr[3]));
			bean.setPeople(getValue(arr[4]));
			bean.setRoomElectric(getValue(arr[5]));
			bean.setRoomMaintain(getValue(arr[6]));
			bean.setTopElectric(getValue(arr[7]));
			bean.setTopMaintain(getValue(arr[8]));
			bean.setAlarm(getValue(arr[9]));
			bean.setErrInfo(getValue(arr[10]));
		}
		br.close();
	}
	
	private String getValue(String tmp){
		if(tmp==null){
			return "";
		}else{
			return tmp.trim();
		}
	}
}
