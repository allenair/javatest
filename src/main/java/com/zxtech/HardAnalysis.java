package com.zxtech;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

import com.zxtech.bean.TransferElevatorParameter;
import com.zxtech.bean.UpHardAnalysis;


public class HardAnalysis {

	public static void main(String[] args) {
		HardAnalysis test = new HardAnalysis();
		
		TransferElevatorParameter bean = new TransferElevatorParameter();
		bean.setElectric("1");
		bean.setPeople("1");
		bean.setElevatorId("900000000000000080006");
		bean.setTime("20161021172345");
		bean.setParameterStr("AKACAwBIQwAAJGdzAQAAAABigAAAAAAAAA==");

		test.getAnalysisBean(bean);
	}

	private UpHardAnalysis getAnalysisBean(TransferElevatorParameter parameter){
		System.out.println("===GET>>>>"+parameter.getParameterStr());
		String srcParam = getParameterBitValue(parameter.getParameterStr());
		System.out.println("===FIN>>>>"+srcParam);
		System.out.println("===FIN02>>>"+getBinStr(srcParam));
		System.out.println("===FIN16>>>"+getHexStr(srcParam));
		
		UpHardAnalysis analyBean = new UpHardAnalysis();
		
		analyBean.setUp_time(new Timestamp(new Date().getTime()));
		analyBean.setElevator_code(parameter.getElevatorId());
		
		System.out.println("<<<ERR>>>"+getSubString(srcParam,0,8));
		int intTmp = getIntByString(getSubString(srcParam,0,8));
		if(intTmp>0){
			analyBean.setErr("E"+intTmp);
		}else{
			analyBean.setErr("0");
		}

		analyBean.setNav(getSubString(srcParam, 8, 1));
		analyBean.setIns(getSubString(srcParam, 9, 1));
		analyBean.setRun(getSubString(srcParam, 10, 1));
		analyBean.setDo_p(getSubString(srcParam, 11, 1));
		analyBean.setDol(getSubString(srcParam, 12, 1));
		analyBean.setDw(getSubString(srcParam, 13, 1));
		analyBean.setDcl(getSubString(srcParam, 14, 1));
		analyBean.setDz(getSubString(srcParam, 15, 1));
		analyBean.setEfo(getSubString(srcParam, 16, 1));
		analyBean.setCb(getSubString(srcParam, 17, 1));
		analyBean.setUp(getSubString(srcParam, 18, 1));
		analyBean.setDown(getSubString(srcParam, 19, 1));
		
		analyBean.setFl(getIntByString(getSubString(srcParam, 24, 16)));
		analyBean.setCnt(getIntByString(getSubString(srcParam, 40, 32)));
		analyBean.setDdfw(getIntByString(getSubString(srcParam, 72, 32)));
		analyBean.setHxxh(getIntByString(getSubString(srcParam, 104, 32)));
		
		System.out.println("<<<FL>>>"+getSubString(srcParam,24, 16));
		System.out.println("<<<CNT>>>"+getSubString(srcParam,40, 32));
		System.out.println("<<<DDFW>>>"+getSubString(srcParam,72, 16));
		System.out.println("<<<HXXH>>>"+getSubString(srcParam,104, 16));
		
		
		analyBean.setEs(getSubString(srcParam, 136, 1));
		analyBean.setSe(getSubString(srcParam, 137, 1));
		analyBean.setDfc(getSubString(srcParam, 138, 1));
		analyBean.setTci(getSubString(srcParam, 139, 1));
		analyBean.setEro(getSubString(srcParam, 140, 1));
		analyBean.setLv1(getSubString(srcParam, 141, 1));
		analyBean.setLv2(getSubString(srcParam, 142, 1));
		analyBean.setLs1(getSubString(srcParam, 143, 1));
		analyBean.setLs2(getSubString(srcParam, 144, 1));
		analyBean.setDob(getSubString(srcParam, 145, 1));
		analyBean.setDcb(getSubString(srcParam, 146, 1));
		analyBean.setLrd(getSubString(srcParam, 147, 1));
		analyBean.setDos(getSubString(srcParam, 148, 1));
		analyBean.setEfk(getSubString(srcParam, 149, 1));
		analyBean.setPks(getSubString(srcParam, 150, 1));
		analyBean.setRdol(getSubString(srcParam, 151, 1));
		analyBean.setRdcl(getSubString(srcParam, 152, 1));
		analyBean.setRdob(getSubString(srcParam, 153, 1));
		analyBean.setRdcb(getSubString(srcParam, 154, 1));
		analyBean.setOthers(srcParam.substring(155));
		
		analyBean.setElectric_flag(parameter.getElectric());
		analyBean.setPeople_flag(parameter.getPeople());	
		analyBean.setRoom_electric_flag(parameter.getRoomElectric());
		analyBean.setRoom_maintain_flag(parameter.getRoomMaintain());
		analyBean.setTop_electric_flag(parameter.getTopElectric());
		analyBean.setTop_maintain_flag(parameter.getTopMaintain());
		return analyBean;
	}
	
	private String getSubString(String srcStr, int start, int len){
		return srcStr.substring(start, start+len);
	}
	
	private String getParameterBitValue(String baseParam){
		StringBuilder strBuffer = new StringBuilder();
		byte[] srcArr = Base64.getDecoder().decode(baseParam);
		for (byte b : srcArr) {
			strBuffer.append(""+(b>>7&0x1));
			strBuffer.append(""+(b>>6&0x1));
			strBuffer.append(""+(b>>5&0x1));
			strBuffer.append(""+(b>>4&0x1));
			strBuffer.append(""+(b>>3&0x1));
			strBuffer.append(""+(b>>2&0x1));
			strBuffer.append(""+(b>>1&0x1));
			strBuffer.append(""+(b>>0&0x1));
		}
		return strBuffer.toString();
	}
	
	private String getHexStr(String str){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<str.length();i=i+8){
			sb.append(Integer.toHexString(Integer.parseInt(str.substring(i,i+4), 2)).toUpperCase());
			sb.append(Integer.toHexString(Integer.parseInt(str.substring(i+4,i+8), 2)).toUpperCase());
			sb.append(" ");
		}
		return sb.toString();
	}
	
	private String getBinStr(String str){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<str.length();i=i+8){
			sb.append(str.substring(i,i+8));
			sb.append(" ");
		}
		return sb.toString();
	}
	
	// 按照小端法取值
	// 序列是，。。。0x78 0x56 0x34 0x12。。。，按照0x12345678来解析
	private int getIntByString(String str){
		int len = str.length();
		int byteNum = len/8;
		StringBuilder sb = new StringBuilder();
		
		for(int i=byteNum-1; i>=0; i--){
			sb.append(str.substring(i*8, (i+1)*8));
		}
		
		return Integer.parseInt(sb.toString(), 2);
	}
	
	private int dealCount(Integer srcNum, String flag){
		int baseNum = 0;
		if(srcNum!=null){
			baseNum = srcNum.intValue();
		}
		if(flag.equals("1")){
			baseNum = baseNum + 1;
		}
		return baseNum;
	}
}
