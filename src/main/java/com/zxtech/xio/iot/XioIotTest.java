package com.zxtech.xio.iot;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import db.RedisUtil;

public class XioIotTest {
	private static GsonBuilder gsonBulder = new GsonBuilder();
	public static final TypeAdapter<java.sql.Timestamp> TIMESTAMP = new TypeAdapter<java.sql.Timestamp>() {
        @Override
        public java.sql.Timestamp read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                return new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(in.nextString()).getTime());
            } catch (Exception e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, java.sql.Timestamp value) throws IOException {
            out.value(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value));
        }
    };
	static {
		gsonBulder.registerTypeAdapter(java.sql.Timestamp.class, TIMESTAMP);
	}
	
	
	public static void main(String[] args) {
		System.out.println(getParameterBitValue("AEAAfwAAAAAAAAAAABUAAAAFgQElLS0AAAAAABhC0gAA/x0CAQEBAAAAAKqqqqohBAAAAACzAAAAAAAAAAAAcg=="));
		
		TransferElevatorParameter parameter = new TransferElevatorParameter();
		parameter.setElevatorId("900000000000000080172");
		parameter.setParameterStr("AMIAfwCAtwAAqBoBAAMAAAA0gQFoLS0AAAAAANyEywAA/xwCAQEBAAAAAKqqqqoIAAAAAAAAAAAAAAAAAAAAcg==");
		parameter.setTime("20171011111830");
		parameter.setPeople("0");
		parameter.setRoomElectric("1");
		parameter.setRoomMaintain("0");
		parameter.setTopElectric("9");
		parameter.setTopMaintain("9");
		parameter.setAlarm("0");
		parameter.setMaintenance("0");
		parameter.setErrInfo("111");
		
//		System.out.println(XioIotTest.handle(parameter));
		
		System.out.println(getIntByString("10101010101010101010101010101010"));
		
//		System.out.println(Integer.parseInt("10101010101010101010101010101010", 2));
	}
	
	
	public static boolean handle(TransferElevatorParameter parameter) {
		if (checkApiParameter(parameter)) {
			return false;
		}
		try{
			updateAnalysisResult(parameter);
		} catch(Exception e){
			System.out.println("HARD-ERROR:"+e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private static String getParameterBitValue(String baseParam) {
		StringBuilder strBuffer = new StringBuilder();
		byte[] srcArr = Base64.getDecoder().decode(baseParam);
		for (byte b : srcArr) {
			strBuffer.append("" + (b >> 7 & 0x1));
			strBuffer.append("" + (b >> 6 & 0x1));
			strBuffer.append("" + (b >> 5 & 0x1));
			strBuffer.append("" + (b >> 4 & 0x1));
			strBuffer.append("" + (b >> 3 & 0x1));
			strBuffer.append("" + (b >> 2 & 0x1));
			strBuffer.append("" + (b >> 1 & 0x1));
			strBuffer.append("" + (b >> 0 & 0x1));
			strBuffer.append(" ");
		}
		return strBuffer.toString();
	}
	
	/*此处如果返回true代表上传数据有不符合项，程序直接返回错误*/
	private static boolean checkApiParameter(TransferElevatorParameter parameter){
		// 上面四个字段不能为空
		if(StringUtils.isBlank(parameter.getElevatorId())){
			return true;
		}
		if(StringUtils.isBlank(parameter.getParameterStr())){
			return true;
		}	
		if(StringUtils.isBlank(parameter.getTime())){
			return true;
		}	
		if(StringUtils.isBlank(parameter.getPeople()) || parameter.getPeople().length()>1){
			return true;
		}
		
		// 以下字段由于都是标志位，因此长度最长为1
		if(StringUtils.isNotBlank(parameter.getRoomElectric()) && parameter.getRoomElectric().length()>1){
			return true;
		}
		if(StringUtils.isNotBlank(parameter.getRoomMaintain()) && parameter.getRoomMaintain().length()>1){
			return true;
		}
		if(StringUtils.isNotBlank(parameter.getTopElectric()) && parameter.getTopElectric().length()>1){
			return true;
		}
		if(StringUtils.isNotBlank(parameter.getTopMaintain()) && parameter.getTopMaintain().length()>1){
			return true;
		}
		if(StringUtils.isNotBlank(parameter.getAlarm()) && parameter.getAlarm().length()>1){
			return true;
		}
		if(StringUtils.isNotBlank(parameter.getErrInfo()) && parameter.getErrInfo().length()>3){
			return true;
		}
		
		return false;
	}
	
	private static void updateAnalysisResult(TransferElevatorParameter parameter) {
		//===================开始处理将拆解后的接收数据存入数据库=================
		UpHardAnalysis analyBean = getAnalysisBean(parameter);
		
		// 说明此时上传的数据长度不符合要求，需要512位
		if(analyBean==null) {
			System.out.println("=========IOT=Parameter=length=too=short========"+parameter.getElevatorId()+"##"+parameter.getParameterStr());
			return;
		}
		
		//===================开始进行判断是否处于检修状态
		boolean maintanceFlag=false;
		if("1".equals(analyBean.getRoom_maintain_flag())){
			maintanceFlag=true;
		}
		if("0".equals(analyBean.getRoom_maintain_flag())) {
			maintanceFlag=false;
		}
		
		//===================只有不是维修状态的时候，才需要真正进行是否有人的判断
		if(!maintanceFlag){
			analyBean.setPeople_flag(checkPeopleFlag(analyBean));
		}
		
		
		
		// 此处根据实际情况加入缓存，使用Redis
		RedisUtil.set("hard-analy:" + analyBean.getElevator_code(), gsonBulder.create().toJson(analyBean), 1800);
		
		/*==========目前上传内容里面包含统计信息，此处为加快处理速度，不再对上传信息进行统计，因此表up_hard_statistic不再使用，20170914*/
		
		//===================开始处理错误与报警问题=================
		
		
		//===================开始处理判断是否发出召修=================
		// 目前处于报警状态，进行召修时候需要查看目前是否有还未解决的召修，如有则不再发起新召修
		
	}
	
	// 进行ParameterStr参数分解，此处分解之后在经过后续的逻辑处理，会进行保存
		private static UpHardAnalysis getAnalysisBean(TransferElevatorParameter parameter) {
			String srcParam = getParameterBitValue(parameter.getParameterStr());
			
			if(srcParam.length()<512) {
				return null;
			}

			UpHardAnalysis analyBean = new UpHardAnalysis();

			analyBean.setUp_time(new Timestamp(new Date().getTime()));
			analyBean.setElevator_code(parameter.getElevatorId());
			
			// 此处字段修改为保存所有上传的字符串值，为保证调试的方便（2017-09-01）
			analyBean.setOthers(srcParam);
			
			// 此处是指从srcParam字符串中，从第0位开始，截取8个字符,此处系统保留不记录
			int intTmp = getIntByString(getSubString(srcParam, 0, 8));
			analyBean.setErr(""+intTmp);
			
			analyBean.setNav(getSubString(srcParam, 15, 1));
			analyBean.setIns(getSubString(srcParam, 14, 1));
			analyBean.setRun(getSubString(srcParam, 13, 1));
			analyBean.setDo_p(getSubString(srcParam, 12, 1));
			analyBean.setDol(getSubString(srcParam, 11, 1));
			analyBean.setDw(getSubString(srcParam, 10, 1));
			analyBean.setDcl(getSubString(srcParam, 9, 1));
			analyBean.setDz(getSubString(srcParam, 8, 1));

			analyBean.setEfo(getSubString(srcParam, 23, 1));
			analyBean.setCb(getSubString(srcParam, 22, 1));
			analyBean.setUp(getSubString(srcParam, 21, 1));
			analyBean.setDown(getSubString(srcParam, 20, 1));

			analyBean.setFl(getIntByString(getSubString(srcParam, 24, 16)));
			analyBean.setCnt(getIntByString(getSubString(srcParam, 40, 32)));
			analyBean.setDdfw(getIntByString(getSubString(srcParam, 72, 32)));
			analyBean.setHxxh(getIntByString(getSubString(srcParam, 104, 32)));
			
			analyBean.setEs(getSubString(srcParam, 143, 1));
			analyBean.setSe(getSubString(srcParam, 142, 1));
			analyBean.setDfc(getSubString(srcParam, 141, 1));
			analyBean.setTci(getSubString(srcParam, 140, 1));
			analyBean.setEro(getSubString(srcParam, 139, 1));
			analyBean.setLv1(getSubString(srcParam, 138, 1));
			//analyBean.setLv2(getSubString(srcParam, 137, 1));
			analyBean.setLs1(getSubString(srcParam, 136, 1));
			
			analyBean.setLs2(getSubString(srcParam, 151, 1));
			analyBean.setDob(getSubString(srcParam, 150, 1));
			analyBean.setDcb(getSubString(srcParam, 149, 1));
			analyBean.setLrd(getSubString(srcParam, 148, 1));
			analyBean.setDos(getSubString(srcParam, 147, 1));
			analyBean.setEfk(getSubString(srcParam, 146, 1));
			analyBean.setPks(getSubString(srcParam, 145, 1));
			analyBean.setRdol(getSubString(srcParam, 144, 1));
			
			analyBean.setRdcl(getSubString(srcParam, 159, 1));
			analyBean.setRdob(getSubString(srcParam, 158, 1));
			analyBean.setRdcb(getSubString(srcParam, 157, 1));
			analyBean.setRear_en(getSubString(srcParam, 156, 1));
			analyBean.setRdoo(getSubString(srcParam, 155, 1));

			analyBean.setLogic_err(""+getIntByString(getSubString(srcParam, 160, 8)));
			
			int up=getIntByString(getSubString(srcParam, 168, 8));//显示楼层高位
			int down=getIntByString(getSubString(srcParam, 176, 8));//显示楼层低位
			analyBean.setShow_left(""+up);
			analyBean.setShow_right(""+down);
			analyBean.setShow_fl(dealFloor(up, down));

			analyBean.setBoard_type(getIntByString(getSubString(srcParam, 184, 8))+"");
			
			analyBean.setLast_count(getIntByString(getSubString(srcParam, 192, 32)));
			analyBean.setTotal_time(getIntByString(getSubString(srcParam, 224, 32)));
			analyBean.setDriver_err(""+getIntByString(getSubString(srcParam, 256, 8)));
			analyBean.setLogic_lock(""+getIntByString(getSubString(srcParam, 264, 8)));
			analyBean.setSys_model(""+getIntByString(getSubString(srcParam, 272, 8)));
			analyBean.setXh_time(getIntByString(getSubString(srcParam, 280, 32)));
			analyBean.setArm_code(getIntByString(getSubString(srcParam, 312, 32)));
			analyBean.setDsp_code(getIntByString(getSubString(srcParam, 344, 32)));//******
			
			analyBean.setSafe_circle(getSubString(srcParam, 376, 1));
			analyBean.setOpen_fault(getSubString(srcParam, 377, 1));
			analyBean.setClose_fault(getSubString(srcParam, 378, 1));
			analyBean.setUp_switch(getSubString(srcParam, 379, 1));
			analyBean.setDown_switch(getSubString(srcParam, 380, 1));
			analyBean.setStop_fault(getSubString(srcParam, 381, 1));
			analyBean.setLock_broken(getSubString(srcParam, 382, 1));
			
			analyBean.setSpeed_fault(getSubString(srcParam, 384, 1));
			
			analyBean.setGo_top(getSubString(srcParam, 386, 1));
			analyBean.setGo_down(getSubString(srcParam, 387, 1));
			
			int tmpCode=0;
			tmpCode = getIntByString(getSubString(srcParam, 408, 8));
			if((tmpCode>=31 && tmpCode<=99) || (tmpCode>=220 && tmpCode<=255)) {
				analyBean.setDriver_fault("E"+tmpCode);
			}else {
				analyBean.setDriver_fault("0");
			}
			
			tmpCode=0;
			tmpCode = getIntByString(getSubString(srcParam, 416, 8));
			if(tmpCode>=100 && tmpCode<=150) {
				analyBean.setLogic_fault("E"+tmpCode);
			}else {
				analyBean.setLogic_fault("0");
			}
			
			tmpCode=0;
			tmpCode = getIntByString(getSubString(srcParam, 424, 8));
			if(tmpCode>=151 && tmpCode<=219) {
				analyBean.setLogic_status("E"+tmpCode);
			}else {
				analyBean.setLogic_status("0");
			}
			
			analyBean.setVer_code(""+getIntByString(getSubString(srcParam, 504, 8)));

//			analyBean.setElectric_flag(parameter.getElectric());
			analyBean.setPeople_flag(parameter.getPeople());
			analyBean.setRoom_electric_flag(parameter.getRoomElectric());
			analyBean.setRoom_maintain_flag(parameter.getRoomMaintain());
			analyBean.setTop_electric_flag(parameter.getTopElectric());
			analyBean.setTop_maintain_flag(parameter.getTopMaintain());
			
			analyBean.setAlarm(parameter.getAlarm());
			analyBean.setMaintenance(parameter.getMaintenance());
			analyBean.setErr_info(parameter.getErrInfo());
			
			return analyBean;
		}
		
		private static String getSubString(String srcStr, int start, int len) {
			return srcStr.substring(start, start + len);
		}
		
		// 按照小端法取值,只有获取整数(涉及多个字节问题)时候需要这样处理
		// 序列是，。。。0x78 0x56 0x34 0x12。。。，按照0x12345678来解析
		private static int getIntByString(String str) {
			int res=999999999;
			int len = str.length();
			int byteNum = len / 8;
			StringBuilder sb = new StringBuilder();

			for (int i = byteNum - 1; i >= 0; i--) {
				sb.append(str.substring(i * 8, (i + 1) * 8));
			}

			try {
				res = Integer.parseInt(sb.toString(), 2);
			}catch(Exception e) {
				System.out.println("String to Integer Error! String is "+sb.toString());
				res=999999999;
			}
			return res;
		}
		private static String dealFloor(int iL, int iR){
			int c=(int)'A';
			if(iL==37) {
				if(iR>=1 && iR<=9) {
					return "-"+iR;
				}
				if(iR==37) {
					return "--";
				}
			}
			if(iL==39 && iR==39) {
				return "**";
			}
			if(iL==10) {
				if(iR>=0 && iR<=9) {
					return ""+iR;
				}
				if(iR>=11 && iR<=36 && iR!=29) {
					return ""+((char)(iR-11+c));
				}
				if(iR==10) {
					return "";
				}
				if(iR==38) {
					return "S";
				}
			}
			if(iL>=1 && iL<=7) {
				if(iR>=0 && iR<=9) {
					return iL+""+iR;
				}
				if(iR>=11 && iR<=36) {
					return iL+""+((char)(iR-11+c));
				}
			}
			if(iL==8) {
				switch (iR) {
					case 0:
						return "12A";
					case 1:
						return "12B";
					case 2:
						return "13A";
					case 3:
						return "13B";
					case 4:
						return "14A";
					case 5:
						return "14B";
					case 6:
						return "15A";
					case 7:
						return "15B";
					case 8:
						return "17A";
					case 9:
						return "17B";
				}
				if(iR>=11 && iR<=36) {
					return iL+""+((char)(iR-11+c));
				}
			}
			if(iL==9) {
				switch (iR) {
					case 0:
						return "18A";
					case 1:
						return "18B";
					case 2:
						return "23A";
					case 3:
						return "23B";
					case 4:
						return "33A";
					case 5:
						return "33B";
				}
				if(iR>=11 && iR<=36) {
					return iL+""+((char)(iR-11+c));
				}
			}
			if(iL>=11 && iL<=36) {
				if(iR>=1 && iR<=9) {
					return ((char)(iL-11+c))+""+iR;
				}
			}
			
			return "EE";
		}
		
		private static String checkPeopleFlag(UpHardAnalysis analyBean) {
			String peopleFlag = analyBean.getPeople_flag();
			String p11 = analyBean.getDo_p();
			String p12 = analyBean.getDol();
			String p15 = analyBean.getDz();

			String elevatorCode = analyBean.getElevator_code();
			// 初始化缓存中的是否有人标记
			String realPeopleFlag = RedisUtil.get("people_flag:" + elevatorCode);
			realPeopleFlag = (realPeopleFlag == null ? peopleFlag : realPeopleFlag);

			if ("0".equals(p11)) {
				if ("1".equals(peopleFlag)) {
					RedisUtil.set("people_flag:" + elevatorCode, "1");
					return "1";
				} else {
					RedisUtil.set("people_flag:" + elevatorCode, "0");
					return "0";
				}
				
			} else if ("1".equals(p11) && "1".equals(p12) && "1".equals(p15)) {
				RedisUtil.set("people_flag:" + elevatorCode, "0");
				return "0";
			}
			
			return realPeopleFlag;
		}
		
		private static String isSendAlarm(UpHardAnalysis analyBean){
			boolean flagP381_10 = isContinueSeconds(analyBean, "P381", analyBean.getStop_fault(), 10); // 101, 201
			boolean flagP386 = "1".equals(analyBean.getGo_top()); // 102, 202
			boolean flagP387 = "1".equals(analyBean.getGo_down()); // 103, 203
			boolean flagP382NotP386 = isNotShow(analyBean, "P382", analyBean.getLock_broken(), "P386", analyBean.getGo_top(), 15); // 104, 204
			boolean flagP384NotP386 = isNotShow(analyBean, "P384", analyBean.getSpeed_fault(), "P386", analyBean.getGo_top(), 10); // 105, 205
			boolean flagE107 = "E107".equalsIgnoreCase(analyBean.getLogic_fault()); //106, 206
			boolean flagP382_10 = isContinueSeconds(analyBean, "P382", analyBean.getLock_broken(), 10); // 107, 207
			boolean flagElec = "0".equals(analyBean.getRoom_electric_flag()) && !"E157".equalsIgnoreCase(analyBean.getLogic_status()); //108, 208
			boolean flagAlarm = isAlarmNew(analyBean, 10); // 109
			
			boolean flagE126 = "E126".equalsIgnoreCase(analyBean.getLogic_fault()); // 209
			boolean flagE75 = "E75".equalsIgnoreCase(analyBean.getDriver_fault()); // 209
			
			boolean flagE125 = "E125".equalsIgnoreCase(analyBean.getLogic_fault()); // 210
			boolean flagE76 = "E76".equalsIgnoreCase(analyBean.getDriver_fault()); // 210
			
			boolean flagE64 = "E64".equalsIgnoreCase(analyBean.getDriver_fault()); //211
			boolean flagE46 = "E46".equalsIgnoreCase(analyBean.getDriver_fault()); //212
			boolean flagE47 = "E47".equalsIgnoreCase(analyBean.getDriver_fault()); //213
			boolean flagP8 = "1".equalsIgnoreCase(analyBean.getNav()); //216
			boolean flagP377_500 = isContinueSeconds(analyBean, "P377", analyBean.getOpen_fault(), 500); //217
			boolean flagP378_500 = isContinueSeconds(analyBean, "P378", analyBean.getClose_fault(), 500); //218
			boolean flagP137 = isShowInTimescope219(analyBean, 2*60*60); //219
			boolean flagE152 = isShowInTimescope220(analyBean, 24*60*60); //220
			boolean flagE120 = "E120".equalsIgnoreCase(analyBean.getLogic_fault()); // 221
			boolean flagE104 = "E104".equalsIgnoreCase(analyBean.getLogic_fault()); // 222
			boolean flagE105 = "E105".equalsIgnoreCase(analyBean.getLogic_fault()); // 223
			boolean flagP136 = "1".equalsIgnoreCase(analyBean.getEs()); // 224
			
			
			if(flagAlarm) {
				return "109";
			}
			// 第一类报警
			if("1".equals(analyBean.getPeople_flag())) {
				if(flagP381_10) {
					return "101";
				}
				if(flagP386) {
					return "102";
				}
				if(flagP387) {
					return "103";
				}
				if(flagP382NotP386) {
					return "104";
				}
				if(flagP384NotP386) {
					return "105";
				}
				if(flagE107) {
					return "106";
				}
				if(flagP382_10) {
					return "107";
				}
				if(flagElec) {
					return "108";
				}
				
			}else {
				if(flagP381_10) {
					return "201";
				}
				if(flagP386) {
					return "202";
				}
				if(flagP387) {
					return "203";
				}
				if(flagP382NotP386) {
					return "204";
				}
				if(flagP384NotP386) {
					return "205";
				}
				if(flagE107) {
					return "206";
				}
				if(flagP382_10) {
					return "207";
				}
				if(flagElec) {
					return "208";
				}
				
				if(flagE126 || flagE75) {
					return "209";
				}
				if(flagE125 || flagE76) {
					return "210";
				}
				if(flagE64) {
					return "211";
				}
				if(flagE46) {
					return "212";
				}
				if(flagE47) {
					return "213";
				}
				if(flagP8) {
					return "216";
				}
				if(flagP377_500) {
					return "217";
				}
				if(flagP378_500) {
					return "218";
				}
				if(flagP137) {
					return "219";
				}
				if(flagE152) {
					return "220";
				}
				if(flagE120) {
					return "221";
				}
				if(flagE104) {
					return "222";
				}
				if(flagE105) {
					return "223";
				}
				if(flagP136) {
					return "224";
				}
			}

			return "0";
		}

	    // 10秒出现3次以上
	    private static boolean isAlarmNew(UpHardAnalysis analyBean, int seconds) {
	    	String tmpCount = RedisUtil.get("#parameter#ALARM#Count:"+ analyBean.getElevator_code());
	        String tmpLastTime = RedisUtil.get("#parameter#ALARM#LastTime:"+ analyBean.getElevator_code());
	        if (StringUtils.isNotBlank(tmpCount)) {
	            int countNum = Integer.parseInt(tmpCount);
	            
	            if("1".equals(analyBean.getAlarm())){
	                countNum++;
	                RedisUtil.set("#parameter#ALARM#Count:"+ analyBean.getElevator_code(), ""+countNum, seconds);
	                RedisUtil.set("#parameter#ALARM#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), seconds);
	            }else{
	                return false;
	            }

	            long nowTime = analyBean.getUp_time().getTime();
	            long lastTime = Long.parseLong(tmpLastTime);
	            if(countNum>3 && (nowTime-lastTime)<seconds*1000L){
	                RedisUtil.set("#parameter#ALARM#Count:"+ analyBean.getElevator_code(), "", 1);
	                RedisUtil.set("#parameter#ALARM#LastTime:"+ analyBean.getElevator_code(), "", 1);
	                return true;
	                
	            }else if((nowTime-lastTime)>=seconds*1000L){
	                RedisUtil.set("#parameter#ALARM#Count:"+ analyBean.getElevator_code(), "", 1);
	                RedisUtil.set("#parameter#ALARM#LastTime:"+ analyBean.getElevator_code(), "", 1);
	                return false;
	            }
	            
	        }else{
	            if("1".equals(analyBean.getAlarm())){
	                RedisUtil.set("#parameter#ALARM#Count:"+ analyBean.getElevator_code(), "1", seconds);
	                RedisUtil.set("#parameter#ALARM#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), seconds);
	                return false;
	            }
	        }
	        
	        return false;
	    }
	    
		/**
		 * 错误errcode持续指定秒数，使用时间戳处理
		 * （1）如果收到的不是指定的errcode，则将缓存清空
		 * （2）如果收到的是指定的errcode
		 * A、如果缓存中没有记录，则表示此为第一次出现，因此需要把当时的时间戳（精确到毫秒）进行记录
		 * B、如果缓存中有记录，则表示前一个数据包已经出现了此errcode，因此需要计算一下缓存中的时间戳与当前时间戳的差值，
		 * 差值如果大于或等于指定的毫秒数则会触发报警，同时需要清除缓存状态信息
		 * */
		private static boolean isContinueSeconds(UpHardAnalysis analyBean, String paramCode, String val, int secondes){
			boolean flag=false;
			if("1".equals(val)) {
				flag=true;
			}
			
			if(!flag){
				RedisUtil.set("#parameter#"+ paramCode +"-lasttime:"+ analyBean.getElevator_code(), "", 1);
				return false;
				
			}else{
				String tmp = RedisUtil.get("#parameter#"+ paramCode +"-lasttime:"+ analyBean.getElevator_code());
				if(StringUtils.isBlank(tmp)){
					RedisUtil.set("#parameter#"+ paramCode +"-lasttime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), secondes+10);
					return false;
					
				}else{
					long nowTime = analyBean.getUp_time().getTime();
					long lastTime = Long.parseLong(tmp);
					
					if((nowTime-lastTime)>=secondes*1000L){
						RedisUtil.set("#parameter#"+ paramCode +"-lasttime:"+ analyBean.getElevator_code(), "", 1);
						return true;
					}
				}
			}
			return false;
		}
		
		/**
		 * 此处特殊处理219（主机热敏开关动作）报警，逻辑是：2 小时内3 次以上P137=0
		 * （1）此处需要同时存储计数数量与持续时间2个缓存状态
		 * （2）当从缓存中取到数据的时候，
		 * A、如果满足条件P137（Se）=0，则进行计数增加，同时需要更新缓存中的时间（时间需要始终存储最新的时间戳，来保证持续性的判断）。计数后进行次数与时间的判断
		 * （a）、如果在指定秒数（2小时=2*60*60）之内并且次数大于等于3次，则报警，并清除缓存
		 * （b）、如果超过指定秒数,则清空缓存
		 * B、如果不满足条件则退出
		 * （2）当从缓存中没有取到数据的时候，
		 * A、如果满足条件P137（Se）=0，则进行缓存的初始化
		 * B、否则什么都不处理
		 * */
		private static boolean isShowInTimescope219(UpHardAnalysis analyBean, int seconds){
			String tmpCount = RedisUtil.get("#parameter#P137#Count:"+ analyBean.getElevator_code());
			String tmpLastTime = RedisUtil.get("#parameter#P137#LastTime:"+ analyBean.getElevator_code());
			if (StringUtils.isNotBlank(tmpCount)) {
				int countNum = Integer.parseInt(tmpCount);
				
				if("0".equals(analyBean.getSe())){
					countNum++;
					RedisUtil.set("#parameter#P137#Count:"+ analyBean.getElevator_code(), ""+countNum, seconds);
					RedisUtil.set("#parameter#P137#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), seconds);
				}else{
					return false;
				}
		
				long nowTime = analyBean.getUp_time().getTime();
				long lastTime = Long.parseLong(tmpLastTime);
				if (countNum > 3 && (nowTime - lastTime) < seconds * 1000L) {
					RedisUtil.set("#parameter#P137#Count:"+ analyBean.getElevator_code(), "", 1);
					RedisUtil.set("#parameter#P137#LastTime:"+ analyBean.getElevator_code(), "", 1);
					return true;
					
				}else if((nowTime-lastTime)>=seconds*1000L){
					RedisUtil.set("#parameter#P137#Count:"+ analyBean.getElevator_code(), "", 1);
					RedisUtil.set("#parameter#P137#LastTime:"+ analyBean.getElevator_code(), "", 1);
					return false;
				}
				
			}else{
				if("0".equals(analyBean.getSe())){
					RedisUtil.set("#parameter#P137#Count:"+ analyBean.getElevator_code(), "1", seconds);
					RedisUtil.set("#parameter#P137#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), seconds);
					return false;
				}
			}
			
			return false;
		}
		
		/**
		 * 此处新版逻辑与219基本相同, 24小时5次以上E152
		 * */
		private static boolean isShowInTimescope220(UpHardAnalysis analyBean, int seconds){
			boolean e152Flag = "E152".equalsIgnoreCase(analyBean.getLogic_status());
			
			String tmpCount = RedisUtil.get("#parameter#E152#Count:"+ analyBean.getElevator_code());
			String tmpLastTime = RedisUtil.get("#parameter#E152#LastTime:"+ analyBean.getElevator_code());
			
			if (StringUtils.isNotBlank(tmpCount)) {
				int countNum = Integer.parseInt(tmpCount);
				
				if(e152Flag){
					countNum++;
					RedisUtil.set("#parameter#E152#Count:"+ analyBean.getElevator_code(), ""+countNum, seconds);
					RedisUtil.set("#parameter#E152#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), seconds);
				}else{
					return false;
				}
		
				long nowTime = analyBean.getUp_time().getTime();
				long lastTime = Long.parseLong(tmpLastTime);
				if (countNum > 5 && (nowTime - lastTime) < seconds * 1000L) {
					RedisUtil.set("#parameter#E152#Count:"+ analyBean.getElevator_code(), "", 1);
					RedisUtil.set("#parameter#E152#LastTime:"+ analyBean.getElevator_code(), "", 1);
					return true;
					
				}else if((nowTime-lastTime)>=seconds*1000L){
					RedisUtil.set("#parameter#E152#Count:"+ analyBean.getElevator_code(), "", 1);
					RedisUtil.set("#parameter#E152#LastTime:"+ analyBean.getElevator_code(), "", 1);
					return false;
				}
				
			}else{
				if(e152Flag){
					RedisUtil.set("#parameter#E152#Count:"+ analyBean.getElevator_code(), "1", seconds);
					RedisUtil.set("#parameter#E152#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), seconds);
					return false;
				}
			}
			
			return false;
		}
		
		private static boolean isNotShow(UpHardAnalysis analyBean, String conditionCode, String conditionVal, String notShowCode, String notShowVal, int seconds) {
			boolean conditionBool = false;
			if("1".equals(conditionVal)) {
				conditionBool=true;
			}
			boolean notShowBool = false;
			if("1".equals(notShowVal)) {
				notShowBool=true;
			}
			
			// =========设置启动条件，判断前置错误信息
			// 此处是判断是否为条件判断的起始包
			String conditionFlag = RedisUtil.get("#parameter-condition#" + conditionCode + ":" + analyBean.getElevator_code());
			String lastTimeStr;
			if (StringUtils.isBlank(conditionFlag)) {
				// 为空并且符合条件就开始计数
				if (conditionBool) {
					RedisUtil.set("#parameter-condition#" + conditionCode + ":" + analyBean.getElevator_code(), "1", seconds * 2);
					RedisUtil.set("#parameter#" + conditionCode + "&" + notShowCode + "-lasttime:" + analyBean.getElevator_code(), "" + analyBean.getUp_time().getTime(), seconds * 2);
					return false;
				}

			} else { // 如果不空说明已经开始判断
				// 如果出现notShowCode，那么直接返回false，因为此errCode任何情况不应出现
				if (notShowBool) {
					RedisUtil.set("#parameter-condition#" + conditionCode + ":" + analyBean.getElevator_code(), "", 1);
					RedisUtil.set("#parameter#" + conditionCode + "&" + notShowCode + "-lasttime:" + analyBean.getElevator_code(), "", 1);
					return false;

				} else {
					lastTimeStr = RedisUtil.get("#parameter#" + conditionCode + "&" + notShowCode + "-lasttime:" + analyBean.getElevator_code());
					if (StringUtils.isNotBlank(lastTimeStr)) {
						long lastTime = Long.parseLong(lastTimeStr);
						long nowTime = analyBean.getUp_time().getTime();
						// 满足条件后需要初始化缓存
						if ((nowTime - lastTime) >= seconds * 1000) {
							RedisUtil.set("#parameter-condition#" + conditionCode + ":" + analyBean.getElevator_code(), "", 1);
							RedisUtil.set("#parameter#" + conditionCode + "&" + notShowCode + "-lasttime:" + analyBean.getElevator_code(), "", 1);
							return true;
						}
					}
				}
			}

			return false;
		}
}
