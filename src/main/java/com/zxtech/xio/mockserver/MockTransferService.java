package com.zxtech.xio.mockserver;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.zxtech.RedisUtil;
import com.zxtech.xio.iot.TransferElevatorParameter;

public class MockTransferService {
	private static GsonBuilder gsonBulder = new GsonBuilder();
	private static HashMap<String, String> errorDescriptMap = new HashMap<>();
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
		
		errorDescriptMap.put("101", "门区外停车困人");
		errorDescriptMap.put("102", "冲顶困人");
		errorDescriptMap.put("103", "蹲底困人");
		errorDescriptMap.put("104", "运行中开门困人");
		errorDescriptMap.put("105", "超速困人");
		errorDescriptMap.put("106", "轿厢意外移动困人");
		errorDescriptMap.put("107", "门锁回路断路困人");
		errorDescriptMap.put("108", "停电困人");
		errorDescriptMap.put("109", "报警困人");
		
		errorDescriptMap.put("201", "门区外停车");
		errorDescriptMap.put("202", "电梯冲顶");
		errorDescriptMap.put("203", "电梯蹲底");
		errorDescriptMap.put("204", "运行中开门");
		errorDescriptMap.put("205", "电梯速度异常");
		errorDescriptMap.put("206", "轿厢意外移动");
		errorDescriptMap.put("207", "门锁回路断路");
		errorDescriptMap.put("208", "电梯停电");
		errorDescriptMap.put("209", "制动力检测警告");
		errorDescriptMap.put("210", "制动力检测故障");
		errorDescriptMap.put("211", "平层光电开关故障");
		errorDescriptMap.put("212", "1LV 光电故障");
		errorDescriptMap.put("213", "2LV 光电故障");
		errorDescriptMap.put("214", "制动单元故障");
		errorDescriptMap.put("215", "变频器功率读取失败");
		errorDescriptMap.put("216", "综合故障");
		errorDescriptMap.put("217", "开门故障");
		errorDescriptMap.put("218", "关门故障");
		errorDescriptMap.put("219", "主机热敏开关动作");
		errorDescriptMap.put("220", "电梯频繁复位");
		errorDescriptMap.put("221", "门锁短接");
		errorDescriptMap.put("222", "强迫减速丢失");
		errorDescriptMap.put("223", "上下强减动作");
		errorDescriptMap.put("224", "安全开关动作");
	}
	
	public boolean handle(TransferElevatorParameter parameter) {
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
	
	private boolean checkApiParameter(TransferElevatorParameter parameter){
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
	
	private void updateAnalysisResult(TransferElevatorParameter parameter) {
		//===================开始处理将拆解后的接收数据存入数据库=================
		UpHardAnalysis analyBean = getAnalysisBean(parameter);
		
		// 在缓存中的该设备上一条数据
		
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
			analyBean.setPeople_flag(checkPeopleFlagNew(analyBean));
		}
		
		// 保存分析后的结果
		
		// 此处将准备存入缓存中的数据进行处理
		if("1".equals(analyBean.getRoom_maintain_flag())){
			analyBean.setShow_fl("--");
		}
		if("0".equals(analyBean.getRdcl()) && "0".equals(analyBean.getRdol())){
			analyBean.setRdob("9");
		}
		// 此处根据实际情况加入缓存，使用Redis
		RedisUtil.set("hard-analy:" + analyBean.getElevator_code(), gsonBulder.create().toJson(analyBean), 1800);
		
		//===================此处进行统计汇总记录
		
		
		//===================开始处理错误与报警问题=================
		
		
		//===================开始处理判断是否发出召修=================
		// 目前处于报警状态，进行召修时候需要查看目前是否有还未解决的召修，如有则不再发起新召修
		if(!maintanceFlag){
			String errorCode = isSendAlarm(analyBean);
			if(!"0".equals(errorCode)){
				//calCallFix(analyBean, errorCode);
				System.out.println("=========IOT=RULE=RETURN========"+errorCode);
			}
		}
	}
	
	private UpHardAnalysis getAnalysisBean(TransferElevatorParameter parameter) {
		String srcParam = getParameterBitValue(parameter.getParameterStr());
System.out.println("srcParam==========="+srcParam.length());
		UpHardAnalysis analyBean = new UpHardAnalysis();

		analyBean.setUp_time(new Timestamp(new Date().getTime()));
		analyBean.setElevator_code(parameter.getElevatorId());
		
		// 此处是指从srcParam字符串中，从第0位开始，截取8个字符
		int intTmp = getIntByString(getSubString(srcParam, 0, 8));
		if (intTmp > 0) {
			analyBean.setErr("E" + intTmp);
		} else {
			analyBean.setErr("0");
		}

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

		String up=leftLower(getIntByString(getSubString(srcParam, 168, 8))+"");//显示楼层高位
		String down=RightShow(getIntByString(getSubString(srcParam, 176, 8))+"");//显示楼层低位
		
		analyBean.setShow_fl(up+""+down);

		analyBean.setBoard_type(getIntByString(getSubString(srcParam, 184, 8))+"");
		
		analyBean.setEs(getSubString(srcParam, 143, 1));
		analyBean.setSe(getSubString(srcParam, 142, 1));
		analyBean.setDfc(getSubString(srcParam, 141, 1));
		analyBean.setTci(getSubString(srcParam, 140, 1));
		analyBean.setEro(getSubString(srcParam, 139, 1));
		analyBean.setLv1(getSubString(srcParam, 138, 1));
		analyBean.setLv2(getSubString(srcParam, 137, 1));
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
		analyBean.setOthers(srcParam.substring(156));

//		analyBean.setElectric_flag(parameter.getElectric());
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
	
	private String getSubString(String srcStr, int start, int len) {
		return srcStr.substring(start, start + len);
	}

	// 上传的ParameterStr是经过base64编码，因此此处需要先处理编码，然后再将bit位处理成字符，以便后续处理
	// 因此处理后整数如果是4个字节，那么就是32个字符
	private String getParameterBitValue(String baseParam) {
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
		}
		return strBuffer.toString();
	}

	// 按照小端法取值,只有获取整数(涉及多个字节问题)时候需要这样处理
	// 序列是，。。。0x78 0x56 0x34 0x12。。。，按照0x12345678来解析
	private int getIntByString(String str) {
		int len = str.length();
		int byteNum = len / 8;
		StringBuilder sb = new StringBuilder();

		for (int i = byteNum - 1; i >= 0; i--) {
			sb.append(str.substring(i * 8, (i + 1) * 8));
		}

		return Integer.parseInt(sb.toString(), 2);
	}

	/**
	 * 左侧的根据传过来的值进行判断显示的楼层信息
	 */
	private String leftLower(String info){
		String result=info;
		if(info.equals("37")){
			result="-";
		}else if(info.equals("10")){
			result="";
		}else if(info.substring(0, 1).equals("0") &&info.length()>1 ){//当是01,02这种格式的时候进行判断
			result=info.substring(1, 2);
		}else if(info.equals("36")){
			result="0";
		}
		return result;
	}
	/**
	 * 右侧的高位显示，需要显示的有字母等值
	 */
	private static String RightShow(String info) {
		String result = info;
		if (info.substring(0, 1).equals("0") && info.length() > 1) {
			result = info.substring(1, 2);
		} else if(info.equals("10")){
			result="";
		}else if(info.equals("00")){
			result="0";
		}else{
			switch (info) {
			case "11":result ="A";break;
			case "12":result ="B";break;
			case "13":result ="C";break;
			case "14":result ="D";break;
			case "15":result = "E";break;
			case "16":result = "F";break;
			case "17":result = "G";break;
			case "18":result = "H";break;
			
			case "19":result = "I";break;
			case "20":result = "J";break;
			case "21":result = "K";break;
			case "22":result = "L";break;
			
			case "23":result = "M";break;
			case "24":result = "N";break;
			case "25":result = "o";break;
			case "26":result = "P";break;
			case "27":result = "Q";break;
			case "28":result = "R";break;
			case "29":result = "S";break;
			case "30":result = "T";break;
			
			case "31":result = "U";break;
			case "32":result = "V";break;
			case "33":result = "W";break;
			case "34":result = "X";break;
			case "35":result = "Y";break;
			case "36":result = "Z";break;
			}
		}

		return result;
	}
	

	
	private String checkPeopleFlagNew(UpHardAnalysis analyBean) {
		String peopleFlag = analyBean.getPeople_flag();
        String do11 = analyBean.getDo_p();
        String dcl14 = analyBean.getDcl();
        String dz15 = analyBean.getDz();
        String elevatorCode = analyBean.getElevator_code();
        String realPeopleFlag="0";
        
        // 初始化缓存中的是否有人标记
        realPeopleFlag = RedisUtil.get("people_flag:"+elevatorCode);
        if(StringUtils.isBlank(realPeopleFlag)){
            realPeopleFlag="0";
            RedisUtil.set("people_flag:"+elevatorCode,realPeopleFlag);
        }
        
        String timeStr = RedisUtil.get("#parameter#-people-flag-lasttime:" + elevatorCode);
        if (StringUtils.isNotBlank(timeStr)) {
			long lastTime = Long.parseLong(timeStr);
			long nowTime = analyBean.getUp_time().getTime();
			// 已经到达最大判断时间
			if ((nowTime - lastTime) > 4 * 1000) {
				RedisUtil.set("#parameter#-people-flag-lasttime:" + elevatorCode, "", 1);
				// 在判断的最后一次，如果数量累积到了此次，应该没人，因为如果中间存在有人的情况，count不会累积到此时，因此应为没人
				realPeopleFlag = "0";
				RedisUtil.set("people_flag:" + elevatorCode, realPeopleFlag);
            }else{
                // 如果出现people=1，则判断有人
                if("1".equals(peopleFlag)){
                    realPeopleFlag="1";
                    RedisUtil.set("people_flag:"+elevatorCode,realPeopleFlag);
                    RedisUtil.set("#parameter#-people-flag-lasttime:" + elevatorCode, "", 1);
                }
            }
            
        }else{
            // 如果满足标记的同时，people=1，即刻判断为有人,否则记录起始时间
            if (("0".equals(do11) && "1".equals(dcl14)) || ("1".equals(do11) && "0".equals(dz15))) {
                if("1".equals(peopleFlag)){
                    realPeopleFlag="1";
                    RedisUtil.set("people_flag:"+elevatorCode,realPeopleFlag);
                }else{
                    RedisUtil.set("#parameter#-people-flag-lasttime:" + elevatorCode, ""+analyBean.getUp_time().getTime(), 5);
                }
            }
        }
        
        return realPeopleFlag;
	}
	
	
	private String isSendAlarm(UpHardAnalysis analyBean){
		String err=analyBean.getErr();
		
		boolean flagE6_10 = isContinueSeconds(analyBean, "E6", 10); // 101, 201
		boolean flagE11 = "E11".equalsIgnoreCase(err); // 102, 202
		boolean flagE12 = "E12".equalsIgnoreCase(err); // 103, 203
		boolean flagE7NotE11 = isNotShowNew(analyBean, "E7", "E11", 15); // 104, 204
		boolean flagE9NotE11 = isNotShowNew(analyBean, "E9", "E11", 10); // 105, 205
		boolean flagE107 = "E107".equalsIgnoreCase(err); //106, 206
		boolean flagE7_10 = isContinueSeconds(analyBean, "E7", 10); // 107, 207
		boolean flagElec = "0".equals(analyBean.getRoom_electric_flag()) && !"E157".equalsIgnoreCase(err); //108, 208
		boolean flagAlarm = isAlarmNew(analyBean, 10); // 109
		
		boolean flagE126 = "E126".equalsIgnoreCase(err); // 209
		boolean flagE125 = "E125".equalsIgnoreCase(err); // 210
		boolean flagE64 = "E64".equalsIgnoreCase(err); //211
		boolean flagE46 = "E46".equalsIgnoreCase(err); //212
		boolean flagE47 = "E47".equalsIgnoreCase(err); //213
		boolean flagP8 = "1".equalsIgnoreCase(analyBean.getNav()); //216
		boolean flagE2_500 = isContinueSeconds(analyBean, "E2", 500); //217
		boolean flagE3_500 = isContinueSeconds(analyBean, "E3", 500); //218
		boolean flagP137 = isShowInTimescope219(analyBean, 2*60*60); //219
		boolean flagE152P9 = isShowInTimescope220(analyBean, 24*60*60); //220
		boolean flagE120 = "E120".equalsIgnoreCase(err); // 221
		boolean flagE104 = "E104".equalsIgnoreCase(err); // 222
		boolean flagE105 = "E105".equalsIgnoreCase(err); // 223
		boolean flagP136 = "1".equalsIgnoreCase(analyBean.getEs()); // 224
		
		if(flagAlarm) {
			return "109";
		}
		// 第一类报警
		if("1".equals(analyBean.getPeople_flag())) {
			if(flagE6_10) {
				return "101";
			}
			if(flagE11) {
				return "102";
			}
			if(flagE12) {
				return "103";
			}
			if(flagE7NotE11) {
				return "104";
			}
			if(flagE9NotE11) {
				return "105";
			}
			if(flagE107) {
				return "106";
			}
			if(flagE7_10) {
				return "107";
			}
			if(flagElec) {
				return "108";
			}
		}else {
			if(flagE6_10) {
				return "201";
			}
			if(flagE11) {
				return "202";
			}
			if(flagE12) {
				return "203";
			}
			if(flagE7NotE11) {
				return "204";
			}
			if(flagE9NotE11) {
				return "205";
			}
			if(flagE107) {
				return "206";
			}
			if(flagE7_10) {
				return "207";
			}
			if(flagElec) {
				return "208";
			}
			
			if(flagE126) {
				return "209";
			}
			if(flagE125) {
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
			if(flagE2_500) {
				return "217";
			}
			if(flagE3_500) {
				return "218";
			}
			if(flagP137) {
				return "219";
			}
			if(flagE152P9) {
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
	
    // 10秒出现6次以上
    private boolean isAlarmNew(UpHardAnalysis analyBean, int seconds) {
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
            if(countNum>=6 && (nowTime-lastTime)<seconds*1000L){
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
	private boolean isContinueSeconds(UpHardAnalysis analyBean, String errCode, int secondes){
		if(!analyBean.getErr().equalsIgnoreCase(errCode)){
			RedisUtil.set("#parameter#"+ errCode +"-lasttime:"+ analyBean.getElevator_code(), "", 1);
			return false;
			
		}else{
			String tmp = RedisUtil.get("#parameter#"+ errCode +"-lasttime:"+ analyBean.getElevator_code());
			if(StringUtils.isBlank(tmp)){
				RedisUtil.set("#parameter#"+ errCode +"-lasttime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), secondes+10);
				return false;
				
			}else{
				long nowTime = analyBean.getUp_time().getTime();
				long lastTime = Long.parseLong(tmp);
				
				if((nowTime-lastTime)>=secondes*1000L){
					RedisUtil.set("#parameter#"+ errCode +"-lasttime:"+ analyBean.getElevator_code(), "", 1);
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
	private boolean isShowInTimescope219(UpHardAnalysis analyBean, int secondes){
		String tmpCount = RedisUtil.get("#parameter#P137#Count:"+ analyBean.getElevator_code());
		String tmpLastTime = RedisUtil.get("#parameter#P137#LastTime:"+ analyBean.getElevator_code());
		if (StringUtils.isNotBlank(tmpCount)) {
			int countNum = Integer.parseInt(tmpCount);
			
			if("0".equals(analyBean.getSe())){
				countNum++;
				RedisUtil.set("#parameter#P137#Count:"+ analyBean.getElevator_code(), ""+countNum, secondes);
				RedisUtil.set("#parameter#P137#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), secondes);
			}else{
				return false;
			}
	
			long nowTime = analyBean.getUp_time().getTime();
			long lastTime = Long.parseLong(tmpLastTime);
			if(countNum>=3 && (nowTime-lastTime)<secondes*1000L){
				RedisUtil.set("#parameter#P137#Count:"+ analyBean.getElevator_code(), "", 1);
				RedisUtil.set("#parameter#P137#LastTime:"+ analyBean.getElevator_code(), "", 1);
				return true;
				
			}else if((nowTime-lastTime)>=secondes*1000L){
				RedisUtil.set("#parameter#P137#Count:"+ analyBean.getElevator_code(), "", 1);
				RedisUtil.set("#parameter#P137#LastTime:"+ analyBean.getElevator_code(), "", 1);
				return false;
			}
			
		}else{
			if("0".equals(analyBean.getSe())){
				RedisUtil.set("#parameter#P137#Count:"+ analyBean.getElevator_code(), "1", secondes);
				RedisUtil.set("#parameter#P137#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), secondes);
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * 此处判断方式与219相同，需要处理的是例外情况“P9 解除后出现E152,60s 内E152 次数不计在这个范围内”
	 * 因此此处仅需要在计数位置增加以下例外情况的判断
	 * */
	private boolean isShowInTimescope220(UpHardAnalysis analyBean, int seconds){
		String tmpCount = RedisUtil.get("#parameter#E152#Count:"+ analyBean.getElevator_code());
		String tmpLastTime = RedisUtil.get("#parameter#E152#LastTime:"+ analyBean.getElevator_code());
		if (StringUtils.isNotBlank(tmpCount)) {
			int countNum = Integer.parseInt(tmpCount);
			
			if("E152".equalsIgnoreCase(analyBean.getErr()) && isP9Status(analyBean, 60)){
				countNum++;
				RedisUtil.set("#parameter#E152#Count:"+ analyBean.getElevator_code(), ""+countNum, seconds);
				RedisUtil.set("#parameter#E152#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), seconds);
			}else{
				return false;
			}
			
			long nowTime = analyBean.getUp_time().getTime();
			long lastTime = Long.parseLong(tmpLastTime);
			if(countNum>=5 && (nowTime-lastTime)<seconds*1000L){
				RedisUtil.set("#parameter#E152#Count:"+ analyBean.getElevator_code(), "", 1);
				RedisUtil.set("#parameter#E152#LastTime:"+ analyBean.getElevator_code(), "", 1);
				return true;
				
			}else if((nowTime-lastTime)>=seconds*1000L){
				RedisUtil.set("#parameter#E152#Count:"+ analyBean.getElevator_code(), "", 1);
				RedisUtil.set("#parameter#E152#LastTime:"+ analyBean.getElevator_code(), "", 1);
				return false;
			}
			
		}else{
			if("E152".equalsIgnoreCase(analyBean.getErr()) && isP9Status(analyBean, 60)){
				RedisUtil.set("#parameter#E152#Count:"+ analyBean.getElevator_code(), "1", seconds);
				RedisUtil.set("#parameter#E152#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), seconds);
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * 此处判断是否已经出了P9恢复的时间范围，处理220的“P9 解除后出现E152,60s 内E152 次数不计在这个范围内”这个例外
	 * 如果P9由1变为0，从此时开始60s内返回false，60s以上返回true
	 * 如果P9没有以上变化，则返回true
	 * 返回true代表不会对其他的判断进行阻止，返回false会进行阻止
	 * */
	private boolean isP9Status(UpHardAnalysis analyBean, int seconds){
		String tmpFlag = RedisUtil.get("#parameter#P9#Flag:"+ analyBean.getElevator_code());
		String tmpLastTime = RedisUtil.get("#parameter#P9#LastTime:"+ analyBean.getElevator_code());
		
		// 得到P9（INS）就置状态为1，并清空时间
		if("1".equals(analyBean.getIns())){
			RedisUtil.set("#parameter#P9#Flag:"+ analyBean.getElevator_code(),"1", seconds);
			RedisUtil.set("#parameter#P9#LastTime:"+ analyBean.getElevator_code(), "", seconds);
			return false;
			
		}else{ // 如果得到0,需要判断当前状态
			// 如果之前状态是1，说明此时是P9由1变为0,此时需要开始记录时间，并返回false
			if("1".equals(tmpFlag)){
				RedisUtil.set("#parameter#P9#Flag:"+ analyBean.getElevator_code(),"0", seconds);
				RedisUtil.set("#parameter#P9#LastTime:"+ analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), seconds);
				
				return false;
				
			}else if("0".equals(tmpFlag)){ // 如果之前状态是0，此时需要根据持续时间进行判断
				long nowTime = analyBean.getUp_time().getTime();
				long lastTime = Long.parseLong(tmpLastTime);
				// 超过指定秒数则不会阻止
				if((nowTime-lastTime)>=seconds*1000L){
					RedisUtil.set("#parameter#P9#Flag:"+ analyBean.getElevator_code(),"", 1);
					RedisUtil.set("#parameter#P9#LastTime:"+ analyBean.getElevator_code(), "", 1);
					return true;
					
				}else{
					return false;
				}
				
			}else{ // 如果之前状态为null，则直接返回true，不会阻止
				return true;
			}
		}
	}
	
	
	
	
	private boolean isNotShowNew(UpHardAnalysis analyBean, String conditionCode, String errCode, int seconds){
		//=========设置启动条件，判断前置错误信息
		// 此处是判断是否为条件判断的起始包（例如收到E9，之后再。。。）
		String conditionFlag = RedisUtil.get("#parameter-condition#"+ conditionCode +":"+ analyBean.getElevator_code());
		String lastTimeStr;
		if(StringUtils.isBlank(conditionFlag)){
			// 为空并且符合条件就开始计数
			if(analyBean.getErr().equalsIgnoreCase(conditionCode)){
				RedisUtil.set("#parameter-condition#"+ conditionCode +":"+ analyBean.getElevator_code(), "1", seconds*2);
				RedisUtil.set("#parameter#" + conditionCode + "&" + errCode + "-lasttime:" + analyBean.getElevator_code(), ""+analyBean.getUp_time().getTime(), seconds*2);
				return false;
			}
			
		}else{ // 如果不空说明已经开始判断
			// 如果等于errCode，那么直接返回false，因为此errCode任何情况不应出现
			if(analyBean.getErr().equalsIgnoreCase(errCode)){
				RedisUtil.set("#parameter-condition#"+ conditionCode +":"+ analyBean.getElevator_code(), "", 1);
				RedisUtil.set("#parameter#" + conditionCode + "&" + errCode +"-lasttime:"+ analyBean.getElevator_code(), "", 1);
				return false;
				
			}else{
				lastTimeStr = RedisUtil.get("#parameter#" + conditionCode + "&" + errCode +"-lasttime:"+ analyBean.getElevator_code());
				if (StringUtils.isNotBlank(lastTimeStr)) {
					long lastTime = Long.parseLong(lastTimeStr);
					long nowTime = analyBean.getUp_time().getTime();
					// 满足条件后需要初始化缓存
					if ((nowTime-lastTime) >= seconds * 1000) {
						RedisUtil.set("#parameter-condition#"+ conditionCode +":"+ analyBean.getElevator_code(), "", 1);
						RedisUtil.set("#parameter#" + conditionCode + "&" + errCode +"-lasttime:"+ analyBean.getElevator_code(), "", 1);
						return true;
						
					}
				}
			}
		}
		
		return false;
	}
	
}
